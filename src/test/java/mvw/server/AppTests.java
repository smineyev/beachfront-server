package mvw.server;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class AppTests {

    @LocalServerPort
    private int port;
    
    @Value(value = "${spring.maxSleepTime}")
    private int maxSleepTime;

    @Autowired
    private TestRestTemplate restTemplate;	
	
	@Test
	void testGetDisplayLongTimeout() {
		var response = this.restTemplate.getForEntity(
				"http://localhost:" + port + "/getDisplay?userid=12&displayid=11&timeout="+(10*maxSleepTime),
                String.class);
		assertEquals(HttpStatus.OK, response.getStatusCode());
	}

	@Test
	void testGetDisplayShortTimeout() {
		var response = this.restTemplate.getForEntity(
				"http://localhost:" + port + "/getDisplay?userid=12&displayid=11&timeout=1",
				String.class);
		assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
	}
	
	@Test
	void testGetVideoLongTimeout() {
		var response = this.restTemplate.getForEntity(
				"http://localhost:" + port + "/getVideo?userid=12&videoid=11&timeout="+(10*maxSleepTime),
				String.class);
		assertEquals(HttpStatus.OK, response.getStatusCode());
	}
	
	@Test
	void testGetVideoShortTimeout() {
		var response = this.restTemplate.getForEntity(
				"http://localhost:" + port + "/getVideo?userid=12&videoid=11&timeout=1",
				String.class);
		assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
	}
	
}
