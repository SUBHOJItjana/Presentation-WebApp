package com.ty.dto;

import com.ty.Entities.Presentation;
import com.ty.Enum.Role;
import com.ty.Enum.Status;

import lombok.Data;

@Data
public class UserRequest { 
	private Integer uid;
	private String name;
	private String email;
	private String password;
	private long uphoneno;
	private Presentation presentation;
	private Status status;
	private Role role;
	private double userTotalScore;

}
