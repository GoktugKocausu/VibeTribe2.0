package com.example.vibetribesdemo.Controller;

import com.example.vibetribesdemo.Repository.FriendRequestRepository;
import com.example.vibetribesdemo.Repository.UserRepository;
import com.example.vibetribesdemo.entities.FriendEntity;
import com.example.vibetribesdemo.entities.UserEntity;
import com.example.vibetribesdemo.Service.FriendRequestService;
import com.example.vibetribesdemo.Service.User.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/friend-requests")
public class FriendRequestController {

    @Autowired
    private FriendRequestService friendRequestService;

    @Autowired
    private UserService userService;

    @PostMapping("/send")
    public FriendEntity sendFriendRequest(@RequestParam String requesterUsername, @RequestParam String recipientUsername) {
        UserEntity requester = userService.findByUsername(requesterUsername)
                .orElseThrow(() -> new RuntimeException("Requester not found"));
        UserEntity recipient = userService.findByUsername(recipientUsername)
                .orElseThrow(() -> new RuntimeException("Recipient not found"));
        return friendRequestService.sendFriendRequest(requester, recipient);
    }

    @PostMapping("/accept/{requestId}")
    public FriendEntity acceptFriendRequest(@PathVariable Long requestId) {
        return friendRequestService.acceptFriendRequest(requestId);
    }

    @PostMapping("/decline/{requestId}")
    public FriendEntity declineFriendRequest(@PathVariable Long requestId) {
        return friendRequestService.declineFriendRequest(requestId);
    }

    @GetMapping("/{username}/friends")
    public List<UserEntity> getFriends(@PathVariable String username) {
        return friendRequestService.findFriends(username);
    }

    @PostMapping("/block")
    public FriendEntity blockUser(@RequestParam String blockerUsername, @RequestParam String blockedUsername) {
        // Kullanıcıları username ile bul
        UserEntity blocker = userService.findByUsername(blockerUsername)
                .orElseThrow(() -> new RuntimeException("Blocker not found"));
        UserEntity blocked = userService.findByUsername(blockedUsername)
                .orElseThrow(() -> new RuntimeException("Blocked user not found"));

        // Blocklama işlemini gerçekleştir
        return friendRequestService.blockUser(blocker, blocked);
    }

    @GetMapping("/is-blocked")
    public boolean isBlocked(@RequestParam String user1Username, @RequestParam String user2Username) {
        // Kullanıcıları username ile bul
        UserEntity user1 = userService.findByUsername(user1Username)
                .orElseThrow(() -> new RuntimeException("User 1 not found"));
        UserEntity user2 = userService.findByUsername(user2Username)
                .orElseThrow(() -> new RuntimeException("User 2 not found"));

        // Engellenme durumunu kontrol et
        return friendRequestService.isBlocked(user1, user2);
    }

}
