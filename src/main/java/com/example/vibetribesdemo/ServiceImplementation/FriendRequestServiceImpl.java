package com.example.vibetribesdemo.ServiceImplementation;

import com.example.vibetribesdemo.entities.FriendEntity;
import com.example.vibetribesdemo.entities.UserEntity;
import com.example.vibetribesdemo.Repository.FriendRequestRepository;
import com.example.vibetribesdemo.Service.FriendRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class FriendRequestServiceImpl implements FriendRequestService {

    @Autowired
    private FriendRequestRepository friendRequestRepository;



    @Override
    public FriendEntity sendFriendRequest(UserEntity requester, UserEntity recipient) {
        if (friendRequestRepository.existsByRequesterAndRecipient(requester, recipient)) {
            throw new RuntimeException("Friend request already sent");
        }
        FriendEntity request = new FriendEntity();
        request.setRequester(requester);
        request.setRecipient(recipient);
        request.setRequestDate(LocalDateTime.now());
        request.setStatus(FriendEntity.FriendRequestStatus.PENDING);
        return friendRequestRepository.save(request);
    }

    @Override
    public FriendEntity acceptFriendRequest(Long requestId) {
        FriendEntity friendRequest = friendRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Friend request not found"));

        if (friendRequest.getStatus() != FriendEntity.FriendRequestStatus.PENDING) {
            throw new IllegalStateException("Friend request is not pending");
        }

        // İsteği kabul et
        friendRequest.setStatus(FriendEntity.FriendRequestStatus.ACCEPTED);
        friendRequestRepository.save(friendRequest);

        // Çift yönlü ilişki oluştur
        createMutualFriendship(friendRequest.getRequester(), friendRequest.getRecipient());

        return friendRequest;
    }
    private void createMutualFriendship(UserEntity user1, UserEntity user2) {
        FriendEntity mutualFriendship = new FriendEntity();
        mutualFriendship.setRequester(user2); // Ters ilişkiyi oluştur
        mutualFriendship.setRecipient(user1);
        mutualFriendship.setRequestDate(LocalDateTime.now());
        mutualFriendship.setStatus(FriendEntity.FriendRequestStatus.ACCEPTED);

        friendRequestRepository.save(mutualFriendship);
    }

    @Override
    public List<UserEntity> findFriends(String username) {
        return friendRequestRepository.findFriendsByUsername(username);
    }

    @Override
    public FriendEntity declineFriendRequest(Long requestId) {
        FriendEntity request = friendRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found"));
        request.setStatus(FriendEntity.FriendRequestStatus.DECLINED);
        return friendRequestRepository.save(request);
    }

    @Override
    public boolean isFriendRequestPending(UserEntity requester, UserEntity recipient) {
        return friendRequestRepository.existsByRequesterAndRecipient(requester, recipient);
    }


    @Override
    public FriendEntity blockUser(UserEntity blocker, UserEntity blocked) {
        // İki kullanıcı arasındaki ilişkiyi bul
        FriendEntity friendship = friendRequestRepository.findByRequesterAndRecipient(blocker, blocked)
                .orElseThrow(() -> new RuntimeException("Friendship not found"));

        // Durumu BLOCKED olarak güncelle
        friendship.setStatus(FriendEntity.FriendRequestStatus.BLOCKED);

        // Güncellenmiş ilişkiyi kaydet
        return friendRequestRepository.save(friendship);
    }


    @Override
    public boolean isBlocked(UserEntity user1, UserEntity user2) {
        // İki kullanıcı arasındaki engelleme durumunu kontrol et
        return friendRequestRepository.findByRequesterAndRecipient(user1, user2)
                .filter(friendship -> friendship.getStatus() == FriendEntity.FriendRequestStatus.BLOCKED)
                .isPresent();
    }

}
