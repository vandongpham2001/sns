package com.dongpv.sns.identity.configuration;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import static com.dongpv.sns.identity.constant.CommonConstant.AUTHORIZATION;

@Slf4j
public class AuthenticationRequestInterceptor implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate template) {
        ServletRequestAttributes servletRequestAttributes =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        var authHeader = servletRequestAttributes.getRequest().getHeader(AUTHORIZATION);

        log.info("Header: {}", authHeader);
        if (StringUtils.hasText(authHeader)) template.header(AUTHORIZATION, authHeader);
    }
}
