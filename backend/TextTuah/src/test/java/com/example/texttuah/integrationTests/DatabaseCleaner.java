//package com.example.texttuah.integrationTests;
//
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//@Component
//public class DatabaseCleaner {
//
//    private final JdbcTemplate jdbcTemplate;
//
//    @Autowired
//    public DatabaseCleaner(JdbcTemplate jdbcTemplate) {
//        this.jdbcTemplate = jdbcTemplate;
//    }
//
//    @Transactional
//    public void clean() {
//        jdbcTemplate.execute("DELETE FROM comments");
//        jdbcTemplate.execute("DELETE FROM comment_likes");
//        jdbcTemplate.execute("DELETE FROM message_likes");
//        jdbcTemplate.execute("DELETE FROM chat_messages");
//        jdbcTemplate.execute("DELETE FROM conversation_members");
//        jdbcTemplate.execute("DELETE FROM conversations");
//        jdbcTemplate.execute("DELETE FROM post_likes");
//        jdbcTemplate.execute("DELETE FROM posts");
//        jdbcTemplate.execute("DELETE FROM friendships");
//        jdbcTemplate.execute("DELETE FROM chat_users");
//        jdbcTemplate.execute("DELETE FROM friendship_requests");
//        try {
//            Thread.sleep(1000);
//        } catch (InterruptedException e)
//        {
//            e.printStackTrace();
//        }
//    }
//}