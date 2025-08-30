package com.jse.manage_user.service.impl;

import com.jse.manage_user.model.entity.PhoneEntity;
import com.jse.manage_user.model.entity.UserEntity;
import com.jse.manage_user.model.request.UserRequest;
import com.jse.manage_user.model.response.UserResponse;
import com.jse.manage_user.repository.UserRepository;
import com.jse.manage_user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Autowired
    UserServiceImpl(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Override
    public Set<UserResponse> getAllUsers() {
        return userRepository.findAll().stream().map(userEntity ->
                        UserResponse
                                .builder()
                                .id(userEntity.getId())
                                .created(userEntity.getCreated())
                                .modified(userEntity.getModified())
                                .lastLogin(userEntity.getLastLogin())
                                .token(userEntity.getToken())
                                .activated(userEntity.isActivated())
                                .build())
                .collect(Collectors.toSet());
    }

    @Override
    public UserResponse getUserByEmail(String email) {
        Optional<UserEntity> user = userRepository.findByEmail(email);
        return user.map(userEntity -> UserResponse
                .builder()
                .id(userEntity.getId())
                .created(userEntity.getCreated())
                .modified(userEntity.getModified())
                .lastLogin(userEntity.getLastLogin())
                .token(userEntity.getToken())
                .activated(userEntity.isActivated())
                .build()).orElse(null);
    }

    @Override
    public boolean existsEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public UserResponse createUser(UserRequest request) {

        UserEntity userEntity = UserEntity.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(request.getPassword())
                .lastLogin(LocalDateTime.now())
                .token(UUID.randomUUID().toString())//TODO replace per JWT
                .build();

        Set<PhoneEntity> phones = request.getPhones().stream().map(phoneRequest ->
                PhoneEntity.builder()
                        .number(phoneRequest.getNumber())
                        .cityCode(phoneRequest.getCityCode())
                        .countryCode(phoneRequest.getCountryCode())
                        .user(userEntity)
                        .build()
        ).collect(Collectors.toSet());

        userEntity.setPhones(phones);

        UserEntity userEntityResponse = userRepository.save(userEntity);

        return  UserResponse
                .builder()
                .id(userEntityResponse.getId())
                .created(userEntityResponse.getCreated())
                .modified(userEntityResponse.getModified())
                .lastLogin(userEntityResponse.getLastLogin())
                .token(userEntityResponse.getToken())
                .activated(userEntityResponse.isActivated())
                .build();
    }

    @Override
    public UserResponse updateUser(String email, UserRequest request) {
        return null;
    }

    @Override
    public boolean deactivateUser(String email) {
        return false;
    }
}
