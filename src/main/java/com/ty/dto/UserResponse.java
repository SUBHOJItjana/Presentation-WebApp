package com.ty.dto;

import com.ty.Entities.User;
import com.ty.Enum.Role;
import com.ty.Enum.Status;

import lombok.Data;

@Data
public class UserResponse {

	
	private Integer uid;
	
    private String name;
    
    private String email;
    
    private Long phone;
    
    private Status status;
    
    private Role role;
    
    public UserResponse(User user) {
        this.uid = user.getUid();
        this.name = user.getName();
        this.email = user.getEmail();
        this.phone = user.getUphone();
        this.status = user.getStatus();
        this.role = user.getRole();
    }
}
