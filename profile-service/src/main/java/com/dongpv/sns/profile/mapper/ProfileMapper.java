package com.dongpv.sns.profile.mapper;


import com.dongpv.sns.profile.dto.request.CreateProfileRequest;
import com.dongpv.sns.profile.dto.response.ProfileResponse;
import com.dongpv.sns.profile.entity.Profile;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProfileMapper {
    Profile toProfile(CreateProfileRequest request);

    ProfileResponse toProfileResponse(Profile entity);
}
