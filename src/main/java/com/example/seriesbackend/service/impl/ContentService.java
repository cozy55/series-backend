package com.example.seriesbackend.service.impl;

import com.example.seriesbackend.dto.ContentDto;
import com.example.seriesbackend.entity.Content;
import com.example.seriesbackend.entity.User;
import com.example.seriesbackend.repository.ContentRepository;
import com.example.seriesbackend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
public class ContentService {
    private final ContentRepository contentRepository;
    private final UserRepository userRepository;

    public List<Content> findAll(){
        return StreamSupport.stream(contentRepository.findAll().spliterator(), false).toList();
    }

    private PageRequest getPageRequest(int page, int pageSize, String sortBy, String sortDirection){
        if(sortBy.isEmpty()){
            return PageRequest.of(page, pageSize);
        }
        var sort = Sort.by(sortBy).ascending();
        if(sortDirection.equalsIgnoreCase("ASC")){
            sort = sort.ascending();
        } else if(sortDirection.equalsIgnoreCase("DESC")) {
            sort = sort.descending();
        }
        return PageRequest.of(page, pageSize, sort);
    }

    public List<ContentDto> findAllLike(int page, int pageSize, String search, String sortBy, String sortDirection, String username){
        var a = contentRepository.findAllContentByTitleContainingIgnoreCase(search, getPageRequest(page, pageSize, sortBy, sortDirection));
        System.out.println(a.size());
        return contentRepository.findAllContentByTitleContainingIgnoreCase(search, getPageRequest(page, pageSize, sortBy, sortDirection)).stream().map(content -> {
            var contentDto = contentMapper(content);
            if(content.getSubscribedUsers().stream().anyMatch(user -> user.getUsername().equals(username) ||  user.getEmail().equals(username))){
                contentDto.setSubscribed(true);
            }
            return contentDto;
        }).toList();
    }

    public Optional<Content> findContentById(Long contentId){
        return contentRepository.findById(contentId);
    }

    public Optional<Content> findContentByTitle(String title){
        return contentRepository.findContentByTitle(title);
    }

    public void addUserSubscription(Long id, String username){
        this.findContentById(id)
                .ifPresent(content -> {
                    userRepository.findByUsernameOrEmail(username, username)
                            .ifPresent(user -> {
                                content.getSubscribedUsers().add(user);
                                contentRepository.save(content);
                            });
                });
    }

    public void deleteUserSubscription(Long id, String username){
        this.findContentById(id)
                .ifPresent(content -> {
                    userRepository.findByUsernameOrEmail(username, username)
                            .ifPresent(user -> {
                                if(content.getSubscribedUsers().contains(user)){
                                    content.getSubscribedUsers().remove(user);
                                    contentRepository.save(content);
                                }
                            });
                });
    }

    public List<ContentDto> getSubscription(String username){
        return userRepository.findByUsernameOrEmail(username, username).map(User::getSubscribedContents).orElseGet(List::of)
                .stream().map(subscribedContent -> {
                    var subscribedContentDto = contentMapper(subscribedContent);
                    subscribedContentDto.setSubscribed(true);
                    return subscribedContentDto;
                }).toList();
    }

    private ContentDto contentMapper(Content content){
        var id = content.getId();
        var title = content.getTitle();
        var imageUrl = content.getImageUrl();
        var sourceContents = content.getSourceContents().stream().map(sourceContent -> new ContentDto.SourceContentDto(sourceContent.getUrl(), sourceContent.getImageUrl(), sourceContent.getSource().getName()))
                .toList();
        return new ContentDto(id, title, imageUrl, sourceContents);
    }
}
