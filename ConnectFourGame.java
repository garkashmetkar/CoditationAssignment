

import java.util.Arrays;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ConnectFourGame {

    // we define characters for players (R for Red, Y for Yellow)
    private static final char[] PLAYERS = {'1', '2'};
    // colors for the players
    private static final char[] COLORS = {'r','y'};
    // number of pieces
    private int pieces;
    // dimensions for our obj
    private final int row, column;
    // board for the obj
    private final char[][] board;
    // we store last move made by a player
    private int lastCol = -1, lastTop = -1;

    public ConnectFourGame(int w, int h, int p) {
        row = w;
        column = h;
        pieces = p;
        board = new char[h][];

        // init the board will blank cell
        for (int i = 0; i < h; i++) {
            Arrays.fill(board[i] = new char[w], '-');
        }
    }

    // we use Streams to make a more concise method
    // for representing the obj
    public String toString() {
        return IntStream.range(0,  row).
                mapToObj(Integer::toString).
                collect(Collectors.joining()) +
                "\n" +
                Arrays.stream(board).
                        map(String::new).
                        collect(Collectors.joining("\n"));
    }

    // get string representation of the row containing
    // the last play of the user
    public String horizontal() {
        return new String(board[lastTop]);
    }

    // get string representation fo the col containing
    // the last play of the user
    public String vertical() {
        StringBuilder sb = new StringBuilder(column);

        for (int h = 0; h < column; h++) {
            sb.append(board[h][lastCol]);
        }

        return sb.toString();
    }

    // get string representation of the "/" diagonal
    // containing the last play of the user
    public String slashDiagonal() {
        StringBuilder sb = new StringBuilder(column);

        for (int h = 0; h < column; h++) {
            int w = lastCol + lastTop - h;

            if (0 <= w && w < row) {
                sb.append(board[h][w]);
            }
        }

        return sb.toString();
    }

    // get string representation of the "\"
    // diagonal containing the last play of the user
    public String backslashDiagonal() {
        StringBuilder sb = new StringBuilder(column);

        for (int h = 0; h < column; h++) {
            int w = lastCol - lastTop + h;

            if (0 <= w && w < row) {
                sb.append(board[h][w]);
            }
        }

        return sb.toString();
    }

    // static method checking if a substring is in str
    public static boolean contains(String str, String substring) {
        return str.indexOf(substring) >= 0;
    }

    // now, we create a method checking if last play is a winning play
    public boolean isWinningPlay() {
        if (lastCol == -1) {
            System.err.println("No move has been made yet");
            return false;
        }

        // winning streak with the last play symbol
        String streak = "";
        char sym = board[lastTop][lastCol];
        for (int i = 0; i < pieces; i++) {
            streak += sym;
        }

//        String streak1 = String.format("%c%c%c%c", sym, sym, sym, sym);

        // check if streak is in row, col,
        // diagonal or backslash diagonal
        return contains(horizontal(), streak) ||
                contains(vertical(), streak) ||
                contains(slashDiagonal(), streak) ||
                contains(backslashDiagonal(), streak);
    }

    // prompts the user for a column, repeating until a valid choice is made
    public void chooseAndDrop(char symbol, Scanner input, int player) {
        do {
            System.out.print("\nPlayer " + (player+1) + ", what column do you want to put your piece? ");
            int col = input.nextInt();

            // check if column is ok
            if (!(0 <= col && col < row)) {
                System.out.println("Column must be between 0 and " + (row - 1));
                continue;
            }

            // now we can place the symbol to the first
            // available row in the asked column
            for (int h = column - 1; h >= 0; h--) {
                if (board[h][col] == '-') {
                    board[lastTop = h][lastCol = col] = symbol;
                    return;
                }
            }

            // if column is full ==> we need to ask for a new input
            System.out.println("Column " + col + " is full.");
        } while (true);
    }

    public static void main(String[] args) {
        // we assemble all the pieces of the puzzle for
        // building our Connect Four Game
        try (Scanner input = new Scanner(System.in)) {
            // we define some variables for our game like
            // dimensions and nb max of moves
            int column = 6; int row = 7; int pieces = 0; int moves = column * row;

            //Getting inputs
            System.out.print("Enter the values saperated by spaces -r -c -p: ");
            row = input.nextInt();
            column = input.nextInt();
            pieces = input.nextInt();

            //condition for pieces
            while (pieces <= 0) {
                if (pieces == 0) {
                    System.out.println("Error, 0 pieces to connect.");
                }
                else if (pieces < 0) {
                    System.out.println("Error Negative value please enter valid value:");
                }
                System.out.print("\n Enter a positive, non-zero integer for the number of pieces to connect: ");
                pieces = input.nextInt();
            }

            // creating the ConnectFour instance
            ConnectFour obj = new ConnectFour(row, column, pieces);

            // player one choice
            System.out.print("\nPlayer one, do you want red or yellow (r or y): ");
            String color = input.next();

            // Validating user choice
            while (! (color.equals("r") || color.equals("y"))) {
                    System.out.print("Please, enter valid color: ");
                    color = input.next();
            }

            
            System.out.println("Use 0-" + (row - 1) + " to choose a column");
            // we display initial obj
            System.out.println(obj);

            if (color.equals("y")) {
                COLORS[0] = 'y';
                COLORS[1] = 'r';
            }

            // we iterate until max nb moves be reached
            // to change player turn at each iteration
            for (int player = 0; moves-- > 0; player = 1 - player) {
                // symbol for current player
                char symbol = COLORS[player];

                // Choose a column
                obj.chooseAndDrop(symbol, input, player);

                // Displaying the obj
                System.out.println(obj);

                // check player won If not,
                // If continue or else message displayed
                if (obj.isWinningPlay()) {
                    System.out.println("\nPlayer " + (player+1) + " wins!");
                    System.out.println("\nDo you want to play again (0-no, 1-yes)? ");
                    if(input.nextInt() == 0) {
                        return;
                    }
                    else {
                        System.out.println("\n Play again,");
                        player = 1;
                        obj = new ConnectFour(row, column, pieces);
                    }
                }
            }

            System.out.println("Game over");
        }
    }

}