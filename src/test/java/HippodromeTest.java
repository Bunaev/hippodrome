import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class HippodromeTest {
    @Spy
    List<Horse> horses = new ArrayList<>();
    {
        for (int i = 0; i < 30; i++) {
            horses.add(new Horse("Лошадь №" + i, (double) i / 10, i));
        }
    }
    Horse mockHorse = Mockito.mock(Horse.class);
    @Spy
    List<Horse> mockList = new ArrayList<>(Collections.nCopies(50, mockHorse));

    @Test
    void hippodromeConstructor() {
        Throwable throwable_null = assertThrows(IllegalArgumentException.class, () -> new Hippodrome(null));
        assertEquals("Horses cannot be null.", throwable_null.getMessage());
        Throwable throwable_empty = assertThrows(IllegalArgumentException.class, () -> new Hippodrome(new ArrayList<>()));
        assertEquals("Horses cannot be empty.", throwable_empty.getMessage());
    }
    @Test
    void getHorses() {
        Hippodrome hippodrome = new Hippodrome(horses);
        for (int i = 0; i < horses.size(); i++) {
            assertSame(horses.get(i), hippodrome.getHorses().get(i));
        }
        assertEquals(horses.size(), hippodrome.getHorses().size());
    }

    @Test
    void move() {
        Hippodrome hippodrome = new Hippodrome(mockList);
        hippodrome.move();
        Mockito.verify(mockHorse, times(50)).move();
    }

    @Test
    void getWinner() {
        Hippodrome hippodrome = new Hippodrome(horses);
        assertEquals(horses.stream().max(Comparator.comparing(Horse::getDistance)).get(), hippodrome.getWinner());
    }
}