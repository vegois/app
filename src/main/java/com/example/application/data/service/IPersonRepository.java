package com.example.application.data.service;

import com.example.application.data.entity.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface IPersonRepository
        extends
            JpaRepository<Player, Long>,
            JpaSpecificationExecutor<Player> {

}
