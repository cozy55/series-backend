package com.example.seriesbackend.repository;

import com.example.seriesbackend.entity.Source;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface SourceRepository extends CrudRepository<Source, Long> {
    Optional<Source> findSourceByName(Source.SourceType name);
}
