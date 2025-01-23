package com.ty.Service;

import org.springframework.http.ResponseEntity;

import com.ty.Entities.Presentation;
import com.ty.dto.UpdateRequest;

public interface PresentationService {


	public ResponseEntity<?> add(Integer adminId, Integer userId, Presentation presentation);

	public ResponseEntity<?> fetch(Integer pid);

	public ResponseEntity<?> fetchall(Integer uid);

	public ResponseEntity<?> changesstatus(Integer uid, Integer pid, String request);


	public ResponseEntity<?> updatePresentationScore(Integer aid, Integer pid, UpdateRequest request);

	

}
