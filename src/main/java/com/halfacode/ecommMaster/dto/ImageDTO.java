package com.halfacode.ecommMaster.dto;

import lombok.Data;

@Data
public class ImageDTO {
    private Long id;
    private String url;
    private String name;
    private boolean isMain;
}
