package com.ty.Service;

import org.springframework.http.ResponseEntity;

import com.ty.Entities.User;
import com.ty.dto.UserLogin;
import com.ty.dto.UserStatusReq;


public interface UserService {
	
	ResponseEntity<?> register (User user);
	
//	ResponseEntity<?> login(User users);
//
	ResponseEntity<?> login(UserLogin userlogin);



	ResponseEntity<?> getUsers(User user);

//ResponseEntity<?> findByrole(UserRole userrole);
public ResponseEntity<?> getAllUsersForAdmin() ;

public ResponseEntity<?> updateStatus(UserStatusReq user);




}
