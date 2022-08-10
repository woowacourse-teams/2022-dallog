package com.allog.dallog.infrastructure.log;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.IThrowableProxy;
import ch.qos.logback.classic.spi.StackTraceElementProxy;
import ch.qos.logback.core.UnsynchronizedAppenderBase;
import com.allog.dallog.infrastructure.log.dto.DiscordWebhookRequest;
import com.allog.dallog.infrastructure.log.dto.Embed;
import com.allog.dallog.infrastructure.log.dto.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

public class DiscordAppender extends UnsynchronizedAppenderBase<ILoggingEvent> {

    private static final String TITLE_FORMAT = "[%s] %s";
    private static final String DESCRIPTION_FORMAT = "%s: %s";
    private static final RestTemplate CLIENT;

    static {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(3000);
        CLIENT = new RestTemplate(factory);
    }

    private String username;
    private String embedsColor;
    private int stackTraceMaxSize;
    private String webhookUri;

    @Override
    protected void append(final ILoggingEvent eventObject) {
        if (!Objects.isNull(webhookUri) && !webhookUri.isEmpty()) {
            String title = getTitle(eventObject);
            List<Embed> embeds = getEmbeds(title, embedsColor, eventObject);
            DiscordWebhookRequest request = new DiscordWebhookRequest(username, embeds);

            CLIENT.postForEntity(webhookUri, request, Void.class);
        }
    }

    private String getTitle(final ILoggingEvent eventObject) {
        return String.format(TITLE_FORMAT, eventObject.getLevel(), eventObject.getMessage());
    }

    private List<Embed> getEmbeds(final String title, final String embedsColor, final ILoggingEvent eventObject) {
        if (Objects.isNull(eventObject.getThrowableProxy())) {
            return List.of(new Embed(title, embedsColor));
        }

        IThrowableProxy throwableProxy = eventObject.getThrowableProxy();
        String description = getDescription(throwableProxy);
        List<Field> fields = getFields(throwableProxy);

        return List.of(new Embed(title, description, embedsColor, fields));
    }

    private String getDescription(final IThrowableProxy throwableProxy) {
        return String.format(DESCRIPTION_FORMAT, throwableProxy.getClassName(), throwableProxy.getMessage());
    }

    private List<Field> getFields(final IThrowableProxy throwableProxy) {
        List<String> stackTraces = Arrays.stream(throwableProxy.getStackTraceElementProxyArray())
                .map(StackTraceElementProxy::getSTEAsString)
                .limit(stackTraceMaxSize)
                .collect(Collectors.toList());

        return stackTraces.stream()
                .map(Field::from)
                .collect(Collectors.toList());
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(final String username) {
        this.username = username;
    }

    public String getEmbedsColor() {
        return embedsColor;
    }

    public void setEmbedsColor(final String embedsColor) {
        this.embedsColor = embedsColor;
    }

    public int getStackTraceMaxSize() {
        return stackTraceMaxSize;
    }

    public void setStackTraceMaxSize(final int stackTraceMaxSize) {
        this.stackTraceMaxSize = stackTraceMaxSize;
    }

    public String getWebhookUri() {
        return webhookUri;
    }

    public void setWebhookUri(final String webhookUri) {
        this.webhookUri = webhookUri;
    }
}
