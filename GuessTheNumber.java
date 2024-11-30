import java.util.Scanner;
import java.util.Random;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;

public class GuessTheNumber {
    private int targetNumber; // Target number to guess
    private int attempts; // Number of attempts made by the player
    private int maxAttempts; // Maximum number of attempts based on difficulty
    private boolean guessedCorrectly; // Flag to track if the number is guessed
    private static Scanner scanner; // Scanner for user input
    private Random random; // Random object for generating the target number
    private static int bestScore = Integer.MAX_VALUE; // Track the best score across games

    // Constructor to initialize the scanner and random generator
    public GuessTheNumber() {
        scanner = new Scanner(System.in);
        random = new Random();
    }

    // startGame method to initialize a new game
    public void startGame(int difficultyLevel) {
        maxAttempts = 10 - difficultyLevel; // Adjust max attempts based on difficulty
        targetNumber = random.nextInt(100) + 1; // Generate a random number between 1 and 100
        attempts = 0; // Reset attempts for the new game
        guessedCorrectly = false; // Reset guessed status

        System.out.println("Welcome to Guess the Number!");
        System.out.println("Try to guess the number between 1 and 100.");
    }

    // makeGuess method to process the player's guess
    public void makeGuess(int guess) {
        attempts++; // Increment attempt count

        // Check if the guess is correct or not
        if (guess == targetNumber) {
            System.out.println("Congratulations! You guessed the number in " + attempts + " attempts.");
            guessedCorrectly = true;

            // Update best score if the current attempt count is better
            if (attempts < bestScore) {
                bestScore = attempts; 
            }
        } else if (guess < targetNumber) {
            System.out.println("Too low! Try again.");
        } else {
            System.out.println("Too high! Try again.");
        }
    }

    // endGame method to finalize the game and print/write results
    public void endGame() {
        String result;

        // Record whether the user won or lost
        if (guessedCorrectly) {
            result = "Won in " + attempts + " attempts!";
        } else {
            result = "Lost. The number was: " + targetNumber;
        }

        // Write the game result to a file for record-keeping
        writeResultToFile(result);

        // Display the result on the console
        System.out.println(result);

        // Show the best score achieved
        System.out.println("Your best score is " + bestScore + " attempts.");
    }

    // writeResultToFile method to log results to a file
    private void writeResultToFile(String result) {
        try (FileWriter writer = new FileWriter("game_results.txt", true)) {
            writer.write(result + "\n"); // Append the result to the file
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }
    }

    // readPreviousResults method to display previous game results from the file
    public static void readPreviousResults() {
        try (FileReader reader = new FileReader("game_results.txt")) {
            int character;
            while ((character = reader.read()) != -1) {
                System.out.print((char) character); // Print each character from the file
            }
            System.out.println();
        } catch (IOException e) {
            System.out.println("Error reading from file: " + e.getMessage());
        }
    }

    // chooseDifficulty method to prompt the user to select a difficulty level
    public static int chooseDifficulty() {
        int difficulty = 0;
        boolean validInput = false;

        // Loop until valid input is provided
        while (!validInput) {
            System.out.println("Choose your difficulty level (1-3): ");
            System.out.println("1. Easy (10 attempts)");
            System.out.println("2. Medium (8 attempts)");
            System.out.println("3. Hard (5 attempts)");

            // Error handling for non-integer input
            if (scanner.hasNextInt()) {
                difficulty = scanner.nextInt();
                if (difficulty >= 1 && difficulty <= 3) {
                    validInput = true; // Valid input, exit loop
                } else {
                    System.out.println("Invalid input! Please choose a level between 1 and 3.");
                }
            } else {
                System.out.println("Invalid input! Please enter a number between 1 and 3.");
                scanner.next(); // Clear the invalid input
            }
        }
        return difficulty; // Return the valid difficulty level
    }

    public static void main(String[] args) {
        boolean playAgain = true;

        // Display previous game results before starting a new game
        System.out.println("Previous game results:");
        readPreviousResults();

        // Loop to ask if the player wants to play again
        while (playAgain) {
            GuessTheNumber game = new GuessTheNumber(); // Create a new game instance

            // Get the difficulty level from the user
            int difficulty = chooseDifficulty();

            // Start the game with the chosen difficulty
            game.startGame(difficulty);

            // Game loop: Allow the player to make guesses
            while (game.attempts < game.maxAttempts && !game.guessedCorrectly) {
                System.out.print("Enter your guess: ");
                
                // Error handling for non-integer input
                while (!scanner.hasNextInt()) {
                    System.out.println("Invalid input! Please enter a valid number.");
                    scanner.next(); // Clear invalid input
                }
                int userGuess = scanner.nextInt(); // Read the player's guess
                game.makeGuess(userGuess); // Process the guess
            }

            // End the game and show the results
            game.endGame();

            // Ask if the player wants to play again
            System.out.print("Do you want to play again? (yes/no): ");
            String response = scanner.next().trim().toLowerCase();

            // Handle response for playing again
            if (response.equals("no")) {
                playAgain = false;
                System.out.println("Thank you for playing! Goodbye.");
            } else if (!response.equals("yes")) {
                System.out.println("Invalid response. Assuming you want to play again.");
            } else {
                System.out.println("Starting a new game...");
            }
        }
    }
}
