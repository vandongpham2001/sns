package com.dongpv.sns.identity.mapper;

import com.dongpv.sns.identity.dto.request.PermissionRequest;
import com.dongpv.sns.identity.dto.response.PermissionResponse;
import com.dongpv.sns.identity.entity.Permission;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PermissionMapper {
    Permission toPermission(PermissionRequest request);

    PermissionResponse toPermissionResponse(Permission permission);
}
