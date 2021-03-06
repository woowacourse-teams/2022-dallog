package com.allog.dallog.subscription.controller;

import com.allog.dallog.auth.dto.LoginMember;
import com.allog.dallog.auth.support.AuthenticationPrincipal;
import com.allog.dallog.subscription.dto.request.SubscriptionCreateRequest;
import com.allog.dallog.subscription.dto.response.SubscriptionResponse;
import com.allog.dallog.subscription.dto.response.SubscriptionsResponse;
import com.allog.dallog.subscription.service.SubscriptionService;
import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    public SubscriptionController(final SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }

    @PostMapping("/api/members/me/categories/{categoryId}/subscriptions")
    public ResponseEntity<SubscriptionResponse> save(@AuthenticationPrincipal final LoginMember loginMember,
                                                     @PathVariable final Long categoryId,
                                                     @RequestBody final SubscriptionCreateRequest request) {
        SubscriptionResponse response = subscriptionService.save(loginMember.getId(), categoryId, request);
        return ResponseEntity.created(
                URI.create("/api/members/me/" + categoryId + "/subscriptions/" + response.getId())).body(response);
    }

    @GetMapping("/api/members/me/subscriptions")
    public ResponseEntity<SubscriptionsResponse> findMine(@AuthenticationPrincipal final LoginMember loginMember) {
        SubscriptionsResponse response = subscriptionService.findByMemberId(loginMember.getId());
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/api/members/me/subscriptions/{subscriptionId}")
    public ResponseEntity<Void> deleteById(@AuthenticationPrincipal final LoginMember loginMember,
                                           @PathVariable final Long subscriptionId) {
        subscriptionService.deleteByIdAndMemberId(subscriptionId, loginMember.getId());
        return ResponseEntity.noContent().build();
    }
}
