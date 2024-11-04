package Software;

public class Card {
    private final int value;
    
    public Card(int value) {
        if (value < 0) {
            throw new IllegalArgumentException("Card value cannot be negative");
        }
        this.value = value;
    }
    
    public int getValue() {
        return value;
    }
    
    @Override
    public String toString() {
        return String.valueOf(value);
    }
}