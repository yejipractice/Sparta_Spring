package com.sparta.week04.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ProductRequestDto {
    private String title;
    private String link;
    private String image;
    private int lprice;
}