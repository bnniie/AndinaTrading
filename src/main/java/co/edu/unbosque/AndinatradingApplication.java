package co.edu.unbosque;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan(basePackages = "co.edu.unbosque.model.entity")
public class AndinatradingApplication {

	public static void main(String[] args) {
		SpringApplication.run(AndinatradingApplication.class, args);
	}

}
