package com.sparta.week02.models;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PersonRequestDto {
    private String name;
    private int age;
    private String job;
    private String address;

    public PersonRequestDto(String name, int age, String job, String address){
        this.name = name;
        this.age = age;
        this.job = job;
        this.address = address;
    }
}
