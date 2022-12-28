package com.example.application.data.service;

import com.example.application.data.entity.Likes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ILikesRepository extends JpaRepository<Likes, Long>, JpaSpecificationExecutor<Likes> {

    Likes findByUsernameAndReportId(String username, Long reportId);
}
