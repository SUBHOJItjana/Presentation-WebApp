//package com.ty.Service;

import java.util.List;
import java.util.stream.Collectors;

import com.ty.Entities.User;
import com.ty.dto.PresentationDTO;
import com.ty.dto.UserDTO;
@SuppressWarnings("all")
public UserDTO convertToDTO(User user) {
	
	try {
    UserDTO userDTO = new UserDTO();
    userDTO.setUid(user.getUid());
    userDTO.setName(user.getName());
    userDTO.setEmail(user.getEmail());

    List<PresentationDTO> presentationDTOs = user.getPresentations().stream()
        .map(p -> new PresentationDTO(p.getPid(), p.getCourse(), p.getTopic(), p.getUserTotalScore()))
        .collect(Collectors.toList());

    userDTO.setPresentations(presentationDTOs);
    return userDTO;
	}
	catch (Exception e) {
		System.out.println("Something Went Wrong");
	}
}

