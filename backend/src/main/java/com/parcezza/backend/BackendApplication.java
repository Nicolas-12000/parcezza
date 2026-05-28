package com.parcezza.backend;

import io.github.cdimascio.dotenv.Dotenv;
import io.github.cdimascio.dotenv.DotenvEntry;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
public class BackendApplication {

	// Clase principal de la aplicación Spring Boot
	// Ejecuta el contexto de Spring y carga variables desde .env si existen
	public static void main(String[] args) {
		loadDotenvIntoSystemProperties();
		SpringApplication.run(BackendApplication.class, args);
	}

	private static void loadDotenvIntoSystemProperties() {
		Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();
		for (DotenvEntry entry : dotenv.entries()) {
			String key = entry.getKey();
			if (System.getProperty(key) == null && System.getenv(key) == null) {
				System.setProperty(key, entry.getValue());
			}
		}
	}

}
