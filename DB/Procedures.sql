CREATE OR REPLACE PROCEDURE recommend_friends(
    p_user_id IN NUMBER,
    p_cursor OUT SYS_REFCURSOR
) IS
BEGIN
    OPEN p_cursor FOR
        WITH user_friends AS (
            SELECT USER2_ID AS friend_id
            FROM FRIENDSHIPS
            WHERE USER1_ID = p_user_id
            UNION
            SELECT USER1_ID AS friend_id
            FROM FRIENDSHIPS
            WHERE USER2_ID = p_user_id
        ),
             friends_of_friends AS (
                 SELECT DISTINCT f2.USER2_ID AS potential_friend
                 FROM user_friends uf
                          JOIN FRIENDSHIPS f2 ON uf.friend_id = f2.USER1_ID
                 WHERE f2.USER2_ID != p_user_id
                 UNION
                 SELECT DISTINCT f2.USER1_ID AS potential_friend
                 FROM user_friends uf
                          JOIN FRIENDSHIPS f2 ON uf.friend_id = f2.USER2_ID
                 WHERE f2.USER1_ID != p_user_id
             )
        SELECT
            cu.ID,
            cu.NAME,
            COUNT(DISTINCT uf.friend_id) as common_friends,
            calculate_user_activity(cu.ID) as activity_score
        FROM friends_of_friends fof
                 JOIN CHAT_USERS cu ON fof.potential_friend = cu.ID
                 LEFT JOIN user_friends uf ON uf.friend_id = cu.ID
        WHERE cu.ID NOT IN (SELECT friend_id FROM user_friends)
        GROUP BY cu.ID, cu.NAME
        ORDER BY common_friends DESC, activity_score DESC;
END;
/


CREATE OR REPLACE PROCEDURE analyze_conversation_activity(
    p_days_back IN NUMBER,
    p_cursor OUT SYS_REFCURSOR
) IS
BEGIN
    OPEN p_cursor FOR
        WITH conversation_stats AS (
            SELECT
                c.ID as conv_id,
                c.NAME as conv_name,
                COUNT(DISTINCT cm.SENDER_ID) as unique_participants,
                COUNT(cm.ID) as message_count,
                COUNT(ml.LIKER_ID) as total_likes
            FROM CONVERSATIONS c
                     JOIN CHAT_MESSAGES cm ON c.ID = cm.CONVERSATION_ID
                     LEFT JOIN MESSAGE_LIKES ml ON cm.ID = ml.MESSAGE_ID
            WHERE cm.SENT_AT > SYSTIMESTAMP - INTERVAL '1' DAY * p_days_back
            GROUP BY c.ID, c.NAME
        )
        SELECT
            cs.conv_name,
            cs.unique_participants,
            cs.message_count,
            cs.total_likes,
            ROUND(cs.message_count / p_days_back, 2) as messages_per_day,
            ROUND(cs.total_likes / NULLIF(cs.message_count, 0), 2) as likes_per_message
        FROM conversation_stats cs
        WHERE cs.message_count > 0
        ORDER BY cs.message_count DESC;
END;
/
