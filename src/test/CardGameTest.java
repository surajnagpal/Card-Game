package test;

import main.Card;
import main.CardDeck;
import main.CardGame;
import main.Player;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CardGameTest {

    @Test
    public void testInitializationWithReflection() throws Exception {
        // Create a card pack for n = 4 (8n cards)
        List<Card> cardPack = new ArrayList<>();
        for (int i = 0; i < 32; i++) { // For n = 4, pack size = 8 * 4 = 32
            cardPack.add(new Card(i % 8)); // Simulate cards with values 0-7 repeated
        }

        // Initialize the game
        CardGame game = new CardGame(4, cardPack);

        // Validate the number of players
        Field playersField = CardGame.class.getDeclaredField("players");
        playersField.setAccessible(true);
        List<Player> players = (List<Player>) playersField.get(game);

        assertEquals(4, players.size(), "Number of players should match the input.");

        // Validate the number of decks
        Field decksField = CardGame.class.getDeclaredField("decks");
        decksField.setAccessible(true);
        List<CardDeck> decks = (List<CardDeck>) decksField.get(game);

        assertEquals(4, decks.size(), "Number of decks should match the input.");

        // Validate the card pack size
        Field cardPackField = CardGame.class.getDeclaredField("cardPack");
        cardPackField.setAccessible(true);
        List<Card> remainingPack = (List<Card>) cardPackField.get(game);

        assertTrue(remainingPack.isEmpty(), "Card pack should be empty after initialization.");

        // Validate that each deck has cards after initialization
        for (int i = 0; i < decks.size(); i++) {
            CardDeck deck = decks.get(i);
            List<Card> deckCards = deck.getCards();
            assertFalse(deckCards.isEmpty(), "Each deck should have cards after initialization.");
        }

        // Validate that players have correct initial hands
        for (Player player : players) {
            Field handField = Player.class.getDeclaredField("hand");
            handField.setAccessible(true);
            List<Card> hand = (List<Card>) handField.get(player);

            assertEquals(4, hand.size(), "Each player should have 4 cards in their hand.");
        }
    }
}
