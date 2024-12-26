package com.dongpv.sns.identity.mapper;

import com.dongpv.sns.identity.dto.request.UserCreationRequest;
import com.dongpv.sns.identity.dto.request.UserUpdateRequest;
import com.dongpv.sns.identity.dto.response.UserResponse;
import com.dongpv.sns.identity.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(UserCreationRequest request);

    UserResponse toUserResponse(User user);

    @Mapping(target = "roles", ignore = true)
    void updateUser(@MappingTarget User user, UserUpdateRequest request);
}
