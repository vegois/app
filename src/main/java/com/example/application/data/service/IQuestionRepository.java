package com.example.application.data.service;

import com.example.application.data.entity.Question;
import com.example.application.data.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;


public interface IQuestionRepository extends JpaRepository<Question,Long>, JpaSpecificationExecutor<Question> {

    Question findByAuthor(User user);

    Optional<Question> findById(Long id);

    List<Question> findByCity(String city);
}
