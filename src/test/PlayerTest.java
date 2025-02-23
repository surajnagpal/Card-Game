package test;

import main.*;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

public class PlayerTest {

    @Test
    public void testHasWinningHand_WithWinningHand() {
        Player player = new Player(1, new CardDeck(1), new CardDeck(2), 1, 
                List.of(new Card(1), new Card(1), new Card(1), new Card(1)), 
                null); // No game object needed for this test

        assertTrue(player.hasWinningHand(), "Player should win with four cards of the same value.");
    }

    @Test
    public void testHasWinningHand_WithoutWinningHand() {
        Player player = new Player(1, new CardDeck(1), new CardDeck(2), 1, 
                List.of(new Card(1), new Card(2), new Card(3), new Card(4)), 
                null);

        assertFalse(player.hasWinningHand(), "Player should not win with different card values.");
    }

    @Test
    public void testAddToHand() {
        Player player = new Player(1, new CardDeck(1), new CardDeck(2), 1, 
                List.of(new Card(1), new Card(2), new Card(3)), 
                null);
        
        player.addToHand(new Card(4));

    //     assertEquals(4, player.hand.size(), "Player's hand should have 4 cards after adding one.");
    //     assertEquals(4, player.hand.get(3).getValue(), "The added card should have value 4.");
    // } - removed this block of code and had to use reflection to test the private field hand -- suraj
        try {
            Field handField = Player.class.getDeclaredField("hand");
            handField.setAccessible(true);
            List<Card> hand = (List<Card>) handField.get(player);
            
            assertEquals(4, hand.size(), "Player's hand should have 4 cards after adding one.");
            assertEquals(4, hand.get(3).getValue(), "The added card should have value 4");
        } catch (Exception e) {
            fail("Reflection failed: " + e.getMessage());
        }
    }

    @Test
    public void testChooseCardToDiscard_PrefersNonPreferredCard() {
        Player player = new Player(1, new CardDeck(1), new CardDeck(2), 1, 
                List.of(new Card(1), new Card(2), new Card(3), new Card(4)), 
                null);
        try{    
            Method discardMethod = Player.class.getDeclaredMethod("chooseCardToDiscard"); //added by suraj
            discardMethod.setAccessible(true); //added by suraj

            // Card discarded = player.chooseCardToDiscard();
            Card discarded = (Card) discardMethod.invoke(player); //added by suraj

            assertNotEquals(1, discarded.getValue(), "Player should discard a non-preferred card."); 
        
        } catch (Exception e) {
            fail("Reflection failed: " + e.getMessage());
        }
    }    
    @Test

    public void testChooseCardToDiscard_AllPreferredValues() {
        Player player = new Player(1, new CardDeck(1), new CardDeck(2), 1, 
                List.of(new Card(1), new Card(1), new Card(1), new Card(1)), 
                null);
        try{
            Method discardMethod = Player.class.getDeclaredMethod("chooseCardToDiscard"); //added by suraj
            discardMethod.setAccessible(true); //added by suraj
            // Card discarded = player.chooseCardToDiscard();
            Card discarded = (Card) discardMethod.invoke(player); //added by suraj

            assertEquals(1, discarded.getValue(), "Player should discard the first card if all cards are preferred.");

        } catch (Exception e) {
            fail("Reflection failed: " + e.getMessage());
        } 
    }    

    @Test
    public void testPlayTurn() {
        CardDeck leftDeck = new CardDeck(1);
        CardDeck rightDeck = new CardDeck(2);
    
        leftDeck.addCard(new Card(5));
        Player player = new Player(1, leftDeck, rightDeck, 1, 
                List.of(new Card(1), new Card(2), new Card(3), new Card(4)), 
                null);
    
        player.playTurn();
    
        try {
            Field handField = Player.class.getDeclaredField("hand");
            handField.setAccessible(true);
            List<Card> hand = (List<Card>) handField.get(player);
            
            assertEquals(4, hand.size(), "Player should still have 4 cards after playing a turn.");
            assertEquals(5, hand.get(3).getValue(), "Player should have drawn the card from the left deck.");
            assertEquals(1, rightDeck.getCards().size(), "Right deck should have one discarded card.");
        } catch (Exception e) {
            fail("Reflection failed: " + e.getMessage());
        }
    }
}
