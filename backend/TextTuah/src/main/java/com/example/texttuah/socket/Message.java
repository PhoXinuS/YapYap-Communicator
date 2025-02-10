package com.example.texttuah.socket;

import lombok.Builder;

@Builder
public record Message(String username, String message) { }
