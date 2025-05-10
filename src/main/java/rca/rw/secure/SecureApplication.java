package rca.rw.secure;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import lombok.RequiredArgsConstructor;


import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@EnableCaching
@RequiredArgsConstructor
@SpringBootApplication
@EnableJpaRepositories
public class SecureApplication {

	public static void main(String[] args) {
		SpringApplication.run(SecureApplication.class, args);
	}
}