package com.example.application.data.service.imp;

import com.example.application.data.entity.Player;
import java.util.Optional;

import com.example.application.data.service.IPersonRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class PersonService {

    private final IPersonRepository repository;

    public PersonService(IPersonRepository repository) {
        this.repository = repository;
    }

    public Optional<Player> get(Long id) {
        return repository.findById(id);
    }

    public Player update(Player entity) {
        return repository.save(entity);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public Page<Player> list(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Page<Player> list(Pageable pageable, Specification<Player> filter) {
        return repository.findAll(filter, pageable);
    }

    public int count() {
        return (int) repository.count();
    }


}
