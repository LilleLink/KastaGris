import java.util.Random;
import java.util.Scanner;

import static java.lang.System.*;

/*
 * The Pig game
 * See http://en.wikipedia.org/wiki/Pig_%28dice_game%29
 *
 */
public class Pig {

    public static void main(String[] args) {
        new Pig().program();
    }

    // The only allowed instance variables (i.e. declared outside any method)
    // Accessible from any method
    final Scanner sc = new Scanner(in);
    final Random rand = new Random();

    void program() {
        //test();                 // <-------------- Uncomment to run tests!

        final int winPts = 20;    // Points to win (decrease if testing)
        Player[] players;         // The players (array of Player objects)
        Player current;            // Current player for round (must use)
        boolean aborted = false;   // Game aborted by player?

        /*
        players = new Player[2];   // Hard coded players, replace *last* of all with ... (see below)
        Player p1 = new Player();
        p1.name = "Olle";
        Player p2 = new Player();
        p2.name = "Fia";
        players[0] = p1;
        players[1] = p2;
        */


        welcomeMsg(winPts);
        players = getPlayers();  // ... this (method to read in all players)
        statusMsg(players);
        current = getRandomPlayer(players);   // TODO Set random player to start

        // TODO Game logic, using small step, functional decomposition
        while (true) {

            String input = getPlayerChoice(current);

            if (input.equals("q")) {

                aborted = true;
                break;

            } else if (input.equals("n")) {
                // Add to points
                current.win();

                current = next(current, players);
                statusMsg(players);

            } else if (input.equals("r")) {
                int roll = rollDice();

                if (roll == 1) {
                    current.reset();
                    current = next(current, players);
                    roundMsg(roll, current);
                } else if (roll > 1) {
                    current.roundPts += roll;
                    if (current.roundPts + current.totalPts >= winPts) {
                        break;
                    }
                    roundMsg(roll, current);
                }
            }
        }

        gameOverMsg(current, aborted);
    }

    // ---- Game logic methods --------------

    Player getRandomPlayer(Player[] players) {
        return players[rand.nextInt(players.length-1)];
    }


    Player next(Player current, Player[] players) {
        if (indexOf(current, players) == players.length-1) {
            return players[0];
        } else {
            return players[indexOf(current, players)+1];
        }
    }

    int indexOf(Player current, Player[] players) {
        for (int i = 0; i < players.length; i++) {
            if (players[i] == current) {
                return i;
            }
        }
        return -1;
    }

    int rollDice() {
        return rand.nextInt(5)+1;
    }

    // TODO

    // ---- IO methods ------------------

    void welcomeMsg(int winPoints) {
        out.println("Welcome to PIG!");
        out.println("First player to get " + winPoints + " points will win!");
        out.println("Commands are: r = roll , n = next, q = quit");
        out.println();
    }

    void statusMsg(Player[] players) {
        out.print("Points: ");
        for (int i = 0; i < players.length; i++) {
            out.print(players[i].name + " = " + players[i].totalPts + " ");
        }
        out.println();
    }

    void roundMsg(int result, Player current) {
        if (result > 1) {
            out.println("Got " + result + " running total are " + current.roundPts);
        } else {
            out.println("Got 1 lost it all!");
        }
    }

    void gameOverMsg(Player player, boolean aborted) {
        if (aborted) {
            out.println("Aborted");
        } else {
            out.println("Game over! Winner is player " + player.name + " with "
                    + (player.totalPts + player.roundPts) + " points");
        }
    }

    String getPlayerChoice(Player player) {
        out.print("Player is " + player.name + " > ");
        return sc.next();
    }


    Player[] getPlayers() {

        System.out.print("How many players? > ");
        int playerNumber = sc.nextInt();
        Player[] players = new Player[playerNumber];

        for (int i = 0; i < playerNumber; i++) {
            players[i] = new Player();
            out.print("Enter name for player "+(i+1)+" > ");
            players[i].name = sc.next();
        }
        return players;
    }

    // ---------- Class -------------
    // Class representing the concept of a player
    // Use the class to create (instantiate) Player objects
    class Player {
        String name;     // Default null
        int totalPts = 0;    // Total points for all rounds, default 0
        int roundPts = 0;    // Points for a single round, default 0

        void win() {
            this.totalPts += roundPts;
            reset();
        }

        void reset() {
            this.roundPts = 0;
        }

    }

    // ----- Testing -----------------
    // Here you run your tests i.e. call your game logic methods
    // to see that they really work (IO methods not tested here)
    void test() {
        // This is hard coded test data
        // An array of (no name) Players (probably don't need any name to test)
        //Player[] players = {new Player(), new Player(), new Player()};

        // TODO Use for testing of logcial methods (i.e. non-IO methods)
        //Player[] players = getPlayers();
        //Player current = players[0];
        //out.println(indexOf(current, players));

        exit(0);   // End program
    }
}



