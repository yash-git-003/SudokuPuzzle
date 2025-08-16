import java.util.*;

public class SudokuPuzzle {
    private static final int SIZE = 9;
    private static int[][] board = new int[SIZE][SIZE];
    private static int[][] solution = new int[SIZE][SIZE];
    private static boolean[][] isMutable = new boolean[SIZE][SIZE];
    private static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("Welcome to Sudoku Solver!");

        generateFullSolution(board);      
        copyBoard(board, solution);      
        removeCells(board, 40);        
        initializeMutableFlags();        

        printBoard(board);               

        while (true) {
            System.out.println("\nOptions: \n1. Enter value \n2. Solve \n3. Exit");
            System.out.print("Choose option: ");

            int option;
            try {
                option = Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
                continue;
            }

            switch (option) {
                case 1:
                    enterValue();
                    break;
                case 2:
                    copyBoard(solution, board);
                    System.out.println("Puzzle solved:");
                    printBoard(board);
                    break;
                case 3:
                    System.out.println("Exiting...");
                    sc.close();  
                    return;
                default:
                    System.out.println("Invalid option. Please choose 1-3.");
            }
        }
    }

    private static void printBoard(int[][] b) {
        System.out.println("-------------------------");
        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                if (c % 3 == 0) System.out.print("| ");
                System.out.print((b[r][c] == 0 ? ". " : b[r][c] + " "));
            }
            System.out.println("|");
            if ((r + 1) % 3 == 0) System.out.println("-------------------------");
        }
    }

    private static void enterValue() {
        try {
            System.out.print("Enter row (1-9): ");
            int r = Integer.parseInt(sc.nextLine()) - 1;
            System.out.print("Enter column (1-9): ");
            int c = Integer.parseInt(sc.nextLine()) - 1;
            System.out.print("Enter value (1-9): ");
            int val = Integer.parseInt(sc.nextLine());

            if (r < 0 || r >= SIZE || c < 0 || c >= SIZE || val < 1 || val > 9) {
                System.out.println("Invalid input. Please enter numbers in range 1-9.");
                return;
            }

            if (!isMutable[r][c]) {
                System.out.println("This cell is fixed and cannot be modified.");
                return;
            }

            if (!isValid(board, val, r, c)) {
                System.out.println("Conflict detected! Move not allowed.");
            } else {
                board[r][c] = val;
                printBoard(board);
            }

        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter numeric values.");
        }
    }

    private static void generateFullSolution(int[][] b) {
        solve(b);
    }

    private static void removeCells(int[][] b, int emptyCells) {
        Random rand = new Random();
        int removed = 0;
        while (removed < emptyCells) {
            int r = rand.nextInt(SIZE);
            int c = rand.nextInt(SIZE);
            if (b[r][c] != 0) {
                b[r][c] = 0;
                removed++;
            }
        }
    }

    private static void initializeMutableFlags() {
        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                isMutable[r][c] = board[r][c] == 0;
            }
        }
    }

    private static boolean solve(int[][] b) {
        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                if (b[r][c] == 0) {
                    for (int num = 1; num <= SIZE; num++) {
                        if (isValid(b, num, r, c)) {
                            b[r][c] = num;
                            if (solve(b)) return true;
                            b[r][c] = 0;
                        }
                    }
                    return false;
                }
            }
        }
        return true;
    }

    private static boolean isValid(int[][] b, int num, int r, int c) {
        for (int i = 0; i < SIZE; i++) {
            if (b[r][i] == num || b[i][c] == num) return false;
        }

        int boxRow = (r / 3) * 3;
        int boxCol = (c / 3) * 3;
        for (int i = boxRow; i < boxRow + 3; i++)
            for (int j = boxCol; j < boxCol + 3; j++)
                if (b[i][j] == num) return false;

        return true;
    }

    private static void copyBoard(int[][] src, int[][] dst) {
        for (int i = 0; i < SIZE; i++) {
            System.arraycopy(src[i], 0, dst[i], 0, SIZE);
        }
    }
}
