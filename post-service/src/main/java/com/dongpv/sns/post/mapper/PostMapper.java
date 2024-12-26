package com.dongpv.sns.post.mapper;

import com.dongpv.sns.post.dto.response.PostResponse;
import com.dongpv.sns.post.entity.Post;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PostMapper {
    PostResponse toPostResponse(Post post);
}
