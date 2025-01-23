package com.ty.dto;

import lombok.Data;

@Data
public class RatingDto {

	private int communication;
	private int confidence;
	private int content ;
	private int interaction;
	private int liveliness;
	private int usageProps;
	
	private double totalScore;
}
