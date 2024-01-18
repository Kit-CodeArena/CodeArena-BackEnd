package com.example.codeArena.Post.domain;

import com.example.codeArena.User.domain.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "posts")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private String title;
    private String content;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;

    @ElementCollection
    private Set<String> tags = new HashSet<>();

    private int views;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.EAGER) // FetchType.EAGER로 변경
    @JoinColumn(name = "author_id", referencedColumnName = "id")
    private User author;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    @Lob
    @Column(name = "image", columnDefinition="LONGBLOB")
    private byte[] image;

    @ManyToMany
    @JoinTable(
            name = "post_liked_users",
            joinColumns = @JoinColumn(name = "post_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    @JsonIgnore
    private Set<User> likedUsers = new HashSet<>();

    public Post(String title, String content, User author, Set<String> tags) {
        this.title = title;
        this.content = content;
        this.author = author;
        this.tags = tags;
        this.createdAt = new Date();
        this.updatedAt = this.createdAt;
        this.views = 0;
    }

    public void addImage(byte[] image) {
        this.image = image;
    }

    public void incrementViews() {
        this.views++;
    }

    public void addComment(Comment comment) {
        this.comments.add(comment);
        comment.setPost(this);
    }

    // 좋아요 수 반환
    public int getLikes() {
        return likedUsers.size();
    }

    // 좋아요 추가
    public void addLike(User user) {
        this.likedUsers.add(user);
    }

    // 좋아요 제거
    public void removeLike(User user) {
        this.likedUsers.remove(user);
    }
}