package com.example.vibetribesdemo.Repository;

import com.example.vibetribesdemo.entities.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    // Method to find a user by email (used during login and JWT processing)

    Optional<UserEntity> findByUsername(String username);

    @Query("SELECT u FROM UserEntity u " +
            "WHERE u.username LIKE %:query% " +
            "AND u.userId NOT IN (" +
            "    SELECT f.recipient.userId " +
            "    FROM FriendEntity f " +
            "    WHERE f.requester.userId = :currentUserId AND f.status = 'BLOCKED'" +
            ")")
    Page<UserEntity> searchUsersWithFilters(
            @Param("query") String query,
            @Param("currentUserId") Long currentUserId,
            Pageable pageable);


}
