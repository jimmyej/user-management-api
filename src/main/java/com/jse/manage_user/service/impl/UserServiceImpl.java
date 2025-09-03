package com.jse.manage_user.service.impl;

import com.jse.manage_user.exception.ResourceAlreadyExistsException;
import com.jse.manage_user.model.entity.PhoneEntity;
import com.jse.manage_user.model.entity.UserEntity;
import com.jse.manage_user.model.request.UserRequest;
import com.jse.manage_user.model.response.UserResponse;
import com.jse.manage_user.repository.UserRepository;
import com.jse.manage_user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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

    public UserResponse createUser(UserRequest request) {

        if(userRepository.existsByEmail(request.getEmail())){
            throw new ResourceAlreadyExistsException("Email already exists!");
        }

        UserEntity userEntity = UserEntity.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(request.getPassword())
                .lastLogin(LocalDateTime.now())
                .token(UUID.randomUUID().toString())
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
}
