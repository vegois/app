package com.example.application.data.service.imp;

import com.example.application.data.entity.Question;
import com.example.application.data.service.IQuestionRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class QuestionService {
    private final IQuestionRepository repository;

    public QuestionService(IQuestionRepository repository) {
        this.repository = repository;
    }

    public List<Question> findAllByTopicSince(String topicId, Instant since) {
        return repository.findAll();
    }

    public void save(Question questionEntity) {
        repository.save(questionEntity);
    }

    public List<Question> findAll() {
        return repository.findAll();
    }

    public Question findById(Long id) {
        return repository.findById(id).orElse(null);
    }

    public Question update(Question bean) {
        return repository.save(bean);
    }

    public List<Question> findALlByCity(String city) {
        return repository.findByCity(city);
    }
}
