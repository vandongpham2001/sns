package com.dongpv.sns.identity.repository.httpclient;

import com.dongpv.sns.identity.configuration.AuthenticationRequestInterceptor;
import com.dongpv.sns.identity.dto.request.ApiResponse;
import com.dongpv.sns.identity.dto.request.ProfileCreationRequest;
import com.dongpv.sns.identity.dto.response.UserProfileResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
        name = "${app.name.profile}",
        url = "${app.services.profile}",
        configuration = {AuthenticationRequestInterceptor.class})
public interface ProfileClient {
    @PostMapping(value = "/internal/users", produces = MediaType.APPLICATION_JSON_VALUE)
    ApiResponse<UserProfileResponse> createProfile(@RequestBody ProfileCreationRequest request);
}
