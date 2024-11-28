package main;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Random;

public class CardGame {
    private final List<Player> players = new ArrayList<>();
    private final List<CardDeck> decks = new ArrayList<>();
    private final List<Card> cardPack = new ArrayList<>();
    private static final Random RANDOM = new Random();
    private final int n;
    private volatile boolean gameEnded = false;

    public CardGame(int n, List<Card> cardPack) {
        this.n = n;
        this.cardPack.addAll(cardPack);
        
        // Initialize decks
        for (int i = 1; i <= n; i++) {
            decks.add(new CardDeck(i));
        }
        
        // Initialize players with correct decks and preferred values
        for (int i = 1; i <= n; i++) {
            CardDeck leftDeck = decks.get((i - 1) % n);
            CardDeck rightDeck = decks.get(i % n);
            players.add(new Player(i, leftDeck, rightDeck, i, new ArrayList<>(), this));
        }
    }

    public void distributeHands() {
        // Distribute 4 cards to each player
        int playerIndex = 0;
        for (int i = 0; i < 4 * n; i++) {
            players.get(playerIndex).addToHand(cardPack.remove(0));
            playerIndex = (playerIndex + 1) % n;
        }

        // Distribute remaining cards to decks
        int deckIndex = 0;
        while (!cardPack.isEmpty()) {
            decks.get(deckIndex).addCard(cardPack.remove(0));
            deckIndex = (deckIndex + 1) % n;
        }
    }

    public synchronized void notifyWin(Player winner) {
        if (!gameEnded) {
            gameEnded = true;
            System.out.println("player " + winner.getId() + " wins");
            // Interrupt all player threads
            players.forEach(player -> Thread.currentThread().interrupt());
        }
    }

    public void startGame() {
        distributeHands();
        
        // Check for initial winning hands
        for (Player player : players) {
            if (player.hasWinningHand()) {
                notifyWin(player);
                writeDeckStates();
                return;
            }
        }

        // Start player threads
        List<Thread> threads = new ArrayList<>();
        for (Player player : players) {
            Thread t = new Thread(player);
            threads.add(t);
            t.start();
        }

        // Wait for game to end
        for (Thread t : threads) {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // Write final deck states
        writeDeckStates();
    }

    private void writeDeckStates() {
        decks.forEach(deck -> {
            try (FileWriter writer = new FileWriter("deck" + deck.getDeckNumber() + "_output.txt")) {
                writer.write(deck.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    // Method to generate a random pack file
    public static void generateRandomPack(int n, String fileName) throws IOException {
        int totalCards = 8 * n;
        List<Integer> cardValues = new ArrayList<>();

        // Generate random card values
        for (int i = 0; i < totalCards; i++) {
            cardValues.add(RANDOM.nextInt(2 * n)); // Allow values to exceed 'n'
        }

        // Write values to a file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            for (int value : cardValues) {
                writer.write(value + "\n");
            }
        }
        System.out.println("Random pack generated and saved to " + fileName);
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
    
        System.out.print("Please enter the number of players: ");
        int n = scanner.nextInt();
        scanner.nextLine(); // Consume the newline character
    
        System.out.print("Please enter location of pack to load (or type 'generate' to create a new pack): ");
        String packFile = scanner.nextLine();
    
        List<Card> cardPack = new ArrayList<>();
    
        // Check if the user wants to generate a new pack
        if (packFile.equalsIgnoreCase("generate")) {
            String generatedFileName = "random_pack.txt";
            try {
                generateRandomPack(n, generatedFileName);
                packFile = generatedFileName;
            } catch (IOException e) {
                System.out.println("Failed to generate random pack: " + e.getMessage());
                return;
            }
        }
    
        // Read the pack file
        try (Scanner fileScanner = new Scanner(new File(packFile))) {
            while (fileScanner.hasNextInt()) {
                cardPack.add(new Card(fileScanner.nextInt()));
            }
        } catch (IOException e) {
            System.out.println("Invalid pack file. Please try again.");
            return;
        }
    
        // Validate the pack
        if (cardPack.size() != 8 * n) {
            System.out.println("Pack must contain exactly 8n cards.");
            return;
        }
    
        // Start the game
        CardGame game = new CardGame(n, cardPack);
        game.startGame(); // Ensure this line is correct and has no invalid characters
    
    } // Properly close the main method
    
}

    //CardGame main prints out: 
    //"Please enter the number of players:" (via command line)
    //"Please enter location of pack to load:" (via command line)
    //then the CardGame creates the appropriate pack, by creating 8n cards, and putting this into a plain text file
    //for the pack: each row contains a single non-neg int. value, having 8n rows