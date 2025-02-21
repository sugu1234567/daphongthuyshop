package vn.sugu.daphongthuyshop.configuration;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import vn.sugu.daphongthuyshop.entity.User;
import vn.sugu.daphongthuyshop.enums.Role;
import vn.sugu.daphongthuyshop.repository.UserRepository;

import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ApplicationInitConfig {

    PasswordEncoder passwordEncoder;

    @Bean
    ApplicationRunner applicationRunner(UserRepository userRepository) {
        return args -> {
            if (userRepository.findByEmail("admin@gmail.com").isEmpty()) {
                User user = User.builder()
                        .fullName("admin")
                        .password(passwordEncoder.encode("admin"))
                        .email("admin@gmail.com")
                        .role(Role.ADMIN)
                        .build();

                userRepository.save(user);
                log.warn("Admin user has been created with default password: 'admin'. Please change it immediately.");
            }
        };
    }
}
