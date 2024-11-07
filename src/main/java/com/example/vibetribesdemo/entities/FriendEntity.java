package com.example.vibetribesdemo.entities;

import com.example.vibetribesdemo.Utilities.FriendshipStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "friendships")
public class FriendEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private LocalDateTime createdAt;

    private LocalDateTime respondedAt;

    @Enumerated(EnumType.STRING)
    private FriendshipStatus status;





    @Override
    public int hashCode() {
        return getClass().hashCode();
    }



    }

