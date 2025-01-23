package com.ty.Entities;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ty.Enum.PresentationStatus;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Data;

@Entity
@Data
public class Presentation {
	@Id

	
	private int pid;
	
	@ManyToOne
	@JoinColumn(name = "user_id", nullable = false)
	@JsonBackReference
	private User user;



	
	@OneToMany(mappedBy = "present")
	@JsonIgnore
	private List<Rating> ratings;
	
	
	private String course;
	

	private String topic;
	
	@Enumerated(EnumType.STRING)
	private PresentationStatus presentation;
	
	private double userTotalScore;


	

}
