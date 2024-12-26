package com.dongpv.sns.profile.controller;

import org.springframework.web.bind.annotation.*;

import com.dongpv.sns.profile.dto.ApiResponse;
import com.dongpv.sns.profile.dto.request.CreateProfileRequest;
import com.dongpv.sns.profile.dto.response.ProfileResponse;
import com.dongpv.sns.profile.service.ProfileService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class InternalProfileController {
    ProfileService profileService;

    @PostMapping("/internal/users")
    ApiResponse<ProfileResponse> createProfile(@RequestBody CreateProfileRequest request) {
        return ApiResponse.<ProfileResponse>builder()
                .result(profileService.createProfile(request))
                .build();
    }

    @GetMapping("/internal/users/{userId}")
    ApiResponse<ProfileResponse> getProfile(@PathVariable String userId) {
        return ApiResponse.<ProfileResponse>builder()
                .result(profileService.getByUserId(userId))
                .build();
    }
}
