package com.dongpv.sns.identity.mapper;

import com.dongpv.sns.identity.dto.request.RoleRequest;
import com.dongpv.sns.identity.dto.response.RoleResponse;
import com.dongpv.sns.identity.entity.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    @Mapping(target = "permissions", ignore = true)
    Role toRole(RoleRequest request);

    RoleResponse toRoleResponse(Role role);
}
