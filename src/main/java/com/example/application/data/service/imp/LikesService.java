package com.example.application.data.service.imp;

import com.example.application.data.entity.Likes;
import com.example.application.data.service.ILikesRepository;
import org.springframework.stereotype.Service;

@Service
public class LikesService {

    private final ILikesRepository likesRepository;

    public LikesService(ILikesRepository likesRepository) {
        this.likesRepository = likesRepository;
    }

    public Likes findByUsernameAndReportId(String username, Long reportId){
        return likesRepository.findByUsernameAndReportId(username,reportId);
    }

    public void delete(Likes like) {
        likesRepository.delete(like);
    }

    public Likes save(Likes like) {
        return likesRepository.save(like);
    }
}
