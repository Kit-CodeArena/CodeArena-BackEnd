package com.example.codeArena.User.repository;

import com.example.codeArena.User.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {
    // 사용자 이름으로 사용자 찾기
    Optional<User> findByUsername(String username);

    // 이메일로 사용자 찾기
    Optional<User> findByEmail(String email);

    // 사용자 이름이 존재하는지 확인
    boolean existsByUsername(String username);

    // 이메일이 존재하는지 확인
    boolean existsByEmail(String email);

    // 여기에 필요한 추가적인 쿼리 메소드를 정의할 수 있습니다.
}
