-- CHAT_USER TABLE ------------------
CREATE TABLE CHAT_USERS (
    ID INTEGER GENERATED ALWAYS AS IDENTITY (START WITH 1 INCREMENT BY 1) PRIMARY KEY NOT NULL,
    NAME VARCHAR2(255) NOT NULL,
    EMAIL VARCHAR2(255) NOT NULL,
    PASSWORD VARCHAR2(60) NOT NULL,
    JOINED_AT TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT email_format CHECK (EMAIL LIKE '%@%.%'),
    CONSTRAINT unique_email UNIQUE (EMAIL)
);

-- CHAT_GROUP TABLE ------------------
CREATE TABLE CONVERSATIONS (
    ID INTEGER GENERATED ALWAYS AS IDENTITY (START WITH 1 INCREMENT BY 1) PRIMARY KEY NOT NULL,
    NAME VARCHAR2(255) NOT NULL,
    CREATED_AT TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- FRIENDSHIPS TABLE ------------------
CREATE TABLE FRIENDSHIPS (
    ID INTEGER GENERATED ALWAYS AS IDENTITY (START WITH 1 INCREMENT BY 1) PRIMARY KEY NOT NULL,
    USER1_ID INTEGER NOT NULL,
    USER2_ID INTEGER NOT NULL,
    FRIENDS_SINCE TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_user1 FOREIGN KEY (USER1_ID) REFERENCES CHAT_USERS(ID) ON DELETE CASCADE,
    CONSTRAINT fk_user2 FOREIGN KEY (USER2_ID) REFERENCES CHAT_USERS(ID) ON DELETE CASCADE,
    CONSTRAINT check_user1_not_user2 CHECK (USER1_ID != USER2_ID),
    CONSTRAINT unique_friendship UNIQUE (USER1_ID, USER2_ID)
);

-- FRIENDSHIP_REQUEST TABLE ------------------
CREATE TABLE FRIENDSHIP_REQUESTS (
    REQUESTER_ID INTEGER NOT NULL,
    REQUESTEE_ID INTEGER NOT NULL,
    REQUESTED_AT TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (REQUESTER_ID, REQUESTEE_ID),
    CONSTRAINT fk_requester FOREIGN KEY (REQUESTER_ID) REFERENCES CHAT_USERS(ID) ON DELETE CASCADE,
    CONSTRAINT fk_requestee FOREIGN KEY (REQUESTEE_ID) REFERENCES CHAT_USERS(ID) ON DELETE CASCADE,
    CONSTRAINT check_requester_not_requestee CHECK (REQUESTER_ID != REQUESTEE_ID)
);

-- CHAT_MESSAGE TABLE ------------------
CREATE TABLE CHAT_MESSAGES (
    ID INTEGER GENERATED ALWAYS AS IDENTITY (START WITH 1 INCREMENT BY 1) PRIMARY KEY NOT NULL,
    SENDER_ID INTEGER NOT NULL,
    CONVERSATION_ID INTEGER NOT NULL,
    MESSAGE_CONTENT VARCHAR2(255) NOT NULL,
    SENT_AT TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT message_sender FOREIGN KEY (SENDER_ID) REFERENCES CHAT_USERS(ID) ON DELETE CASCADE,
    CONSTRAINT message_conversation FOREIGN KEY (CONVERSATION_ID) REFERENCES CONVERSATIONS(ID) ON DELETE CASCADE
);

-- CHAT_MESSAGE_LIKES TABLE ------------------
CREATE TABLE MESSAGE_LIKES (
    MESSAGE_ID INTEGER NOT NULL,
    LIKER_ID INTEGER NOT NULL,
    PRIMARY KEY (MESSAGE_ID, LIKER_ID),
    CONSTRAINT liked_message FOREIGN KEY (MESSAGE_ID) REFERENCES CHAT_MESSAGES(ID) ON DELETE CASCADE,
    CONSTRAINT message_liker FOREIGN KEY (LIKER_ID) REFERENCES CHAT_USERS(ID) ON DELETE CASCADE
);

-- GROUP_MEMBERS TABLE ------------------
CREATE TABLE CONVERSATION_MEMBERS (
    CONVERSATION_ID INTEGER NOT NULL,
    MEMBER_ID INTEGER NOT NULL,
    JOINED_AT TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (CONVERSATION_ID, MEMBER_ID),
    CONSTRAINT fk_group FOREIGN KEY (CONVERSATION_ID) REFERENCES CONVERSATIONS(ID) ON DELETE CASCADE,
    CONSTRAINT fk_member FOREIGN KEY (MEMBER_ID) REFERENCES CHAT_USERS(ID) ON DELETE CASCADE
);

-- POSTS TABLE ------------------
CREATE TABLE POSTS (
    ID INTEGER GENERATED ALWAYS AS IDENTITY (START WITH 1 INCREMENT BY 1) PRIMARY KEY NOT NULL,
    POSTER_ID INTEGER NOT NULL,
    CONTENT VARCHAR2(255) NOT NULL,
    POSTED_AT TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_poster FOREIGN KEY (POSTER_ID) REFERENCES CHAT_USERS(ID) ON DELETE CASCADE
);

-- LIKES TABLE ------------------
CREATE TABLE POST_LIKES (
    POST_ID INTEGER NOT NULL,
    LIKER_ID INTEGER NOT NULL,
    PRIMARY KEY (POST_ID, LIKER_ID),
    CONSTRAINT post_creator FOREIGN KEY (POST_ID) REFERENCES POSTS(ID) ON DELETE CASCADE,
    CONSTRAINT post_liker FOREIGN KEY (LIKER_ID) REFERENCES CHAT_USERS(ID) ON DELETE CASCADE
);

-- COMMENTS TABLE ------------------
CREATE TABLE COMMENTS (
    ID INTEGER GENERATED ALWAYS AS IDENTITY (START WITH 1 INCREMENT BY 1) PRIMARY KEY NOT NULL,
    POST_ID INTEGER NOT NULL,
    COMMENTER_ID INTEGER NOT NULL,
    CONTENT VARCHAR2(255) NOT NULL,
    COMMENTED_AT TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_post FOREIGN KEY (POST_ID) REFERENCES POSTS(ID) ON DELETE CASCADE,
    CONSTRAINT fk_commenter FOREIGN KEY (COMMENTER_ID) REFERENCES CHAT_USERS(ID) ON DELETE CASCADE
);

-- POST COMMENTS LIKES TABLE ------------------
CREATE TABLE COMMENT_LIKES (
    COMMENT_ID INTEGER NOT NULL,
    LIKER_ID INTEGER NOT NULL,
    PRIMARY KEY (COMMENT_ID, LIKER_ID),
    CONSTRAINT comment_creator FOREIGN KEY (COMMENT_ID) REFERENCES COMMENTS(ID) ON DELETE CASCADE,
    CONSTRAINT comment_liker FOREIGN KEY (LIKER_ID) REFERENCES CHAT_USERS(ID) ON DELETE CASCADE
);

COMMIT;
