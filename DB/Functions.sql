CREATE OR REPLACE FUNCTION calculate_post_score(p_post_id IN NUMBER)
    RETURN NUMBER IS
    v_likes_count NUMBER;
    v_comments_count NUMBER;
    v_age_hours NUMBER;
    v_score NUMBER;
BEGIN
    SELECT COUNT(*) INTO v_likes_count
    FROM POST_LIKES
    WHERE POST_ID = p_post_id;

    SELECT COUNT(*) INTO v_comments_count
    FROM COMMENTS
    WHERE POST_ID = p_post_id;

    SELECT EXTRACT(HOUR FROM (SYSTIMESTAMP - POSTED_AT)) INTO v_age_hours
    FROM POSTS
    WHERE ID = p_post_id;

    -- Score = (likes * 1.5 + comments * 2) / (age_hours + 2)^1.8
    v_score := (v_likes_count * 1.5 + v_comments_count * 2) / POWER(v_age_hours + 2, 1.8);

    RETURN ROUND(v_score, 2);
END;
/

CREATE OR REPLACE FUNCTION calculate_user_activity(p_user_id IN NUMBER)
    RETURN NUMBER IS
    v_posts_count NUMBER;
    v_comments_count NUMBER;
    v_messages_count NUMBER;
    v_activity_score NUMBER;
BEGIN
    SELECT COUNT(*) INTO v_posts_count
    FROM POSTS
    WHERE POSTER_ID = p_user_id
      AND POSTED_AT > SYSTIMESTAMP - INTERVAL '30' DAY;

    SELECT COUNT(*) INTO v_comments_count
    FROM COMMENTS
    WHERE COMMENTER_ID = p_user_id
      AND COMMENTED_AT > SYSTIMESTAMP - INTERVAL '30' DAY;

    SELECT COUNT(*) INTO v_messages_count
    FROM CHAT_MESSAGES
    WHERE SENDER_ID = p_user_id
      AND SENT_AT > SYSTIMESTAMP - INTERVAL '30' DAY;

    v_activity_score := (v_posts_count * 3 + v_comments_count * 2 + v_messages_count) / 30.0;

    RETURN ROUND(v_activity_score, 2);
END;
/
