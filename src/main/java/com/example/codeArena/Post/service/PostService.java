package com.example.codeArena.Post.service;

import com.example.codeArena.Post.dto.PostCreateDto;
import com.example.codeArena.Post.model.Post;
import com.example.codeArena.Post.repository.PostRepository;
import com.example.codeArena.exception.ResourceNotFoundException;
import com.example.codeArena.exception.UnauthorizedAccessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class PostService {

    private final Logger logger = LoggerFactory.getLogger(PostService.class);
    private final PostRepository postRepository;

    @Autowired
    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

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
    public Optional<Post> updatePost(String postId, String currentUserId, PostCreateDto updateDto) {
        return postRepository.findById(postId).map(post -> {
            if (!post.getAuthorId().equals(currentUserId)) {
                throw new UnauthorizedAccessException("이 게시글을 수정할 권한이 없습니다.");
            }
            post.setTitle(updateDto.getTitle());
            post.setContent(updateDto.getContent());
            post.setTags(updateDto.getTags());
            post.setUpdatedAt(new Date());
            return postRepository.save(post);
        });
    }

    // 게시글 삭제
    public void deletePost(String postId, String currentUserId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("해당 ID의 게시글을 찾을 수 없습니다: " + postId));

        if (!post.getAuthorId().equals(currentUserId)) {
            throw new UnauthorizedAccessException("이 게시글을 삭제할 권한이 없습니다.");
        }

        postRepository.deleteById(postId);
    }

    // 특정 사용자가 작성한 게시글 조회
    public List<Post> getPostsByAuthor(String authorId) {
        return postRepository.findByAuthorId(authorId);
    }

    // 제목으로 게시글 검색
    public List<Post> getPostsByTitle(String title) {
        return postRepository.findByTitleContaining(title);
    }

    // 태그로 게시글 검색
    public List<Post> getPostsByTag(String tag) {
        return postRepository.findByTagsContaining(tag);
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
            if (image != null && !image.isEmpty()) {
                try {
                    post.addImage(image.getBytes());
                } catch (IOException e) {
                    logger.error("Image upload failed", e);
                }
            }
            return postRepository.save(post);
        });
    }
}
