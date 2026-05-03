package com.leonardo.orderprocessing;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@Disabled("Integration test — requires PostgreSQL and Kafka running")
@SpringBootTest
class OrderProcessingSystemApplicationTests {

	@Test
	void contextLoads() {
	}

}
