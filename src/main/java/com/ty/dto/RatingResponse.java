package com.ty.dto;

import java.util.List;

import com.ty.Entities.Presentation;
import com.ty.Entities.Rating;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data


public class RatingResponse {
	
	private Integer rid;
	
    private Integer communication;
    
    private Integer confidence;
    
    private Integer content;
    
    private Integer interaction;
    
    private Integer liveliness;
    
    private Integer usageProps;
    
    private Double totalScore;
    
    private UserResponse user;
    
    private Presentation presentation;
    
    public RatingResponse(Rating rating) {
        this.rid = rating.getRid();
        this.communication = rating.getCommunication();
        this.confidence = rating.getConfidence();
        this.content = rating.getContent();
        this.interaction = rating.getInteraction();
        this.liveliness = rating.getLiveliness();
        this.usageProps = rating.getUsageProps();
        this.totalScore = rating.getTotalScore();
        this.user = new UserResponse(rating.getUser());
        this.presentation = rating.getPresent();
    }


	public static List<RatingResponse> fromRatings(List<Rating> ratings) {
		return ratings.stream()
                .map(RatingResponse::new)
                .toList();
	}
}


