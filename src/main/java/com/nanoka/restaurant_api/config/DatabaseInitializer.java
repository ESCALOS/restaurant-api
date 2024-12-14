package com.nanoka.restaurant_api.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.nanoka.restaurant_api.user.domain.model.DocumentTypeEnum;
import com.nanoka.restaurant_api.user.domain.model.RoleEnum;
import com.nanoka.restaurant_api.user.infrastructure.adapters.output.persistence.entity.UserEntity;
import com.nanoka.restaurant_api.user.infrastructure.adapters.output.persistence.repository.UserRepository;

import jakarta.annotation.PostConstruct;

@Configuration
public class DatabaseInitializer {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private Environment env;

    @PostConstruct
    public void init() {
        if (userRepository.findAll().isEmpty()) {
            UserEntity defaultUser = UserEntity.builder()
                    .username(env.getProperty("DEFAULT_USER_USERNAME", "defaultUser"))
                    .password(new BCryptPasswordEncoder().encode(env.getProperty("DEFAULT_USER_PASSWORD", "defaultPassword")))
                    .name(env.getProperty("DEFAULT_USER_NAME", "Default Name"))
                    .documentType(DocumentTypeEnum.valueOf(env.getProperty("DEFAULT_USER_DOCUMENT_TYPE", "DNI")))
                    .documentNumber(env.getProperty("DEFAULT_USER_DOCUMENT_NUMBER", "12345678"))
                    .phone(env.getProperty("DEFAULT_USER_PHONE", "123456789"))
                    .isEnabled(true)
                    .role(RoleEnum.valueOf(env.getProperty("DEFAULT_USER_ROLE", "ADMIN")))
                    .email(env.getProperty("DEFAULT_USER_EMAIL", "default@example.com"))
                    .build();

            userRepository.save(defaultUser);
        }
    }

}
