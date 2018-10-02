package us.zacharymaddox.customannotations;

import java.util.concurrent.ThreadLocalRandom;

import org.springframework.stereotype.Component;

@Component
public class SomeClass {
	
	@ExecutionTimeLogged
	public void someMethod() throws InterruptedException {
		Integer sleepMills = ThreadLocalRandom.current().nextInt(10, 200);
		Thread.sleep(sleepMills);
	}

}
