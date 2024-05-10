package com.ayed.booknetwork;

import com.ayed.booknetwork.Role.Role;
import com.ayed.booknetwork.Role.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.List;

@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
public class BookNetworkApplication {

	public static void main(String[] args) {
		SpringApplication.run(BookNetworkApplication.class, args);
	}

	@Bean
	public CommandLineRunner runner(RoleRepository roleRepository) {
		return args -> {
			if(roleRepository.findAll().isEmpty()){
				roleRepository.save(Role.builder().name("ROLE_USER").build());
				roleRepository.save(Role.builder().name("ROLE_ADMIN").build());

		}
	};
	}
}
