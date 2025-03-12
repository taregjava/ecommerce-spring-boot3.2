package com.halfacode.ecommMaster.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
@Data
@Builder
@AllArgsConstructor
public class ReviewResponseDto {
    private Long id;
    private int rating;
    private String comment;
    private LocalDateTime reviewDate;
}
