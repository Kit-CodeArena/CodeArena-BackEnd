package com.example.codeArena.Post.service;

import com.example.codeArena.Post.dto.PostCreateDto;
import com.example.codeArena.Post.model.Post;
import com.example.codeArena.Post.repository.PostRepository;
import com.example.codeArena.exception.CustomException;
import com.example.codeArena.exception.CustomException.ErrorCode;
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
        Post post = new Post(createDto.getTitle(), createDto.getContent(), createDto.getAuthorId(), createDto.getAuthorNickname(), createDto.getTags());
        if (image != null && !image.isEmpty()) {
            post.addImage(image.getBytes());
        }
        return postRepository.save(post);
    }

    // 게시글 조회
    public Optional<Post> getPostById(Long postId) {
        return postRepository.findById(postId);
    }

    // 게시글 전체 조회
    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    // 게시글 수정
    public Optional<Post> updatePost(Long postId, Long currentUserId, PostCreateDto updateDto, MultipartFile image) throws IOException {
        return postRepository.findById(postId).map(post -> {
            if (!post.getAuthorId().equals(currentUserId)) {
                throw new CustomException(CustomException.ErrorCode.ACCESS_DENIED);
            }
            post.setTitle(updateDto.getTitle());
            post.setContent(updateDto.getContent());
            post.setTags(updateDto.getTags());
            post.setUpdatedAt(new Date());
            if (image != null && !image.isEmpty()) {
                try {
                    post.addImage(image.getBytes());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            return postRepository.save(post);
        });
    }

    // 게시글 삭제
    public void deletePost(Long postId, Long currentUserId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

        if (!post.getAuthorId().equals(currentUserId)) {
            throw new CustomException(ErrorCode.ACCESS_DENIED);
        }

        postRepository.deleteById(postId);
    }

    // 특정 사용자가 작성한 게시글 조회
    public List<Post> getPostsByAuthor(Long authorId) {
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
    public Optional<Post> incrementLikes(Long postId) {
        return postRepository.findById(postId).map(post -> {
            post.incrementLikes();
            return postRepository.save(post);
        });
    }

    // 게시글 조회수 증가
    public Optional<Post> incrementViews(Long postId) {
        return postRepository.findById(postId).map(post -> {
            post.incrementViews();
            return postRepository.save(post);
        });
    }

    // 댓글 ID 추가
    public Optional<Post> addCommentToPost(Long postId, Long commentId) {
        return postRepository.findById(postId).map(post -> {
            post.addComment(commentId);
            return postRepository.save(post);
        });
    }


    // 이미지 업로드
    public Optional<Post> uploadImageToPost(Long postId, MultipartFile image) throws IOException {
        return postRepository.findById(postId).map(post -> {
            try {
                if (image != null && !image.isEmpty()) {
                    post.addImage(image.getBytes());
                }
            } catch (IOException e) {
                logger.error("게시글 ID " + postId + "에 대한 이미지 업로드 중 오류 발생", e);
                throw new CustomException(ErrorCode.IMAGE_PROCESSING_FAILED);
            }
            return postRepository.save(post);
        });
    }
}
