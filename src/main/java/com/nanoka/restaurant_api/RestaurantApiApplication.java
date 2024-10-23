package com.nanoka.restaurant_api;

import com.nanoka.restaurant_api.user.domain.model.DocumentTypeEnum;
import com.nanoka.restaurant_api.user.domain.model.RoleEnum;
import com.nanoka.restaurant_api.user.infrastructure.adapters.output.persistence.entity.PermissionEntity;
import com.nanoka.restaurant_api.user.infrastructure.adapters.output.persistence.entity.RoleEntity;
import com.nanoka.restaurant_api.user.infrastructure.adapters.output.persistence.entity.UserEntity;
import com.nanoka.restaurant_api.user.infrastructure.adapters.output.persistence.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;
import java.util.Set;

@SpringBootApplication
public class RestaurantApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(RestaurantApiApplication.class, args);
	}


	@Bean
	CommandLineRunner init(UserRepository userRepository) {
		return args -> {
            new PermissionEntity();

			RoleEntity roleAdmin = RoleEntity.builder()
					.roleEnum(RoleEnum.ADMIN)
					.build();
			RoleEntity roleWaiter = RoleEntity.builder()
					.roleEnum(RoleEnum.WAITER)
					.build();
			RoleEntity roleStoreKeeper = RoleEntity.builder()
					.roleEnum(RoleEnum.STOREKEEPER)
					.build();

			UserEntity userCarlos = UserEntity.builder()
					.username("carlos")
					.password(new BCryptPasswordEncoder().encode("1234"))
					.name("Carlos Escate")
					.documentType(DocumentTypeEnum.DNI)
					.documentNumber("70821326")
					.phone("941678196")
					.isEnabled(true)
					.roles(Set.of(roleAdmin))
					.build();

			UserEntity userDaniel = UserEntity.builder()
					.username("daniel")
					.password(new BCryptPasswordEncoder().encode("5678"))
					.name("Daniel Herrera")
					.documentType(DocumentTypeEnum.DNI)
					.documentNumber("72461467")
					.phone("926852390")
					.isEnabled(true)
					.roles(Set.of(roleWaiter))
					.build();

			UserEntity userRodrigo = UserEntity.builder()
					.username("rodrigo")
					.password(new BCryptPasswordEncoder().encode("9012"))
					.name("Rodrigo Mendoza")
					.documentType(DocumentTypeEnum.DNI)
					.documentNumber("70821335")
					.phone("981683417")
					.isEnabled(true)
					.roles(Set.of(roleStoreKeeper))
					.build();

			userRepository.saveAll(List.of(userCarlos, userDaniel, userRodrigo));
		};
	}
}
