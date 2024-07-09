package com.santanu.jwtAuth.repository;

import com.santanu.jwtAuth.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token,Long> {

    @Query("""
            Select t from Token t inner join User u
            on t.user.id = u.id
            where t.user.id =:userId and t.loggedOut = false
            """)
    List<Token> findAllTokenByUser(Long userId);

    Optional<Token> findByToken(String token);
}
