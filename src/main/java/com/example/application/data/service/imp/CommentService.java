package com.example.application.data.service.imp;

import com.example.application.data.entity.Comment;
import com.example.application.data.service.ICommentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {
    private final ICommentRepository repository;

    public CommentService(ICommentRepository repository) {
        this.repository = repository;
    }


    public void save(Comment comment) { this.repository.save(comment);
    }

    public List<Comment> findByQuestionId(Long id) {
      return repository.findCommentsByQuestionId(id);
    }
}
