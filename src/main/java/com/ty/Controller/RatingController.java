package com.ty.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ty.Service.RatingService;
import com.ty.dto.RatingDto;

@RestController
@RequestMapping("/Rating")
public class RatingController {
	
	@Autowired
	private RatingService ratingservice;

	
	@PostMapping("/rate")
	
	public ResponseEntity<?> rate(@RequestParam Integer aid , Integer uid, Integer pid,  @RequestBody RatingDto rating){
		return ratingservice.AddRating(aid,uid,pid,rating);
		
	}
	@JsonIgnore
	@GetMapping("/get")
	public ResponseEntity<?> getbypid(@RequestParam  Integer pid){
		return ratingservice.getbyid(pid);
	}
	

	@GetMapping("/getAll")
	public ResponseEntity<?> getAllRatingOfStudent(@RequestParam Integer uid) {
		return ratingservice.getAllRatingOfStudent(uid);
	}
	
}
