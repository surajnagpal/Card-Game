package test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class GameTest {

    @Test
    public void testGameSimulationWithValidPack() {
        CardGame game = new CardGame("valid_pack.txt");
        assertDoesNotThrow(() -> game.simulate());
    }

    @Test
    public void testGameSimulationWithInvalidPack() {
        CardGame game = new CardGame("invalid_pack.txt");
        Exception exception = assertThrows(IllegalArgumentException.class, () -> game.simulate());
        assertEquals("File format error: Invalid card value in pack file: X", exception.getMessage());
    }
}
