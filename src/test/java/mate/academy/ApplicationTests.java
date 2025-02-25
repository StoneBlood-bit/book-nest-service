package mate.academy;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(properties = "jwt.secret=${JWT_SECRET:default_value}")
class ApplicationTests {

    @Test
    void contextLoads() {
    }

}
