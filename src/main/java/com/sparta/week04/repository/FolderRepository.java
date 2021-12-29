package com.sparta.week04.repository;

import com.sparta.week04.models.Folder;
import com.sparta.week04.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FolderRepository extends JpaRepository<Folder, Long> {
    List<Folder> findAllByUser(User user);
}