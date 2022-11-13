package com.allog.dallog.subscription.presentation;

import com.allog.dallog.auth.dto.LoginMember;
import com.allog.dallog.auth.presentation.AuthenticationPrincipal;
import com.allog.dallog.subscription.application.SubscriptionService;
import com.allog.dallog.subscription.dto.request.SubscriptionUpdateRequest;
import com.allog.dallog.subscription.dto.response.SubscriptionResponse;
import com.allog.dallog.subscription.dto.response.SubscriptionsResponse;
import java.net.URI;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/members/me")
@RestController
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    public SubscriptionController(final SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }

    @PostMapping("/categories/{categoryId}/subscriptions")
    public ResponseEntity<SubscriptionResponse> save(@AuthenticationPrincipal final LoginMember loginMember,
                                                     @PathVariable final Long categoryId) {
        SubscriptionResponse response = subscriptionService.save(loginMember.getId(), categoryId);
        return ResponseEntity.created(
                        URI.create("/api/members/me/categories/" + categoryId + "/subscriptions/" + response.getId()))
                .body(response);
    }

    @GetMapping("/subscriptions")
    public ResponseEntity<SubscriptionsResponse> findByMemberId(
            @AuthenticationPrincipal final LoginMember loginMember) {
        SubscriptionsResponse response = subscriptionService.findByMemberId(loginMember.getId());
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/subscriptions/{subscriptionId}")
    public ResponseEntity<Void> update(@AuthenticationPrincipal final LoginMember loginMember,
                                       @PathVariable final Long subscriptionId,
                                       @Valid @RequestBody final SubscriptionUpdateRequest request) {
        subscriptionService.update(subscriptionId, loginMember.getId(), request);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/subscriptions/{subscriptionId}")
    public ResponseEntity<Void> delete(@AuthenticationPrincipal final LoginMember loginMember,
                                       @PathVariable final Long subscriptionId) {
        subscriptionService.delete(subscriptionId, loginMember.getId());
        return ResponseEntity.noContent().build();
    }
}
