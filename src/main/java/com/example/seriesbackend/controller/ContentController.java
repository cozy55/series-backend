package com.example.seriesbackend.controller;

import com.example.seriesbackend.dto.ContentDto;
import com.example.seriesbackend.entity.Content;
import com.example.seriesbackend.exception.RegistrationException;
import com.example.seriesbackend.security.JwtTokenProvider;
import com.example.seriesbackend.service.impl.ContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/content")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class ContentController {
    private final ContentService contentService;
    private final JwtTokenProvider jwtTokenProvider;

    @GetMapping
    public List<ContentDto> getAllContent(@RequestParam(defaultValue = "0") int page,
                                          @RequestParam(defaultValue = "10") int pageSize,
                                          @RequestParam(defaultValue = "") String search,
                                          @RequestParam(defaultValue = "") String sortBy,
                                          @RequestParam(defaultValue = "") String sortDirection,
                                          @RequestHeader(value = HttpHeaders.AUTHORIZATION, defaultValue = "") String token){
        var username = token.isEmpty()? "" :jwtTokenProvider.getUsername(jwtTokenProvider.getTokenFromBearer(token));
        return contentService.findAllLike(page, pageSize, search, sortBy, sortDirection, username);
    }

    @GetMapping("/{contentId}")
    public ResponseEntity<Content> getContentById(@PathVariable Long contentId){
        return contentService.findContentById(contentId)
                .map(content -> new ResponseEntity<>(content, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/{contentId}")
    public ResponseEntity<?> addSubscription(@PathVariable Long contentId,
                                                @RequestHeader(value = HttpHeaders.AUTHORIZATION) String token){
        contentService.addUserSubscription(contentId, jwtTokenProvider.getUsername(jwtTokenProvider.getTokenFromBearer(token)));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{contentId}")
    public ResponseEntity<?> deleteSubscription(@PathVariable Long contentId,
                                                @RequestHeader(value = HttpHeaders.AUTHORIZATION) String token){
        contentService.deleteUserSubscription(contentId, jwtTokenProvider.getUsername(jwtTokenProvider.getTokenFromBearer(token)));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/subscriptions")
    public ResponseEntity<List<ContentDto>> getSubscriptions(@RequestHeader(value = HttpHeaders.AUTHORIZATION) String token){
        var subscriptions = contentService.getSubscription(jwtTokenProvider.getUsername(jwtTokenProvider.getTokenFromBearer(token)));
        return new ResponseEntity<>(subscriptions, HttpStatus.OK);
    }
}
