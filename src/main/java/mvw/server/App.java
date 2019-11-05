package mvw.server;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@SpringBootApplication
@Configuration
public class App {

	@Value(value = "${spring.maxWorkerPoolSize}")
	private int workerPoolSize;

	@Bean
	public ExecutorService threadPoolTaskExecutor() {
		ExecutorService executor = Executors.newFixedThreadPool(workerPoolSize);
		return executor;
	}

	public static void main(String[] args) {
		SpringApplication.run(App.class, args);
	}

}
