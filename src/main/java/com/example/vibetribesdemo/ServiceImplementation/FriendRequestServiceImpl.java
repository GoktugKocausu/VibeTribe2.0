package com.example.vibetribesdemo.ServiceImplementation;

import com.example.vibetribesdemo.entities.FriendEntity;
import com.example.vibetribesdemo.entities.UserEntity;
import com.example.vibetribesdemo.Repository.FriendRequestRepository;
import com.example.vibetribesdemo.Service.FriendRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
        // Fetch the friend request using the request ID
        FriendEntity request = friendRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Friend request not found"));

        // request hala gonderimde mi bak
        if (request.getStatus() != FriendEntity.FriendRequestStatus.PENDING) {
            throw new RuntimeException("Friend request is not pending");
        }


        request.setStatus(FriendEntity.FriendRequestStatus.ACCEPTED);


        return friendRequestRepository.save(request);
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
}
