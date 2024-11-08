import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.internal.verification.VerificationModeFactory.times;

@ExtendWith(MockitoExtension.class)
class HorseTest {

    Horse testHorse_3param;
    Horse testHorse_2param;

    @BeforeEach
    void setUp() {
        testHorse_3param = new Horse ("Плотва", 2.6, 11);
        testHorse_2param = new Horse ("Плотва", 2.6);
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "\u0009", "\u000B", "\u000C", "\u0020", "\u1680", "\u2000",
            "\u2001", "\u2002", "\u2003", "\u2004", "\u2005", "\u2006", "\u2008", "\u2009",
            "\u200A", "\u2028", "\u2029", "\u205F", "\u3000"})
    void horseConstructor (String name) {
        Throwable throwable_null = assertThrows(IllegalArgumentException.class, () -> new Horse(null, 0));
        assertEquals("Name cannot be null.", throwable_null.getMessage());
        Throwable throwable_blank = assertThrows(IllegalArgumentException.class, () -> new Horse(name, 0));
        assertEquals("Name cannot be blank.", throwable_blank.getMessage());
        Throwable throwable_speed = assertThrows(IllegalArgumentException.class, () -> new Horse("Плотва", -1));
        assertEquals("Speed cannot be negative.", throwable_speed.getMessage());
        Throwable throwable_distance = assertThrows(IllegalArgumentException.class, () -> new Horse("Плотва", 0, -1));
        assertEquals("Distance cannot be negative.", throwable_distance.getMessage());
    }

    @Test
    void getName() {
        assertEquals("Плотва", testHorse_3param.getName());
    }

    @Test
    void getSpeed() {
        assertEquals(2.6, testHorse_3param.getSpeed());
    }

    @Test
    void getDistance() {
        assertEquals(11, testHorse_3param.getDistance());
        assertEquals(0, testHorse_2param.getDistance());
    }

    @ParameterizedTest
    @CsvSource({"0.2, 0.9, 0.3"})
    void move(double param_1, double param_2, double random) {
        try (MockedStatic<Horse> mockStaticMethod = mockStatic(Horse.class)) {
            mockStaticMethod.when(() -> Horse.getRandomDouble(param_1, param_2)).thenReturn(random);
            double distance = testHorse_2param.getDistance();
            testHorse_2param.move();
            mockStaticMethod.verify(() -> Horse.getRandomDouble(0.2, 0.9), times(1));
            assertEquals(testHorse_2param.getDistance(), distance + testHorse_2param.getSpeed() * Horse.getRandomDouble(param_1, param_2));
        }
    }
}