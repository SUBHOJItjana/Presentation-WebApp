package com.ty.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
@NoArgsConstructor
@Data
public class PresentationDTO {

    private int pid;
    private String course;
    private String topic;
    private double userTotalScore;

    // Correct Constructor
    public PresentationDTO(int pid, String course, String topic, double userTotalScore) {
        this.pid = pid;
        this.course = course;
        this.topic = topic;
        this.userTotalScore = userTotalScore;
    }
}
