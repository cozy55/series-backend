package com.example.seriesbackend.repository;

import com.example.seriesbackend.entity.SourceContent;
import com.example.seriesbackend.entity.SourceContentKey;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface SourceContentRepository extends CrudRepository<SourceContent, SourceContentKey> {
}
