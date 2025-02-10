-- Test script 1: Analyze user activity
WITH user_activity AS (
    SELECT
        cu.ID,
        cu.NAME,
        calculate_user_activity(cu.ID) as activity_score,
        COUNT(DISTINCT f1.USER2_ID) + COUNT(DISTINCT f2.USER1_ID) as friend_count,
        COUNT(DISTINCT p.ID) as post_count,
        COUNT(DISTINCT c.ID) as comment_count,
        COUNT(DISTINCT cm.ID) as message_count
    FROM CHAT_USERS cu
             LEFT JOIN FRIENDSHIPS f1 ON cu.ID = f1.USER1_ID
             LEFT JOIN FRIENDSHIPS f2 ON cu.ID = f2.USER2_ID
             LEFT JOIN POSTS p ON cu.ID = p.POSTER_ID
             LEFT JOIN COMMENTS c ON cu.ID = c.COMMENTER_ID
             LEFT JOIN CHAT_MESSAGES cm ON cu.ID = cm.SENDER_ID
    GROUP BY cu.ID, cu.NAME
)
SELECT
    NAME,
    activity_score,
    friend_count,
    post_count,
    comment_count,
    message_count,
    RANK() OVER (ORDER BY activity_score DESC) as activity_rank
FROM user_activity
WHERE activity_score > 0
ORDER BY activity_score DESC;



-- Test script 2: Analyze post popularity
WITH post_analytics AS (
    SELECT
        p.ID as post_id,
        cu.NAME as author,
        p.CONTENT,
        p.POSTED_AT,
        calculate_post_score(p.ID) as post_score,
        COUNT(DISTINCT pl.LIKER_ID) as like_count,
        COUNT(DISTINCT c.ID) as comment_count,
        COUNT(DISTINCT cl.LIKER_ID) as comment_likes_count
    FROM POSTS p
             JOIN CHAT_USERS cu ON p.POSTER_ID = cu.ID
             LEFT JOIN POST_LIKES pl ON p.ID = pl.POST_ID
             LEFT JOIN COMMENTS c ON p.ID = c.POST_ID
             LEFT JOIN COMMENT_LIKES cl ON c.ID = cl.COMMENT_ID
    WHERE p.POSTED_AT > SYSTIMESTAMP - INTERVAL '7' DAY
    GROUP BY p.ID, cu.NAME, p.CONTENT, p.POSTED_AT
)
SELECT
    author,
    SUBSTR(CONTENT, 1, 50) as content_preview,
    post_score,
    like_count,
    comment_count,
    comment_likes_count,
    ROUND((EXTRACT(HOUR FROM (SYSTIMESTAMP - POSTED_AT))/24), 2) as days_ago,
    RANK() OVER (ORDER BY post_score DESC) as popularity_rank
FROM post_analytics
WHERE post_score > 0
ORDER BY post_score DESC;



-- Example usage of friend recommendation procedure
DECLARE
    v_cursor SYS_REFCURSOR;
    v_user_id NUMBER;
    v_name VARCHAR2(255);
    v_common_friends NUMBER;
    v_activity_score NUMBER;
BEGIN
    v_user_id := 105;
    recommend_friends(v_user_id, v_cursor);

    DBMS_OUTPUT.PUT_LINE('Recommended friends for user ' || v_user_id || ':');
    LOOP
        FETCH v_cursor INTO v_user_id, v_name, v_common_friends, v_activity_score;
        EXIT WHEN v_cursor%NOTFOUND;
        DBMS_OUTPUT.PUT_LINE(v_name || ' - Common friends: ' || v_common_friends ||
                             ', Activity: ' || v_activity_score);
    END LOOP;
    CLOSE v_cursor;
END;
/

-- Example usage of conversation activity analysis procedure
DECLARE
    v_cursor SYS_REFCURSOR;
    v_conv_name VARCHAR2(255);
    v_participants NUMBER;
    v_message_count NUMBER;
    v_total_likes NUMBER;
    v_messages_per_day NUMBER;
    v_likes_per_message NUMBER;
BEGIN
    analyze_conversation_activity(30, v_cursor);

    DBMS_OUTPUT.PUT_LINE('Analysis of last 30 days:');
    LOOP
        FETCH v_cursor INTO v_conv_name, v_participants, v_message_count,
            v_total_likes, v_messages_per_day, v_likes_per_message;
        EXIT WHEN v_cursor%NOTFOUND;
        DBMS_OUTPUT.PUT_LINE(v_conv_name || ' - Users: ' || v_participants ||
                             ', messages/day: ' || v_messages_per_day ||
                             ', likes/message: ' || v_likes_per_message);
    END LOOP;
    CLOSE v_cursor;
END;
/



