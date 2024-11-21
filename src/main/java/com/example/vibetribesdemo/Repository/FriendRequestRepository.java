package com.example.vibetribesdemo.Repository;

import com.example.vibetribesdemo.entities.FriendEntity;
import com.example.vibetribesdemo.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FriendRequestRepository extends JpaRepository<FriendEntity, Long> {
    List<FriendEntity> findByRecipientAndStatus(UserEntity recipient, FriendEntity.FriendRequestStatus status);
    List<FriendEntity> findByRequesterAndStatus(UserEntity requester, FriendEntity.FriendRequestStatus status);
    boolean existsByRequesterAndRecipient(UserEntity requester, UserEntity recipient);

    @Query("SELECT f.recipient FROM FriendEntity f WHERE f.requester.username = :username AND f.status = 'ACCEPTED' " +
            "UNION " +
            "SELECT f.requester FROM FriendEntity f WHERE f.recipient.username = :username AND f.status = 'ACCEPTED'")
    List<UserEntity> findFriendsByUsername(@Param("username") String username);

    Optional<FriendEntity> findByRequesterAndRecipient(UserEntity requester, UserEntity recipient);


}
