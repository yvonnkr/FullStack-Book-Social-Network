package com.yvolabs.book;

import com.yvolabs.book.role.Role;
import com.yvolabs.book.role.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;

import static com.yvolabs.book.enums.RoleName.USER;

@SpringBootApplication
@EnableJpaAuditing
@EnableAsync
public class BookNetworkApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(BookNetworkApiApplication.class, args);
    }

    @Bean
    public CommandLineRunner runner(RoleRepository roleRepository) {
        return args -> {
            if (roleRepository.findByName(USER.getRoleName()).isEmpty()) {
                roleRepository.save(Role.builder().name(USER.getRoleName()).build());
            }
        };
    }

}
