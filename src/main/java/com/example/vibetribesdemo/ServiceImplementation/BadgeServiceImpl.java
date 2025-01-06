package com.example.vibetribesdemo.ServiceImplementation;

import com.example.vibetribesdemo.DTOs.BadgeRequestDto;
import com.example.vibetribesdemo.DTOs.BadgeResponseDto;
import com.example.vibetribesdemo.DTOs.UserBadgeResponseDto;
import com.example.vibetribesdemo.Repository.BadgeRepository;
import com.example.vibetribesdemo.Repository.UserBadgesRepository;
import com.example.vibetribesdemo.Repository.UserRepository;
import com.example.vibetribesdemo.Service.BadgeService;
import com.example.vibetribesdemo.entities.BadgesEntity;
import com.example.vibetribesdemo.entities.UserBadgesEntity;
import com.example.vibetribesdemo.entities.UserEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BadgeServiceImpl implements BadgeService {
    private final BadgeRepository badgeRepository;
    private final UserBadgesRepository userBadgesRepository;
    private final UserRepository userRepository;

    public BadgeServiceImpl(BadgeRepository badgeRepository, UserBadgesRepository userBadgesRepository, UserRepository userRepository) {
        this.badgeRepository = badgeRepository;
        this.userBadgesRepository = userBadgesRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void awardFirstLoginBadge(UserEntity user) {
        BadgesEntity firstLoginBadge = badgeRepository.findById(1L) // Assume ID 1 is "Welcome Starter"
                .orElseThrow(() -> new RuntimeException("First Login Badge not found"));

        if (!userBadgesRepository.existsByUserAndBadge(user, firstLoginBadge)) {
            awardBadgeToUser(user, firstLoginBadge);
        }
    }

    @Override
    public void awardReputationBadges(UserEntity user) {
        int reputationPoints = user.getReputationPointsCount(); // Use the dynamic getter method
        List<BadgesEntity> eligibleBadges = badgeRepository.findEligibleBadgesByReputation(reputationPoints);

        for (BadgesEntity badge : eligibleBadges) {
            if (!userBadgesRepository.existsByUserAndBadge(user, badge)) {
                awardBadgeToUser(user, badge);
            }
        }
    }

    @Override
    public void awardEventBadges(UserEntity user) {
        int hostedEvents = user.getHostedEventsCount();
        List<BadgesEntity> eligibleBadges = badgeRepository.findByEventCountRequirementLessThanEqual(hostedEvents);

        for (BadgesEntity badge : eligibleBadges) {
            if (!userBadgesRepository.existsByUserAndBadge(user, badge)) {
                awardBadgeToUser(user, badge);
            }
        }
    }



    private void awardBadgeToUser(UserEntity user, BadgesEntity badge) {
        UserBadgesEntity userBadge = new UserBadgesEntity();
        userBadge.setUser(user);
        userBadge.setBadge(badge);
        userBadgesRepository.save(userBadge);
    }

    @Override
    public List<BadgeResponseDto> getAllBadges() {
        return badgeRepository.findAllBadges().stream()
                .map(badge -> {
                    BadgeResponseDto dto = new BadgeResponseDto();
                    dto.setBadgeId(badge.getBadgeId());
                    dto.setBadgeName(badge.getBadgeName());
                    dto.setDescription(badge.getDescription());
                    dto.setReputationRequirement(badge.getReputationRequirement());
                    dto.setEventCountRequirement(badge.getEventCountRequirement());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<UserBadgeResponseDto> getUserBadges(String username) {
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return userBadgesRepository.findByUser(user).stream()
                .map(userBadge -> {
                    UserBadgeResponseDto dto = new UserBadgeResponseDto();
                    dto.setBadgeName(userBadge.getBadge().getBadgeName());
                    dto.setDescription(userBadge.getBadge().getDescription());
                    dto.setEarnedAt(userBadge.getEarnedAt());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public void createOrUpdateBadge(BadgeRequestDto badgeRequestDto) {
        BadgesEntity badge = new BadgesEntity();
        badge.setBadgeName(badgeRequestDto.getBadgeName());
        badge.setDescription(badgeRequestDto.getDescription());
        badge.setReputationRequirement(badgeRequestDto.getReputationRequirement());
        badge.setEventCountRequirement(badgeRequestDto.getEventCountRequirement());
        badgeRepository.save(badge);
    }

    @Override
    public void deleteBadge(Long badgeId) {
        badgeRepository.deleteById(badgeId);
    }
}
