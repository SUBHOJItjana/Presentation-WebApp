package com.ty.Entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.ty.Enum.PresentationStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data

public class Rating {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "rid", nullable = false, updatable = false)
	private int rid;
	
	private int communication;
	private int confidence;
	private int content ;
	private int interaction;
	private int liveliness;
	private int usageProps;
	
    @Column(name = "total_score", nullable = false)
    private Double totalScore;
	
	
	@ManyToOne
	@JoinColumn(name="user_id")
	@JsonBackReference
	private User user;
	
	@ManyToOne
	@JoinColumn(name="presentation_id")
	private Presentation present;
	
	@Enumerated(EnumType.STRING)
	private PresentationStatus presentation;
	

}
