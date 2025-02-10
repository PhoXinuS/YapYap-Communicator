package com.example.texttuah.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "FRIENDSHIPS")
public class Friendship {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER1_ID")
    private ChatUser user1;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER2_ID")
    private ChatUser user2;

    @Column(name = "FRIENDS_SINCE")
    private LocalDateTime friendsSince;

    public Friendship() {
    }

    public Friendship(Long id, ChatUser user1, ChatUser user2, LocalDateTime friendsSince) {
        this.id = id;
        this.user1 = user1;
        this.user2 = user2;
        this.friendsSince = friendsSince;
    }
    
    @PrePersist
    private void setDefaultValue() {
        this.friendsSince = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ChatUser getUser1() {
        return user1;
    }

    public void setUser1(ChatUser user1) {
        this.user1 = user1;
    }

    public ChatUser getUser2() {
        return user2;
    }

    public void setUser2(ChatUser user2) {
        this.user2 = user2;
    }

    public LocalDateTime getFriendsSince() {
        return friendsSince;
    }

    public void setFriendsSince(LocalDateTime friendsSince) {
        this.friendsSince = friendsSince;
    }
}
