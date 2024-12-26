package com.dongpv.sns.identity.mapper;

import com.dongpv.sns.identity.dto.request.ProfileCreationRequest;
import com.dongpv.sns.identity.dto.request.UserCreationRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProfileMapper {
    ProfileCreationRequest toProfileCreationRequest(UserCreationRequest request);
}
