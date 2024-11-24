package main;


import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

public class Player implements Runnable{


private final int prefferedValue;
private final List<Card> hand;
private final int playerId;
private final CardDeck leftDeck;
private final CardDeck rightDeck;
private final ReentrantLock lock = new ReentrantLock();
private final String outputFile;

public Player(int playerId, CardDeck leftDeck, CardDeck rightDeck, int prefferedValue, List<Card> initialHand){
this.playerId = playerId;
this.leftDeck = leftDeck;
this.rightDeck = rightDeck;
this.prefferedValue = prefferedValue;
this.hand = initialHand;
this.outputFile = "player" + playerId + "_output.txt";
}

public int getId(){
  return playerId;
}

private boolean hasWinningHand(){
  int value = hand.get(0).getValue();
  return hand.stream().allMatch(card -> card.getValue() == value);
}

private void logAction(String action){
  try(FileWriter writer = new FileWriter(outputFile, true)){
    writer.write(action + "\n");
  } catch(IOException e){
      e.printStackTrace();
  }
}

public void playTurn(){
  lock.lock();
  try{
    Card drawn = leftDeck.drawCard();
    if(drawn != null){
        hand.add(drawn);
        logAction("player " + playerId + " draws a " + drawn + " from deck " + leftDeck.getDeckNumber());
    }

    Card discard = hand.stream().filter(card -> card.getValue() != prefferedValue).findFirst().orElse(hand.get(0));
    hand.remove(discard);
    rightDeck.addCard(discard);
    logAction("player " + playerId + " discards a " + discard + " to deck " + rightDeck.getDeckNumber());
            logAction("player " + playerId + " current hand is " + hand);
  } finally {
    lock.unlock();
  }
}

@Override
public void run() {
    logAction("player " + playerId + " initial hand " + hand);
    while (!Thread.currentThread().isInterrupted()) {
        playTurn();
        if (hasWinningHand()) {
            logAction("player " + playerId + " wins");
            break;
        }
    }
    logAction("player " + playerId + " exits");
}

// public Card drawCard(){
// return (Card) hand;
// }

// public Card discardCard(){
// return (Card) hand;
// }

// public boolean checkWin(){
//   if(hand != null){
//     int counter = 1;
//     for(int i = 0; i<=3; i++)
//     {
//       if(hand.get(i).equals(playerId)){
//           counter++;
//       }
//     }
//     if(counter == 4){
//       return true;
//     }
//     return false;
  
//   }
// return false;
// }


public void play(){
//Need a time limit to make sure that the player doesn't hold onto a card indefinitely
//Should have a method call here to make sure non-preffered cards are within the limit, then proceed to add and remove card
// return;
}

public String toString(){
return "";
}



}

