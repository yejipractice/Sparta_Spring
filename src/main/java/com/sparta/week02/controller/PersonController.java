package com.sparta.week02.controller;

import com.sparta.week02.domain.Person;
import com.sparta.week02.domain.PersonRepository;
import com.sparta.week02.models.PersonRequestDto;
import com.sparta.week02.service.PersonService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class PersonController {
    private final PersonRepository personRepository;
    private final PersonService personService;

    @GetMapping("/api/people")
    public List<Person> getPeople() {
        return personService.getAllContent();
    }

    @GetMapping("/api/people/{id}")
    public Person getPerson(@PathVariable Long id){
        return personService.getContentById(id);
    }

    @PostMapping("/api/people")
    public Person createPerson(@RequestBody PersonRequestDto requestDto){
        return personService.createContent(requestDto);
    }

    @PutMapping("/api/people/{id}")
    public Long updatePerson(@PathVariable Long id, @RequestBody PersonRequestDto requestDto){
        return personService.updateContent(id, requestDto);
    }

    @DeleteMapping("/api/people/{id}")
    public Long deletePerson(@PathVariable Long id){
        return personService.deleteContent(id);
    }


}
