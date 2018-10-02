package us.zacharymaddox.customannotations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CustomAnnotationsApplication implements CommandLineRunner {
	
	@Autowired
	private SomeClass someClass;

	public static void main(String[] args) {
		SpringApplication.run(CustomAnnotationsApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		for (int i = 0; i < 100; i++) {
			someClass.someMethod();
		}
	}
}
