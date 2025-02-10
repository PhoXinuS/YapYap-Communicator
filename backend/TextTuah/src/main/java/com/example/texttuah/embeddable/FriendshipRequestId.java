package com.example.texttuah.embeddable;

import jakarta.persistence.Embeddable;

import java.util.Objects;

@Embeddable
public class FriendshipRequestId {
    private long requesterId;
    private long requesteeId;

    public FriendshipRequestId() {}

    public FriendshipRequestId(long requesterId, long requesteeId) {
        this.requesterId = requesterId;
        this.requesteeId = requesteeId;
    }

    public long getRequesterId() {
        return requesterId;
    }

    public void setRequesterId(long requesterId) {
        this.requesterId = requesterId;
    }

    public long getRequesteeId() {
        return requesteeId;
    }

    public void setRequesteeId(long requesteeId) {
        this.requesteeId = requesteeId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FriendshipRequestId that = (FriendshipRequestId) o;
        return Objects.equals(requesterId, that.requesterId) &&
                Objects.equals(requesteeId, that.requesteeId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(requesterId, requesteeId);
    }
}
