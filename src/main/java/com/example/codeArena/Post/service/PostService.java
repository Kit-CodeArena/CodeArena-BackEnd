package com.example.codeArena.Post.service;

import com.example.codeArena.Post.domain.Comment;
import com.example.codeArena.Post.dto.CommentResponseDto;
import com.example.codeArena.Post.dto.PostCreateDto;
import com.example.codeArena.Post.domain.Post;
import com.example.codeArena.Post.dto.PostResponseDto;
import com.example.codeArena.Post.repository.CommentRepository;
import com.example.codeArena.Post.repository.PostRepository;
import com.example.codeArena.User.domain.User;
import com.example.codeArena.User.repository.UserRepository;
import com.example.codeArena.exception.CustomException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PostService {

    private final Logger logger = LoggerFactory.getLogger(PostService.class);
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

    @Autowired
    public PostService(PostRepository postRepository, UserRepository userRepository, CommentRepository commentRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.commentRepository = commentRepository;
    }

    // 게시글 생성
    public Post createPost(PostCreateDto createDto, MultipartFile image) throws IOException {
        User author = userRepository.findById(createDto.getAuthorId())
                .orElseThrow(() -> new CustomException(CustomException.ErrorCode.USER_NOT_FOUND));

        Post post = new Post(createDto.getTitle(), createDto.getContent(), author, createDto.getTags());
        if (image != null && !image.isEmpty()) {
            post.addImage(image.getBytes());
        }
        return postRepository.save(post);
    }

    // 게시글 조회
    public PostResponseDto getPostById(Long postId, Long currentUserId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(CustomException.ErrorCode.POST_NOT_FOUND));

        // 현재 사용자가 좋아요를 눌렀는지 확인
        User currentUser = userRepository.findById(currentUserId)
                .orElseThrow(() -> new CustomException(CustomException.ErrorCode.USER_NOT_FOUND));
        boolean isLikedByCurrentUser = post.getLikedUsers().contains(currentUser);

        return convertToDto(post, isLikedByCurrentUser);
    }

    // 게시글 전체 조회
    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    // 게시글 수정
    public Optional<Post> updatePost(Long postId, Long currentUserId, PostCreateDto updateDto, MultipartFile image) throws IOException {
        return postRepository.findById(postId).map(post -> {
            if (!post.getAuthor().getId().equals(currentUserId)) {
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
                .orElseThrow(() -> new CustomException(CustomException.ErrorCode.POST_NOT_FOUND));

        if (!post.getAuthor().getId().equals(currentUserId)) {
            throw new CustomException(CustomException.ErrorCode.ACCESS_DENIED);
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

    // 게시글에 좋아요 추가/제거
    @Transactional
    public PostResponseDto toggleLike(Long postId, Long userId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(CustomException.ErrorCode.POST_NOT_FOUND));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(CustomException.ErrorCode.USER_NOT_FOUND));

        boolean liked = post.getLikedUsers().contains(user);
        if (liked) {
            post.removeLike(user);
        } else {
            post.addLike(user);
        }

        // 좋아요 관련 변경 사항을 데이터베이스에 반영하기 위해 저장
        Post updatedPost = postRepository.save(post);

        // 좋아요 상태를 반환하는 부분을 수정
        boolean isLikedByCurrentUser = updatedPost.getLikedUsers().contains(user);

        // 변화된 게시글 정보를 DTO로 변환하여 반환
        return convertToDto(updatedPost, isLikedByCurrentUser);
    }

    // 게시글 조회수 증가
    public Optional<Post> incrementViews(Long postId) {
        return postRepository.findById(postId).map(post -> {
            post.incrementViews();
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
                throw new CustomException(CustomException.ErrorCode.IMAGE_PROCESSING_FAILED);
            }
            return postRepository.save(post);
        });
    }

    public Optional<Post> addCommentToPost(Long postId, Long commentId) {
        return postRepository.findById(postId).map(post -> {
            Comment comment = commentRepository.findById(commentId)
                    .orElseThrow(() -> new CustomException(CustomException.ErrorCode.COMMENT_NOT_FOUND));

            post.addComment(comment);
            return postRepository.save(post);
        });
    }

    // Post를 PostResponseDto로 변환
    public PostResponseDto convertToDto(Post post, boolean isLikedByCurrentUser) {
        PostResponseDto dto = new PostResponseDto();
        dto.setId(post.getId());
        dto.setTitle(post.getTitle());
        dto.setContent(post.getContent());
        dto.setAuthorNickname(post.getAuthor().getNickname());
        dto.setCreatedAt(post.getCreatedAt());
        dto.setUpdatedAt(post.getUpdatedAt());
        dto.setTags(post.getTags());
        dto.setViews(post.getViews());
        dto.setLikes(post.getLikedUsers().size()); // 좋아요 수 설정
        dto.setIsLikedByCurrentUser(isLikedByCurrentUser); // 현재 사용자의 좋아요 상태 설정
        // 댓글 목록 변환
        List<CommentResponseDto> commentDtos = post.getComments().stream()
                .map(comment -> new CommentResponseDto(comment.getId(), comment.getAuthorNickname(), comment.getContent(),comment.getCreatedAt()))
                .collect(Collectors.toList());

        dto.setComments(commentDtos); // 댓글 목록 설정
        dto.setCommentCount(commentDtos.size()); // 댓글 수 설정
        return dto;
    }

    public List<PostResponseDto> convertToDtoList(List<Post> posts) {
        return posts.stream().map(post -> {
            // 각 게시글을 PostResponseDto로 변환
            return convertToDto(post, false); // 'false'는 사용자의 좋아요 상태를 나타냅니다. 필요에 따라 수정할 수 있습니다.
        }).collect(Collectors.toList());
    }
}