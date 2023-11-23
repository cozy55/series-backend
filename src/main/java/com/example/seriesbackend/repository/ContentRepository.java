package com.example.seriesbackend.repository;

import com.example.seriesbackend.entity.Content;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Optional;

public interface ContentRepository extends CrudRepository<Content, Long>, PagingAndSortingRepository<Content, Long> {

    Optional<Content> findContentByTitle(String title);

    List<Content> findContentsByTitleLike(String title, Pageable pageable);

    List<Content> findAllContentByTitleContainingIgnoreCase(String title, Pageable pageable);

    List<Content> findAllContentByTitleContainingIgnoreCase(String title);
}
