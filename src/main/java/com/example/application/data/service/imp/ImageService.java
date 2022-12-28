package com.example.application.data.service.imp;

import com.example.application.data.entity.Foto;
import com.example.application.data.service.IImageRepository;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.server.StreamResource;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service
public class ImageService {

    private final IImageRepository imageRepository;


    public ImageService(IImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }


    public Image generateImage(Long id) {
        StreamResource sr = new StreamResource("user", () -> {
            Foto attached = imageRepository.findByPlayerId(id);
            return new ByteArrayInputStream(attached.getPic());
        });
        sr.setContentType("image/png");
        return new Image(sr, "profile-picture");
    }


    public Foto save(Foto foto) {
        return imageRepository.save(foto);
    }

    public void update(Foto foto) {
        imageRepository.save(foto);
    }

    public Foto findByQuestionId(Long id) {
       return imageRepository.findByReportId(id);
    }

    public List<Foto> findListByQuestionId(Long id) {
        return imageRepository.findAllByReportId(id);
    }
}
