package com.example.vibetribesdemo.Repository;

import com.example.vibetribesdemo.entities.FriendEntity;
import com.example.vibetribesdemo.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface FriendRequestRepository extends JpaRepository<FriendEntity, Long> {
    List<FriendEntity> findByRecipientAndStatus(UserEntity recipient, FriendEntity.FriendRequestStatus status);
    List<FriendEntity> findByRequesterAndStatus(UserEntity requester, FriendEntity.FriendRequestStatus status);
    boolean existsByRequesterAndRecipient(UserEntity requester, UserEntity recipient);
}
