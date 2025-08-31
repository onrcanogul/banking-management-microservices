package com.devbank.service.account;

import com.template.starter.outbox.StarterMarker;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@SpringBootApplication(scanBasePackageClasses = {
		AccountServiceApplication.class,
		StarterMarker.class
})
@EnableJpaRepositories(basePackageClasses = {
		AccountServiceApplication.class,
		StarterMarker.class
})
@EntityScan(basePackageClasses = {
				AccountServiceApplication.class,
				StarterMarker.class
})
public class AccountServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(AccountServiceApplication.class, args);
	}

}
