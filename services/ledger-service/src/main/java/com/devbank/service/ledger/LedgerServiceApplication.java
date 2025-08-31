package com.devbank.service.ledger;

import com.template.starter.outbox.StarterMarker;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackageClasses = {
		LedgerServiceApplication.class,
		StarterMarker.class
})
@EnableJpaRepositories(basePackageClasses = {
		LedgerServiceApplication.class,
		StarterMarker.class
})
@EntityScan(basePackageClasses = {
		LedgerServiceApplication.class,
		StarterMarker.class
})
public class LedgerServiceApplication {
	public static void main(String[] args) {
		SpringApplication.run(LedgerServiceApplication.class, args);
	}
}
