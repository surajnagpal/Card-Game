package main;


import java.util.List;

public class Player{

private CardDeck deck;
private int prefferedValue;
private List<Card> hand;
private int id;

public Player(int id){
this.id = id;
}

public Card drawCard(){
return (Card) hand;
}

public Card discardCard(){
return (Card) hand;
}

public boolean checkWin(){
return true;
}

public void play(){
// return;
}

public String toString(){
return "";
}


}

