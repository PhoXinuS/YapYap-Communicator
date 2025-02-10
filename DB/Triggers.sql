CREATE OR REPLACE TRIGGER prevent_self_like
BEFORE INSERT ON MESSAGE_LIKES
FOR EACH ROW
DECLARE
    v_sender_id NUMBER;
BEGIN
    SELECT SENDER_ID INTO v_sender_id
    FROM CHAT_MESSAGES
    WHERE ID = :NEW.MESSAGE_ID;

    IF v_sender_id = :NEW.LIKER_ID THEN
        RAISE_APPLICATION_ERROR(-20001, 'Users cannot like their own messages');
    END IF;
END;
/

CREATE OR REPLACE TRIGGER prevent_message_spam
BEFORE INSERT ON CHAT_MESSAGES
FOR EACH ROW
DECLARE
    v_message_count NUMBER;
BEGIN
    SELECT COUNT(*) INTO v_message_count
    FROM CHAT_MESSAGES
    WHERE SENDER_ID = :NEW.SENDER_ID
    AND SENT_AT > SYSTIMESTAMP - INTERVAL '1' MINUTE;

    IF v_message_count >= 10 THEN
        RAISE_APPLICATION_ERROR(-20002, 'Too many messages in short time. Please wait.');
    END IF;
END;
/
