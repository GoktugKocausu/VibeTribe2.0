package com.example.vibetribesdemo.Repository;

import com.example.vibetribesdemo.entities.BadgesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BadgeRepository extends JpaRepository<BadgesEntity, Long> {
    @Query("SELECT b FROM BadgesEntity b WHERE b.reputationRequirement <= :reputation")
    List<BadgesEntity> findEligibleBadgesByReputation(@Param("reputation") int reputation);

    List<BadgesEntity> findByEventCountRequirementLessThanEqual(int eventCount);

    @Query("SELECT b FROM BadgesEntity b")
    List<BadgesEntity> findAllBadges();
}
