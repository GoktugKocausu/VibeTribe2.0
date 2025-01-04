package com.example.vibetribesdemo.Repository;

import com.example.vibetribesdemo.entities.AttandanceEntity;
import com.example.vibetribesdemo.entities.EventEntity;
import com.example.vibetribesdemo.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AttendanceRepository extends JpaRepository<AttandanceEntity, Long> {
    boolean existsByEventAndUser(EventEntity event, UserEntity user);


    @Query("SELECT COUNT(a) > 0 FROM AttandanceEntity a WHERE a.user.userId = :userId AND a.event.eventId = :eventId")
    boolean existsByUserIdAndEventId(@Param("userId") Long userId, @Param("eventId") Long eventId);



    @Query("SELECT a FROM AttandanceEntity a WHERE a.event.eventId = :eventId AND a.user.userId = :userId")
    Optional<AttandanceEntity> findByEventAndUser(@Param("eventId") Long eventId, @Param("userId") Long userId);

    @Modifying
    @Query("DELETE FROM AttandanceEntity a WHERE a.event.eventId = :eventId AND a.user.userId = :userId")
    void deleteAttendanceByEventAndUser(@Param("eventId") Long eventId, @Param("userId") Long userId);



}

