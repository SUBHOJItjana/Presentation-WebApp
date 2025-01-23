package com.ty.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ty.Entities.User;
import com.ty.Response.ResponseStructure;
import com.ty.Service.UserService;
import com.ty.dto.UserLogin;
import com.ty.dto.UserStatusReq;

@RestController
@RequestMapping("/User")
public class UserController {
	
	@Autowired
	private UserService userservice;
	
	@PostMapping("Register")
	
	public ResponseEntity<?> register(@RequestBody User users) {
		return userservice.register(users);
		
	}
	
	@PostMapping("Login")
	public ResponseEntity<?> login(@RequestBody UserLogin users){
	
		return userservice.login(users);
	}
	
	// User can get details by user_id if they are active only
	@GetMapping("/GetDetails")
	public ResponseEntity<?>getdetails(@RequestBody User id) {
		return userservice.getUsers(id);
	}
	

	@GetMapping("/Admin/allStudents")
	public ResponseEntity<?> getAllUsers() {
	    return userservice.getAllUsersForAdmin(); //Admin will get the list of users
	}
	
	
	
	@PutMapping("/updateStatus")
	public ResponseEntity<?> updateStatus(@RequestBody UserStatusReq request) {
	    // Validate request
	    if (request.getAdminUid() == null || request.getUid() == null || request.getStatus() == null) {
	        ResponseStructure<?> response = new ResponseStructure<>();
	        response.setMessage("Admin ID, Target User ID, and Status must not be null");
	        response.setData(null);
	        response.setStatusCode(HttpStatus.BAD_REQUEST.value());
	        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	    }

	  
	    return userservice.updateStatus(request);
	}



}
