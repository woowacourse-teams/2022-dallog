package com.allog.dallog.presentation;

import com.allog.dallog.domain.subscription.application.SubscriptionService;
import com.allog.dallog.domain.auth.dto.LoginMember;
import com.allog.dallog.domain.subscription.dto.request.SubscriptionUpdateRequest;
import com.allog.dallog.domain.subscription.dto.response.SubscriptionResponse;
import com.allog.dallog.domain.subscription.dto.response.SubscriptionsResponse;
import com.allog.dallog.presentation.auth.AuthenticationPrincipal;
import java.net.URI;
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
    public ResponseEntity<SubscriptionsResponse> findMine(@AuthenticationPrincipal final LoginMember loginMember) {
        SubscriptionsResponse response = subscriptionService.findByMemberId(loginMember.getId());
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/subscriptions/{subscriptionId}")
    public ResponseEntity<Void> update(@AuthenticationPrincipal final LoginMember loginMember,
                                       @PathVariable final Long subscriptionId,
                                       @RequestBody final SubscriptionUpdateRequest request) {
        subscriptionService.update(subscriptionId, loginMember.getId(), request);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/subscriptions/{subscriptionId}")
    public ResponseEntity<Void> deleteById(@AuthenticationPrincipal final LoginMember loginMember,
                                           @PathVariable final Long subscriptionId) {
        subscriptionService.deleteByIdAndMemberId(subscriptionId, loginMember.getId());
        return ResponseEntity.noContent().build();
    }
}
