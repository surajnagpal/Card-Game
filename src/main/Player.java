package main;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

public class Player implements Runnable {
    private final int prefValue;
    public final List<Card> hand;
    private final int playerId;
    private final CardDeck leftDeck;
    private final CardDeck rightDeck;
    private final ReentrantLock playerLock = new ReentrantLock();
    private final String outputFile;
    private final CardGame game;

    public Player(int playerId, CardDeck leftDeck, CardDeck rightDeck, 
                 int prefValue, List<Card> initialHand, CardGame game) {
        this.playerId = playerId;
        this.leftDeck = leftDeck;
        this.rightDeck = rightDeck;
        this.prefValue = prefValue;
        this.hand = new ArrayList<>(initialHand);
        this.outputFile = "player" + playerId + "_output.txt";
        this.game = game;
        
        // Create new output file or clear existing one
        try (FileWriter writer = new FileWriter(outputFile, false)) {
            // Just creating/clearing the file
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addToHand(Card card) {
        hand.add(card);
    }

    public int getId() {
        return playerId;
    }

    public boolean hasWinningHand() {
        if (hand.size() != 4) return false;
        int value = hand.get(0).getValue();
        return hand.stream().allMatch(card -> card.getValue() == value);
    }

    private void logAction(String action) {
        try (FileWriter writer = new FileWriter(outputFile, true)) {
            writer.write(action + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String formatHand() {
        StringBuilder handStr = new StringBuilder();
        for (int i = 0; i < hand.size(); i++) {
            handStr.append(hand.get(i).getValue());
            if (i < hand.size() - 1) {
                handStr.append(" ");
            }
        }
        return handStr.toString();
    }

    public void playTurn() {
        playerLock.lock();
        try {
            // Draw card from left deck
            Card drawn = leftDeck.drawCard();
            if (drawn != null) {
                hand.add(drawn);
                logAction("player " + playerId + " draws a " + drawn.getValue() + 
                         " from deck " + leftDeck.getDeckNumber());

                // Choose card to discard (non-preferred value if possible)
                Card discard = chooseCardToDiscard();
                hand.remove(discard);
                rightDeck.addCard(discard);
                logAction("player " + playerId + " discards a " + discard.getValue() + 
                         " to deck " + rightDeck.getDeckNumber());
                logAction("player " + playerId + " current hand is " + formatHand());
            }
        } finally {
            playerLock.unlock();
        }
    }

    public Card chooseCardToDiscard() {
        // First try to discard a non-preferred card
        for (Card card : hand) {
            if (card.getValue() != prefValue) {
                return card;
            }
        }
        // If all cards are preferred, discard the first one
        return hand.get(0);
    }

    @Override
    public void run() {
        // Log initial hand
        logAction("player " + playerId + " initial hand " + formatHand());

        // Check for initial winning hand
        if (hasWinningHand()) {
            logAction("player " + playerId + " wins");
            logAction("player " + playerId + " exits");
            logAction("player " + playerId + " final hand: " + formatHand());
            game.notifyWin(this);
            return;
        }

        // Main game loop
        while (!Thread.currentThread().isInterrupted()) {
            playTurn();
            if (hasWinningHand()) {
                logAction("player " + playerId + " wins");
                logAction("player " + playerId + " exits");
                logAction("player " + playerId + " final hand: " + formatHand());
                game.notifyWin(this);
                break;
            }
            
            // Small delay to prevent CPU overload
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }

        // If we got here without winning, another player must have won
        if (!hasWinningHand()) {
            logAction("player " + playerId + " has been notified someone has won");
            logAction("player " + playerId + " exits");
            logAction("player " + playerId + " final hand: " + formatHand());
        }
    } // Correctly closing the run() method
} // Correctly closing the Player class



// public void play(){
// //Need a time limit to make sure that the player doesn't hold onto a card indefinitely
// //Should have a method call here to make sure non-preffered cards are within the limit, then proceed to add and remove card
// // return;
// }

// public String toString(){
// return "";
// }


