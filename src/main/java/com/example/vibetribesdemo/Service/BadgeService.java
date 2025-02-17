package com.example.vibetribesdemo.Service;

import com.example.vibetribesdemo.DTOs.BadgeRequestDto;
import com.example.vibetribesdemo.DTOs.BadgeResponseDto;
import com.example.vibetribesdemo.DTOs.UserBadgeResponseDto;
import com.example.vibetribesdemo.entities.UserEntity;

import java.util.List;

public interface BadgeService {
    void awardFirstLoginBadge(UserEntity user);

    void awardReputationBadges(UserEntity user);

    void awardEventBadges(UserEntity user);

    List<BadgeResponseDto> getAllBadges();

    List<UserBadgeResponseDto> getUserBadges(String username);

    void createOrUpdateBadge(BadgeRequestDto badgeRequestDto);

    void deleteBadge(Long badgeId);
}

