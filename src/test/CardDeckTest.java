import org.junit.jupiter.api.Test;  // Using JUnit 5
import static org.junit.jupiter.api.Assertions.*;

public class CardDeckTest {
    
    @Test
    void testThreadSafeDrawCard() {
        // Test concurrent access to drawCard()
    }
    
    @Test
    void testThreadSafeAddCard() {
        // Test concurrent access to addCard()
    }
    
    @Test
    void testDrawFromEmptyDeck() {
        CardDeck deck = new CardDeck(1);
        assertNull(deck.drawCard());
    }
    
    @Test
    void testFIFOOrder() {
        // Test cards are drawn in correct order
    }
    
    @Test
    void testSortedOutput() {
        // Test toString() produces sorted output
    }
    
    // Using Reflection to test private methods
    @Test
    void testPrivateFields() {
        // Test using reflection
    }
}