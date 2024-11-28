package test;

import main.Card;
import main.CardDeck;
import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

public class CardDeckTest {
    private CardDeck deck;

    @Before
    public void setUp() {
        deck = new CardDeck(1);
    }

    @Test
    public void testAddAndDrawCard() {
        Card card = new Card(5);
        deck.addCard(card);

        Card drawnCard = deck.drawCard();
        assertEquals("Drawn card should match added card", 5, drawnCard.getValue());
    }

    @Test
    public void testDrawFromEmptyDeck() {
        assertNull("Drawing from empty deck should return null", deck.drawCard());
    }

    @Test
    public void testToStringFormat() {
        deck.addCard(new Card(3));
        deck.addCard(new Card(1));
        deck.addCard(new Card(2));
        assertEquals("deck1 contents: 1 2 3", deck.toString().trim());
    }

    @Test
    public void testThreadSafety() throws InterruptedException {
        final int numThreads = 5;
        final int numCardsPerThread = 100;
        Thread[] threads = new Thread[numThreads];

        // Multiple threads adding cards
        for (int i = 0; i < numThreads; i++) {
            threads[i] = new Thread(() -> {
                for (int j = 0; j < numCardsPerThread; j++) {
                    deck.addCard(new Card(j));
                }
            });
            threads[i].start();
        }

        // Wait for all threads to complete
        for (Thread thread : threads) {
            thread.join();
        }

        // Verify total number of cards
        assertEquals("Deck should contain all added cards", 
            numThreads * numCardsPerThread, deck.getCards().size());
    }

    @Test
    public void testFIFOOrder() {
        deck.addCard(new Card(1));
        deck.addCard(new Card(2));
        deck.addCard(new Card(3));

        assertEquals("First card drawn should be 1", 1, deck.drawCard().getValue());
        assertEquals("Second card drawn should be 2", 2, deck.drawCard().getValue());
        assertEquals("Third card drawn should be 3", 3, deck.drawCard().getValue());
    }
}