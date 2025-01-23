package com.ty.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.ty.Entities.Presentation;
import com.ty.Entities.Rating;
import com.ty.Entities.User;
import com.ty.Enum.Role;
import com.ty.Exceptions.UserNotFound;
import com.ty.Repository.Presentation_Repository;
import com.ty.Repository.RatingRepo;
import com.ty.Repository.UserRepository;
import com.ty.Response.ResponseStructure;
import com.ty.dto.RatingDto;
import com.ty.dto.RatingResponse;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class RatingService {
	@Autowired
	private RatingRepo ratingrepo;

	@Autowired
	private UserRepository userrepo;

	@Autowired
	private Presentation_Repository presentationrepo;

	@Autowired
	private JavaMailSender mailSender;

	public ResponseEntity<?> AddRating(Integer aid, Integer uid, Integer pid, RatingDto rating) {
		// Validate the admin user
		User adminUser = userrepo.findById(aid)
				.orElseThrow(() -> new UserNotFound("Admin user not found with ID: " + aid));

		if (!adminUser.getRole().equals(Role.ADMIN)) {
			return new ResponseEntity<>("User is not an Admin; access denied.", HttpStatus.BAD_REQUEST);
		}

		// Validate the target user
		User targetUser = userrepo.findById(uid)
				.orElseThrow(() -> new UserNotFound("Target user not found with ID: " + uid));

		if (!targetUser.getRole().equals(Role.STUDENT)) {
			return new ResponseEntity<>("Target user is not a Student.", HttpStatus.BAD_REQUEST);
		}

		// Validate the presentation
		Presentation presentation = presentationrepo.findById(pid)
				.orElseThrow(() -> new UserNotFound("Presentation not found with ID: " + pid));

		// Automatically calculate the total score
		double totalScore = rating.getConfidence() + rating.getCommunication() + rating.getContent()
				+ rating.getInteraction() + rating.getLiveliness() + rating.getUsageProps();

		// Create and save the Rating
		Rating newRating = new Rating();
		newRating.setConfidence(rating.getConfidence());
		newRating.setCommunication(rating.getCommunication());
		newRating.setContent(rating.getContent());
		newRating.setInteraction(rating.getInteraction());
		newRating.setLiveliness(rating.getLiveliness());
		newRating.setUsageProps(rating.getUsageProps());
		newRating.setTotalScore(totalScore); // Set the calculated total score
		newRating.setUser(targetUser);
		newRating.setPresent(presentation);

		ratingrepo.save(newRating);

		return new ResponseEntity<>("Rating added successfully.", HttpStatus.OK);
	}

	public ResponseEntity<?> getbyid(Integer pid) {
		Rating r = ratingrepo.findById(pid).orElseThrow(() -> new UserNotFound("Prsentation Does't Exits"));
		if (r != null) {
			RatingResponse ratingResponse = new RatingResponse(r);
			return new ResponseEntity<RatingResponse>(ratingResponse, HttpStatus.OK);
		}
		return new ResponseEntity<>("Rating with given presentation not found", HttpStatus.BAD_REQUEST);
	}

	public ResponseEntity<?> getById(Integer pid) {
		// Fetch rating by presentation ID
		Rating rating = ratingrepo.findById(pid).orElseThrow(() -> new UserNotFound("Presentation Doesn't Exist"));

		// Return the RatingResponse
		RatingResponse ratingResponse = new RatingResponse(rating);
		return new ResponseEntity<>(ratingResponse, HttpStatus.OK);
	}

	public ResponseEntity<?> getAllRatingOfStudent(Integer uid) {
		// Fetch all ratings for the user
		List<Rating> userRatings = ratingrepo.findAllRatingByUser_Uid(uid);

		if (userRatings == null || userRatings.isEmpty()) {
			return new ResponseEntity<>("No ratings found for the given user ID.", HttpStatus.BAD_REQUEST);
		}

		// Convert ratings to response objects
		List<RatingResponse> ratingResponses = RatingResponse.fromRatings(userRatings);

		// Group ratings by presentation ID and calculate presentation-level averages
		Map<Integer, List<Rating>> presentationRatingsMap = userRatings.stream()
				.filter(rating -> rating.getPresent() != null) // Ensure presentation is not null
				.collect(Collectors.groupingBy(rating -> rating.getPresent().getPid())); // Group by Presentation ID

		for (Map.Entry<Integer, List<Rating>> entry : presentationRatingsMap.entrySet()) {
			Integer presentationId = entry.getKey();
			List<Rating> presentationRatings = entry.getValue();

			// Calculate average rating for the presentation
			double presentationAvg = presentationRatings.stream()
					.mapToDouble(r -> (r.getCommunication() + r.getConfidence() + r.getContent() + r.getInteraction()
							+ r.getLiveliness() + r.getUsageProps()) / 6.0)
					.average().orElse(0.0);

			// Update the presentation with the calculated average
			presentationrepo.findById(presentationId).ifPresent(presentation -> {
				presentation.setUserTotalScore(presentationAvg);
				presentationrepo.save(presentation);
			});
		}

		// Fetch presentations associated with the user
		List<Presentation> userPresentations = presentationrepo.findByUserId(uid);
		if (userPresentations == null || userPresentations.isEmpty()) {
			return new ResponseEntity<>("No presentations found for the given user ID.", HttpStatus.BAD_REQUEST);
		}

		// Calculate the user's total score across all presentations
		double userTotalScore = userPresentations.stream().mapToDouble(Presentation::getUserTotalScore).average()
				.orElse(0.0);

		// Update the user's total score
		Optional<User> userOpt = userrepo.findById(uid);
		if (userOpt.isPresent()) {
			User user = userOpt.get();
			user.setUser_total_score(userTotalScore);
			userrepo.save(user);

			// Send email to the user
			try {
				sendEmail(user.getEmail(), "User Total Score Updated", "Your total score is: " + userTotalScore);
			} catch (MessagingException e) {
				return new ResponseEntity<>("Failed to send email.", HttpStatus.INTERNAL_SERVER_ERROR);
			}

			// Create a success response
			ResponseStructure<String> rs = new ResponseStructure<>();
			rs.setMessage("User Total Score Updated Successfully");
			rs.setData("Total Score: " + userTotalScore);
			rs.setStatusCode(HttpStatus.OK.value());

			return new ResponseEntity<>(rs, HttpStatus.OK);
		}

		return new ResponseEntity<>("User not found.", HttpStatus.BAD_REQUEST);
	}

	private void sendEmail(String to, String subject, String text) throws MessagingException {
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, true);
		helper.setTo(to);
		helper.setSubject(subject);
		helper.setText(text, true);
		mailSender.send(message);
	}
}
