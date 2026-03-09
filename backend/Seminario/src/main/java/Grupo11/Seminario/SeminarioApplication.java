package Grupo11.Seminario;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SeminarioApplication {

	public static void main(String[] args) {
		
		
		SpringApplication.run(SeminarioApplication.class, args);
	}

}
