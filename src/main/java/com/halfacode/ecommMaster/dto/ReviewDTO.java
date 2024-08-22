package com.halfacode.ecommMaster.dto;



import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReviewDTO {
    private Long id;
    private UserDto user;
    private int rating;
    private String comment;
    private LocalDateTime reviewDate;
}
