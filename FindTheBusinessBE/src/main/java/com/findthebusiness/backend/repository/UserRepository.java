package com.findthebusiness.backend.repository;

import com.findthebusiness.backend.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface UserRepository extends JpaRepository<Users, String> {
    Optional<Users> findUsersByEmail(byte[] email);
    Optional<Users> findUsersById(String id);

    @Modifying
    void deleteById(String id);
}
