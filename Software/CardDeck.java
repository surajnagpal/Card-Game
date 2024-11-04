package Software;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

public class CardDeck {
    private final int deckNumber;
    private final LinkedList<Card> cards = new LinkedList<>();
    private final ReentrantLock lock = new ReentrantLock();
    
    public CardDeck(int deckNumber) {
        this.deckNumber = deckNumber;
    }
    
    public void addCard(Card card) {
        lock.lock();
        try {
            cards.addLast(card);
        } finally {
            lock.unlock();
        }
    }
    
    public Card drawCard() {
        lock.lock();
        try {
            if (cards.isEmpty()) {
                return null;
            }
            return cards.removeFirst();
        } finally {
            lock.unlock();
        }
    }
    
    public List<Card> getCards() {
        lock.lock();
        try {
            return new ArrayList<>(cards);
        } finally {
            lock.unlock();
        }
    }
    
    public int getDeckNumber() {
        return deckNumber;
    }
    
    @Override
    public String toString() {
        lock.lock();
        try {
            return "deck" + deckNumber + " contents: " + cards.stream()
                .map(Card::toString)
                .reduce((a, b) -> a + " " + b)
                .orElse("");
        } finally {
            lock.unlock();
        }
    }
}
