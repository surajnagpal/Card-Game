package main;

public class Card {
    // Declare a private final variable to store the card's value.
    private final int value;

    // Constructor to initialize the Card object with a given value.
        public Card(int value) {
        if (value < 0) {
            // If the value is negative, throw an IllegalArgumentException with a descriptive message.
            throw new IllegalArgumentException("Card value cannot be negative");
        }
        // Assign the provided value to the instance variable 'value'.
        this.value = value;
    }

    // Getter method to retrieve the value of the card.
    public int getValue() {
        return value;
    }

    // Override the toString() method to provide a string representation of the Card object.
    @Override
    public String toString() {
        // Return the string representation of the card's value.
        return String.valueOf(value);
    }
}