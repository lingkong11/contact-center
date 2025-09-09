package com.example.callcenter.infrastructure.config;

import com.example.callcenter.application.inbound.InboundRouteService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StartupDataLoader {
	@Bean
	CommandLineRunner seedDemoData(InboundRouteService service) {
		return args -> {
			if (service.list().isEmpty()) {
				service.create("1000", "QUEUE", "support_queue");
			}
		};
	}
}

