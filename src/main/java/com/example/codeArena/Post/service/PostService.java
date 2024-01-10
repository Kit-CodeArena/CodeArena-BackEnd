package com.example.codeArena.Post.service;

import com.example.codeArena.Post.dto.PostCreateDto;
import com.example.codeArena.exception.ResourceNotFoundException;
import com.example.codeArena.exception.UnauthorizedAccessException;
import com.example.codeArena.Post.model.Post;
import com.example.codeArena.Post.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    // 게시글 생성
    public Post createPost(PostCreateDto createDto, MultipartFile image) throws IOException {
        Post post = new Post();
        post.setTitle(createDto.getTitle());
        post.setContent(createDto.getContent());
        post.setAuthorId(createDto.getAuthorId());
        post.setTags(createDto.getTags());
        post.setCreatedAt(new Date());
        post.setUpdatedAt(new Date());
        if (image != null && !image.isEmpty()) {
            post.addImage(image.getBytes());
        }
        return postRepository.save(post);
    }

    // 게시글 조회
    public Optional<Post> getPostById(String postId) {
        return postRepository.findById(postId);
    }

    // 게시글 전체 조회
    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    // 게시글 수정
    public Post updatePost(String postId, String currentUserId, PostCreateDto updateDto) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with id: " + postId));

        if (!post.getAuthorId().equals(currentUserId)) {
            throw new UnauthorizedAccessException("You do not have permission to modify this post.");
        }

        post.setTitle(updateDto.getTitle());
        post.setContent(updateDto.getContent());
        post.setTags(updateDto.getTags());
        post.setUpdatedAt(new Date());
        return postRepository.save(post);
    }

    // 게시글 삭제
    public void deletePost(String postId, String currentUserId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with id: " + postId));

        if (!post.getAuthorId().equals(currentUserId)) {
            throw new UnauthorizedAccessException("You do not have permission to delete this post.");
        }

        postRepository.deleteById(postId);
    }

    // 게시글에 좋아요 추가
    public Optional<Post> incrementLikes(String postId) {
        return postRepository.findById(postId).map(post -> {
            post.incrementLikes();
            return postRepository.save(post);
        });
    }

    // 게시글 조회수 증가
    public Optional<Post> incrementViews(String postId) {
        return postRepository.findById(postId).map(post -> {
            post.incrementViews();
            return postRepository.save(post);
        });
    }

    // 댓글 ID 추가
    public Optional<Post> addCommentToPost(String postId, String commentId) {
        return postRepository.findById(postId).map(post -> {
            post.addComment(commentId);
            return postRepository.save(post);
        });
    }

    // 이미지 업로드
    public Optional<Post> uploadImageToPost(String postId, MultipartFile image) throws IOException {
        return postRepository.findById(postId).map(post -> {
            try {
                post.addImage(image.getBytes());
            } catch (IOException e) {
                e.printStackTrace(); // 실제 애플리케이션에서는 적절한 예외 처리 필요
            }
            return postRepository.save(post);
        });
    }

    // 기타 필요한 메소드 추가...
}