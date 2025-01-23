package com.ty.dto;

import java.util.List;

import lombok.Data;

@Data
public class UserDTO {

    private int uid;
    private String name;
    private String email;
    private List<PresentationDTO> presentations;

}
