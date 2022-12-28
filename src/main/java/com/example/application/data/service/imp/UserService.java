package com.example.application.data.service.imp;

import com.example.application.data.entity.User;
import java.util.Optional;

import com.example.application.data.service.IUserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final IUserRepository repository;

    public UserService(IUserRepository repository) {
        this.repository = repository;
    }

    public Optional<User> get(Long id) {
        return repository.findById(id);
    }

    public User update(User entity) {
        return repository.save(entity);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public Page<User> list(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Page<User> list(Pageable pageable, Specification<User> filter) {
        return repository.findAll(filter, pageable);
    }

    public int count() {
        return (int) repository.count();
    }

    public Optional<User> findById(Long id) {
        return repository.findById(id);
    }

    public User findByName(String name) {
        return repository.findByUsername(name);
    }

    public User findByUserName(String value) {
        return repository.findByUsername(value);
    }
}
