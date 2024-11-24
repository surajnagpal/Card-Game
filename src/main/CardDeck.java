package main;

// import java.util.ArrayList;
// import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class CardDeck {
    // Unique ID for this deck
    private final int deckNumber;
    
    // LinkedList to store the cards in the deck
    private final LinkedList<Card> cards = new LinkedList<>();

    // Constructor initialization
    public CardDeck(int deckNumber) {
        this.deckNumber = deckNumber;
    }

    // Getter for the deck number
    public int getDeckNumber() {
        return deckNumber;
    }

    // Returns a copy of the current list of cards in the deck
    // Synchronized to ensure thread safety when accessing the cards
    public synchronized List<Card> getCards() {
        return new LinkedList<>(cards);
    }

    // Adds a card to the end of the deck
    // Synchronized to ensure only one thread can add a card at a time
    public synchronized void addCard(Card card) {
        cards.addLast(card);
    }

    // Draws a card from the beginning of the deck
    // Returns null if the deck is empty
    // Synchronized to prevent concurrent access issues
    public synchronized Card drawCard() {
        if (cards.isEmpty()) {
            return null;
        }
        return cards.removeFirst();
    }

    // Generates a sorted string representation of the deck's contents
    // @Override
    // public synchronized String toString() {
    //     // Create a copy of the cards list and sort it
    //     List<Card> sortedCards = new ArrayList<>(cards);
    //     Collections.sort(sortedCards, (card1, card2) -> 
    //         Integer.compare(card1.getValue(), card2.getValue()));
        
    //     // Return the deck's number and its sorted contents as a string
    //     return "deck" + deckNumber + " contents: " + sortedCards;
    // }

    @Override
    public synchronized String toString() {
        return "deck" + deckNumber + " contents: " + cards.toString();
    }
}
