package com.example.vibetribesdemo.Service;

import com.example.vibetribesdemo.entities.FriendEntity;
import com.example.vibetribesdemo.entities.UserEntity;

public interface FriendRequestService {



    FriendEntity sendFriendRequest(UserEntity requester, UserEntity recipient);
    FriendEntity acceptFriendRequest(Long requestId);
    FriendEntity declineFriendRequest(Long requestId);
    boolean isFriendRequestPending(UserEntity requester, UserEntity recipient);
}