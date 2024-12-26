package com.dongpv.sns.identity.service;

import com.dongpv.sns.event.dto.NotificationEvent;
import com.dongpv.sns.identity.constant.CommonConstant;
import com.dongpv.sns.identity.constant.PredefinedRole;
import com.dongpv.sns.identity.dto.request.UserCreationRequest;
import com.dongpv.sns.identity.dto.request.UserUpdateRequest;
import com.dongpv.sns.identity.dto.response.UserResponse;
import com.dongpv.sns.identity.entity.Role;
import com.dongpv.sns.identity.entity.User;
import com.dongpv.sns.identity.exception.AppException;
import com.dongpv.sns.identity.exception.ErrorCode;
import com.dongpv.sns.identity.mapper.ProfileMapper;
import com.dongpv.sns.identity.mapper.UserMapper;
import com.dongpv.sns.identity.repository.RoleRepository;
import com.dongpv.sns.identity.repository.UserRepository;
import com.dongpv.sns.identity.repository.httpclient.ProfileClient;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserService {
    UserRepository userRepository;
    RoleRepository roleRepository;
    UserMapper userMapper;
    ProfileMapper profileMapper;
    PasswordEncoder passwordEncoder;
    ProfileClient profileClient;
    KafkaTemplate<String, Object> kafkaTemplate;

    @NonFinal
    @Value("${kafka.topic.notification-delivery}")
    private String NOTIFICATION_DELIVERY_TOPIC;

    public UserResponse createUser(UserCreationRequest request) {
        User user = userMapper.toUser(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        HashSet<Role> roles = new HashSet<>();

        roleRepository.findById(PredefinedRole.USER_ROLE).ifPresent(roles::add);

        user.setRoles(roles);
        user.setEmailVerified(false);

        try {
            user = userRepository.save(user);
        } catch (DataIntegrityViolationException exception) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }

        var profileRequest = profileMapper.toProfileCreationRequest(request);
        profileRequest.setUserId(user.getId());

        var profile = profileClient.createProfile(profileRequest);

        NotificationEvent notificationEvent = NotificationEvent.builder()
                .channel(CommonConstant.EMAIL_CHANEL)
                .recipient(request.getEmail())
                .subject("Welcome to our system")
                .body("Hello, " + request.getUsername())
                .build();

        // Publish message to kafka
        kafkaTemplate.send(NOTIFICATION_DELIVERY_TOPIC, notificationEvent);

        var userCreationResponse = userMapper.toUserResponse(user);
        userCreationResponse.setId(profile.getResult().getId());

        return userCreationResponse;
    }

    public UserResponse getMyInfo() {
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();

        User user = userRepository.findByUsername(name).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        return userMapper.toUserResponse(user);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public UserResponse updateUser(String userId, UserUpdateRequest request) {
        User user = userRepository.findById(userId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        userMapper.updateUser(user, request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        var roles = roleRepository.findAllById(request.getRoles());
        user.setRoles(new HashSet<>(roles));

        return userMapper.toUserResponse(userRepository.save(user));
    }

    @PreAuthorize("hasRole('ADMIN')")
    public void deleteUser(String userId) {
        userRepository.deleteById(userId);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<UserResponse> getUsers() {
        log.info("In method get Users");
        return userRepository.findAll().stream().map(userMapper::toUserResponse).toList();
    }

    @PreAuthorize("hasRole('ADMIN')")
    public UserResponse getUser(String id) {
        return userMapper.toUserResponse(
                userRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED)));
    }
}
