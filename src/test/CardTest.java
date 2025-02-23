package test;

import main.Card;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CardTest {
    @Test
    void testValidCardCreation() {
        Card card = new Card(5);
        assertEquals(5, card.getValue());
    }
    
    @Test
    void testNegativeValueThrowsException() {
        assertThrows(IllegalArgumentException.class,() -> {
            new Card(-1);
        });
    }

    @Test
    public void testToString() {
        Card card = new Card(10);
        assertEquals("10", card.toString(), "toString should return value as string");
    }
}