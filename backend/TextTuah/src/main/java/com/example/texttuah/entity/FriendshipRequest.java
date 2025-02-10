package com.example.texttuah.entity;


import com.example.texttuah.embeddable.FriendshipRequestId;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "FRIENDSHIP_REQUESTS")
public class FriendshipRequest {
    @EmbeddedId
    private FriendshipRequestId id;

    @MapsId("requesterId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "REQUESTER_ID", nullable = false)
    private ChatUser requester;

    @MapsId("requesteeId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "REQUESTEE_ID", nullable = false)
    private ChatUser requested;

    @Column(name = "REQUESTED_AT")
    private LocalDateTime requestedAt;

    public FriendshipRequest() {
    }

    public FriendshipRequest(FriendshipRequestId id, ChatUser requester, ChatUser requested, LocalDateTime requestedAt) {
        this.id = id;
        this.requester = requester;
        this.requested = requested;
        this.requestedAt = requestedAt;
    }

    public FriendshipRequestId getId() {
        return id;
    }

    public void setId(FriendshipRequestId id) {
        this.id = id;
    }

    public ChatUser getRequester() {
        return requester;
    }

    public void setRequester(ChatUser requester) {
        this.requester = requester;
    }

    public ChatUser getRequested() {
        return requested;
    }

    public void setRequested(ChatUser requested) {
        this.requested = requested;
    }

    public LocalDateTime getRequestedAt() {
        return requestedAt;
    }

    public void setRequestedAt(LocalDateTime requestedAt) {
        this.requestedAt = requestedAt;
    }
}
