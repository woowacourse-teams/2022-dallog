package com.allog.dallog.infrastructure.log.dto;

import java.util.List;

public class DiscordWebhookRequest {

    private String username;
    private List<Embed> embeds;

    private DiscordWebhookRequest() {
    }

    public DiscordWebhookRequest(final String username, final List<Embed> embeds) {
        this.username = username;
        this.embeds = embeds;
    }

    public String getUsername() {
        return username;
    }

    public List<Embed> getEmbeds() {
        return embeds;
    }
}
