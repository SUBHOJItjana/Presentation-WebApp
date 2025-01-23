package com.ty.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.ty.Entities.User;
import com.ty.Enum.Role;
import com.ty.Enum.Status;
import com.ty.Exceptions.UserNotFound;
import com.ty.Repository.UserRepository;
import com.ty.Response.ResponseStructure;
import com.ty.dto.UserLogin;
import com.ty.dto.UserRequest;
import com.ty.dto.UserStatusReq;

@Service
public class UserServiceImp implements UserService {

	@Autowired
	private UserRepository userrepository;

	/*
	 * If User is already register with particular email id it will throw message
	 * already register if not then it will register
	 */
	@Override
	public ResponseEntity<?> register(User user) {
		Optional<User> opt = userrepository.findByEmail(user.getEmail());

		if (opt.isPresent()) {
			ResponseStructure<String> rs = new ResponseStructure<>();
			rs.setMessage("Already Registered");
			rs.setData(user.getEmail());
			rs.setStatusCode(HttpStatus.BAD_REQUEST.value());
			return new ResponseEntity<ResponseStructure<String>>(rs, HttpStatus.OK);

		} else {
			User save = userrepository.save(user);
			UserRequest dto = new UserRequest();
			BeanUtils.copyProperties(save, dto);

			ResponseStructure<UserRequest> rs = new ResponseStructure<>();

			rs.setMessage("Registered Successfully");
			rs.setData(dto);
			rs.setStatusCode(HttpStatus.ACCEPTED.value());
			return new ResponseEntity<ResponseStructure<UserRequest>>(rs, HttpStatus.OK);
		}

	}

	/*
	 * Check the email id and password if correct then login
	 */
	@Override
	public ResponseEntity<?> login(UserLogin userlogin) {
		User users = userrepository.findByEmail(userlogin.getEmail())
				.orElseThrow(() -> new UserNotFound("User Not Found In Records"));
		ResponseStructure<String> rs = new ResponseStructure<>();

		if (userlogin.getPassword().equals(users.getPassword())) {
			rs.setMessage("User Login Successfully");
			rs.setStatusCode(HttpStatus.OK.value());

			return new ResponseEntity<ResponseStructure<String>>(rs, HttpStatus.OK);

		}
		rs.setStatusCode(HttpStatus.BAD_REQUEST.value());
		rs.setMessage("Invalid Password");
		return new ResponseEntity<ResponseStructure<String>>(rs, HttpStatus.OK);

	}

	/*
	 * Fetch and return user if exist based id else throw exception
	 */

	@Override
	public ResponseEntity<ResponseStructure<?>> getUsers(User user) {
		// Fetch the user by ID or throw an exception if not found
		User users = userrepository.findById(user.getUid()).orElseThrow(() -> new UserNotFound("User doesn't exist"));

		ResponseStructure<Object> rs = new ResponseStructure<>();

		// Check if the user is active
		if (users.getStatus() != Status.ACTIVE) {
			rs.setData(users.getStatus());
			rs.setMessage("User is inactive");
			rs.setStatusCode(HttpStatus.BAD_REQUEST.value());
			return new ResponseEntity<>(rs, HttpStatus.BAD_REQUEST);
		}

		// Map the user entity to a DTO
		UserRequest dto = new UserRequest();
		BeanUtils.copyProperties(users, dto);

		// Populate response structure for successful fetch
		rs.setData(dto);
		rs.setStatusCode(HttpStatus.OK.value());
		rs.setMessage("User fetched successfully");

		return new ResponseEntity<>(rs, HttpStatus.OK);
	}

	@Override

	public ResponseEntity<?> getAllUsersForAdmin() {
		// Fetch the user with the role ADMIN
		List<User> admins = userrepository.findUsersByRole(Role.ADMIN);

		if (admins.isEmpty()) {
			// Return 403 if no ADMIN user exists
			ResponseStructure<String> response = new ResponseStructure<>();
			response.setMessage("Access denied. Only ADMIN can fetch all users.");
			response.setStatusCode(HttpStatus.FORBIDDEN.value());
			response.setData(null);
			return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
		}

		// Fetch all users
		List<User> allUsers = userrepository.findAll();
		ResponseStructure<List<User>> response = new ResponseStructure<>();
		response.setMessage("All users fetched successfully.");
		response.setStatusCode(HttpStatus.OK.value());
		response.setData(allUsers);

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	// Update the status of the user
	@Override
	public ResponseEntity<?> updateStatus(UserStatusReq user) {
		// Verify admin user existence and role
		User admin = userrepository.findById(user.getAdminUid())
				.orElseThrow(() -> new UserNotFound("Admin with ID " + user.getAdminUid() + " doesn't exist"));

		if (admin.getRole() != Role.ADMIN) {
			// Admin role check
			ResponseStructure<?> response = new ResponseStructure<>();
			response.setMessage("Only an admin can update the status of other users");
			response.setData(null);
			response.setStatusCode(HttpStatus.FORBIDDEN.value());
			return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
		}

		// Find the target user
		User targetUser = userrepository.findById(user.getUid())
				.orElseThrow(() -> new UserNotFound("User with ID " + user.getUid() + " doesn't exist"));

		// Update status
		targetUser.setStatus(user.getStatus());
		userrepository.save(targetUser);

		// Response
		ResponseStructure<User> response = new ResponseStructure<>();
		response.setMessage("Admin ID: " + admin.getUid() + " updated User ID: " + targetUser.getUid() + " status to: "
				+ targetUser.getStatus());
		response.setData(targetUser);
		response.setStatusCode(HttpStatus.ACCEPTED.value());

		return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
	}

}
