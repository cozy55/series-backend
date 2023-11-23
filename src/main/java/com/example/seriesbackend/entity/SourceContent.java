package com.example.seriesbackend.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table
public class SourceContent {
    @EmbeddedId
    SourceContentKey id;

    @ManyToOne
    @MapsId("contentId")
    @JoinColumn(name = "content_id")
    @JsonBackReference
    Content content;

    @ManyToOne
    @MapsId("sourceId")
    @JoinColumn(name = "source_id")
    @JsonBackReference
    Source source;

    @Column
    Long sourceContentId;

    @Column
    String url;

    @Column
    String imageUrl;

    public SourceContent(Long contentId, Long sourceId, Long sourceContentId, String url, String imageUrl) {
        this.id = new SourceContentKey(contentId, sourceId);
        this.sourceContentId = sourceContentId;
        this.url = url;
        this.imageUrl = imageUrl;
    }
}
