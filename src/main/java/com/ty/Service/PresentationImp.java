package com.ty.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ty.Entities.Presentation;
import com.ty.Entities.User;
import com.ty.Enum.PresentationStatus;
import com.ty.Enum.Role;
import com.ty.Exceptions.UserNotFound;
import com.ty.Repository.Presentation_Repository;
import com.ty.Repository.UserRepository;
import com.ty.dto.PresentationDTO;
import com.ty.dto.UpdateRequest;

import jakarta.transaction.Transactional;

@Transactional
@Service
public class PresentationImp implements PresentationService {

	@Autowired
	private UserRepository repo;
	
	
	@Autowired
	private Presentation_Repository present_repo;
	
	



	@Override
	@Transactional
	@JsonIgnore
	public ResponseEntity<?> add(Integer adminId, Integer userId, Presentation presentation) {
	    // Validate admin
	    User admin = repo.findById(adminId)
	            .orElseThrow(() -> new UserNotFound("Admin not found with ID: " + adminId));
	    if (!admin.getRole().equals(Role.ADMIN)) {
	    	
			return new ResponseEntity<String> ("User Is Not Admin" ,HttpStatus.OK);
	    }

	    // Validate target user
	    User targetUser = repo.findById(userId)
	            .orElseThrow(() -> new UserNotFound("User not found with ID: " + userId));
	    if (!targetUser.getRole().equals(Role.STUDENT)) {
	      
	     
	        return new ResponseEntity<>("", HttpStatus.BAD_REQUEST);
	    }

	    // Validate presentation object
    if (presentation == null) {
       
     
	        return new ResponseEntity<>("Invalid presentation details.", HttpStatus.BAD_REQUEST);
    }

	    // Create a new Presentation and associate it with the user
	    Presentation newPresentation = new Presentation();
	    newPresentation.setPid(presentation.getPid());
	    newPresentation.setCourse(presentation.getCourse());
	    newPresentation.setTopic(presentation.getTopic());
	    newPresentation.setUserTotalScore(presentation.getUserTotalScore());
	    newPresentation.setPresentation(presentation.getPresentation());
	    newPresentation.setUser(targetUser);

	    // Ensure both sides of the relationship are updated
	    if (targetUser.getPresentations() == null) {
	        targetUser.setPresentations(new ArrayList<>());
	    }
	    targetUser.getPresentations().add(newPresentation);

	    // Save the updated user object
	    repo.save(targetUser);

	    // Prepare the response


	    return new ResponseEntity<>("Presentation Added Successfully", HttpStatus.OK);
	}





	@Override
	public ResponseEntity<?> fetch(Integer pid) {
	    Presentation p = present_repo.findById(pid).orElseThrow(
	        () -> new UserNotFound("No Presentation is present with pid: " + pid)
	    );

	    // Convert Presentation to PresentationDTO
	    PresentationDTO presentationDTO = new PresentationDTO(
	        p.getPid(),
	        p.getCourse(),
	        p.getTopic(),
	        p.getUserTotalScore()
	    );

	    return ResponseEntity.ok(presentationDTO);
	}






	@Override
	public ResponseEntity<?> fetchall(Integer uid) {
	    // Fetch the User object from the repository
	    User u = repo.findById(uid).orElseThrow(() -> 
	        new UserNotFound("User Not Found with ID: " + uid)
	    );

	    // List to hold the PresentationDTOs
	    List<PresentationDTO> presentationDTOs = new ArrayList<>();

	    // Manually iterate through the presentations and convert them
	    for (Presentation p : u.getPresentations()) {
	        PresentationDTO dto = new PresentationDTO(
	            p.getPid(),
	            p.getCourse(),
	            p.getTopic(),
	            p.getUserTotalScore()
	        );
	        presentationDTOs.add(dto);
	    }

	    // Return the list of PresentationDTOs wrapped in a ResponseEntity
	    return ResponseEntity.ok(presentationDTOs);
	}







	@Override
	public ResponseEntity<?> changesstatus(Integer uid, Integer pid, String request)  {
	    User u = repo.findById(uid).orElseThrow(() -> 
	        new UserNotFound("User Not Found")
	    );
	    		
	    if (u.getRole().equals(Role.STUDENT)) {
	        Presentation p = present_repo.findById(pid).orElseThrow(() -> 
	            new UserNotFound("Presentation Id: " + pid + " is not associated with the given user_id: " + uid)
	        );

	        if (p.getUser().getUid() != uid) {
	            throw new UserNotFound("Presentation ID: " + pid + " does not belong to user ID: " + uid);
	        }

	        try {
	            PresentationStatus newStatus = PresentationStatus.valueOf(request.toUpperCase());
	            p.setPresentation(newStatus);
	            present_repo.save(p);

	            return ResponseEntity.ok("Presentation status updated successfully!");
	        } catch (IllegalArgumentException e) {
	            return ResponseEntity.badRequest().body("Invalid status: " + request);
	        }
	    }

	    return ResponseEntity.status(403).body("User does not have permission to update the presentation status.");
	}


	
	@Override
	public ResponseEntity<?> updatePresentationScore(Integer aid, Integer pid, UpdateRequest request) {
		//Fetch the User by uid  and validate the user is admin or not 
		Optional<User> admin = repo.findById(aid);
		if (!admin.get().getRole().equals(Role.ADMIN)) {
			return new ResponseEntity<String>("User is not ADMIN", HttpStatus.UNAUTHORIZED);
		}
//Fetch Presentation by pid
		Optional<Presentation> Optpresentation = present_repo.findById(pid);
		if(!Optpresentation.isPresent()) {
			return new ResponseEntity<String>("Presentation not found", HttpStatus.NOT_FOUND);
		}
		//convert the presentationstatus to enum
	  PresentationStatus status;
	  
	  try {
		  
		  status= PresentationStatus.valueOf(request.getPresentationStatus().toUpperCase());
		  
	  }
	  catch (IllegalArgumentException e) {
		return new ResponseEntity<>("Invalid PresentationStatus Value",HttpStatus.BAD_REQUEST);
	}
		
		//Update Presentation Entity
		Presentation presentation = Optpresentation.get();
		presentation.setUserTotalScore(request.getUserTotalScore());
		presentation.setPresentation(status);
		
		//save and update presentation
		return new ResponseEntity<Presentation>(present_repo.save(presentation), HttpStatus.OK);
	}


}


