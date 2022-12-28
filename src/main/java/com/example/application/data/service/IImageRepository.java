package com.example.application.data.service;

import com.example.application.data.entity.Foto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface IImageRepository extends JpaRepository<Foto, Long>, JpaSpecificationExecutor<Foto> {

    Foto findByPlayerId(Long id);

    Foto findByReportId(Long id);

    List<Foto> findAllByReportId(Long id);
}
