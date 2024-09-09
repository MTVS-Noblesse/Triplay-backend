package com.noblesse.backend.follow.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="follow")
@Getter
public class Follow {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "to_user_id") // 팔로우 하는 유저 아이디
    private Long toUserId;

    @Column(name = "from_user_id") // 팔로우 받는 유저 아이디
    private Long fromUserId;

    protected Follow() {}

    public Follow(Long toUserId, Long fromUserId) {
        this.toUserId = toUserId;
        this.fromUserId = fromUserId;
    }
}