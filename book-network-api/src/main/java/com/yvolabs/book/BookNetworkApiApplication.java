package com.yvolabs.book;

import com.yvolabs.book.common.BaseEntity;
import com.yvolabs.book.config.ApplicationAuditAware;
import com.yvolabs.book.config.BeansConfig;
import com.yvolabs.book.role.Role;
import com.yvolabs.book.role.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;

import static com.yvolabs.book.enums.RoleName.USER;

/**
 * @apiNote
 * For this: @EnableJpaAuditing(auditorAwareRef = "auditorAware") the bean name declared in BeanConfig.Class <br>
 * "ApplicationAuditAware.Class" implements Spring's "AuditAware" that allows you to provide the current auditor (usually the current user)
 * to automatically populate auditing fields like createdBy and lastModifiedBy in your entities,which we added in the BaseEntity
 *
 * @see ApplicationAuditAware ApplicationAuditAware
 * @see BaseEntity BaseEntity
 * @see BeansConfig Bean Declaration
 */

@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
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
