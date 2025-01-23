package com.ty.dto;

import com.ty.Enum.Status;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Data
@Setter
@Getter
@NoArgsConstructor
public class UserStatusReq {

    private Integer adminUid;

    private Integer uid;

  
    private Status status;
}

