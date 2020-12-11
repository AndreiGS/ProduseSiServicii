package com.findthebusiness.backend.repository;

import com.findthebusiness.backend.entity.Authentication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;

@Repository
public interface AuthenticationRepository extends JpaRepository<Authentication, String> {

    Optional<Authentication> findByAccessTokenEqualsAndCsrfTokenEqualsAndRefreshTokenEquals(String accessToken, String csrfToken, String refreshToken);
    Optional<Authentication> findByRefreshToken(String refreshToken);
    Optional<Authentication> findByAccessToken(String accessToken);

    @Transactional
    @Modifying
    void deleteByAccessToken(String accessToken);

    @Transactional
    @Modifying
    void deleteAllByDateBefore(Date date);
}
