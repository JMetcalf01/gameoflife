import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

class LifeParser {

    static boolean[][] getBoard(String path) {
        int width, height;

        // Initialize scanner
        Scanner scanner = null;
        try {
            scanner = new Scanner(new File(path));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if (scanner == null) {
            throw new IllegalStateException("Scanner is null!");
        }

        // Get to beginning of grid
        String line = scanner.nextLine();
        while (!line.equals("BOARD GRID")) {
            line = scanner.nextLine();
        }

        // Initialize width/height/board
        line = scanner.nextLine();
        width = new Integer(line.split(" ")[1]);
        line = scanner.nextLine();
        height = new Integer(line.split(" ")[1]);
        boolean[][] board = new boolean[height][width];
        scanner.nextLine();

        // Begin parsing
        int currentRow = 0, currentCol = 0;
        while (scanner.hasNextLine()) {
            line = scanner.nextLine();

            // End
            if (line.equals("===================="))
                break;

            String[] nums = line.split(" ");
            for (String num : nums) {
                if (new Integer(num).equals(1))
                    board[currentRow][currentCol] = true;
                else if (new Integer(num).equals(0))
                    board[currentRow][currentCol] = false;
                currentCol++;
            }
            currentRow++;
            currentCol = 0;
        }

        //Return board
        return board;
    }
}
