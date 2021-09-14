package com.sparta.week02.service;

import com.sparta.week02.domain.Person;
import com.sparta.week02.domain.PersonRepository;
import com.sparta.week02.models.PersonRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PersonService {
    private final PersonRepository personRepository;

    public List<Person> getAllContent() {
        return personRepository.findAll();
    }

    public Person getContentById(Long id){
        Person person = personRepository.findById(id).orElseThrow(
            () -> new IllegalArgumentException("해당 아이디가 존재하지 않습니다.")
        );
        return person;
    }

    @Transactional
    public Person createContent(PersonRequestDto requestDto){
        Person person = new Person(requestDto);
        personRepository.save(person);
        return person;
    }

    @Transactional
    public Long updateContent(Long id, PersonRequestDto requestDto){
        Person person = personRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("해당 아이디가 존재하지 않습니다.")
        );
        person.update(requestDto);
        return person.getId();
    }

    @Transactional
    public Long deleteContent(Long id){
        Person person = personRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("해당 아이디가 존재하지 않습니다.")
        );
        personRepository.deleteById(id);
        return id;
    }

}
