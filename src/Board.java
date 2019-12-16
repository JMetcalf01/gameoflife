import javax.swing.*;
import java.awt.*;
import java.util.Random;

class Board extends JFrame {

    private boolean[][] current, next;

    private int CELL_WIDTH;
    private int CELL_HEIGHT;

    private final int PERCENT_ALIVE_AT_START;

    private final javafx.scene.paint.Color COLOR;

    private final boolean TRIPPY;

    private final boolean RANDOM;

    private String filePath;

    private final double SECONDS;

    private Board(String filePath, boolean random, int percent, int width, int height, double delay, javafx.scene.paint.Color color) {

        this.filePath = filePath;

        // Initializes constants
        PERCENT_ALIVE_AT_START = percent;
        SECONDS = delay;
        RANDOM = random;

        // Initializes color
        if (color != null) {
            COLOR = color;
            TRIPPY = false;
        } else {
            COLOR = null;
            TRIPPY = true;
        }

        // Initializes measurements
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        CELL_WIDTH = screen.width / width;
        CELL_HEIGHT = screen.height / height;

        // Initializes arrays
        int numWidth = screen.width / CELL_WIDTH;
        int numHeight = screen.height / CELL_HEIGHT;
        current = new boolean[numWidth][numHeight];
        next = new boolean[numWidth][numHeight];

        // Initializes GUI
        initGUI();
    }

    private void initGUI() {
        setTitle("Conway's Game Of Life");
        setBackground(Color.WHITE);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(true);
        setVisible(true);
    }

    static void init(String filePath, boolean random, int percent, int width, int height, double delay, javafx.scene.paint.Color color) {
        Board board = new Board(filePath, random, percent, width, height, delay, color);
        board.start();
    }

    private void start() {
        if (RANDOM) {
            randomArray();
        } else {
            loadFromFile();
        }
        while (true) {
            iterate();
            try {
                Thread.sleep((int) (1000 * SECONDS));
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void loadFromFile() {
        if (filePath == null)
            throw new IllegalStateException("Filepath is null when it shouldn't be!");

        current = LifeParser.getBoard(filePath);
        resizeUI(current.length, current[0].length);
    }

    private void resizeUI(int width, int height) {
        // Initializes measurements
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        CELL_WIDTH = screen.width / width;
        CELL_HEIGHT = screen.height / height;

        // Initializes arrays
        int numWidth = screen.width / CELL_WIDTH;
        int numHeight = screen.height / CELL_HEIGHT;
        next = new boolean[numWidth][numHeight];
    }

    private void randomArray() {
        Random rand = new Random();
        for (int row = 0; row < current.length; row++) {
            for (int col = 0; col < current[row].length; col++) {
                if (rand.nextInt(100) + 1 < PERCENT_ALIVE_AT_START) {
                    current[row][col] = true;
                }
            }
        }
    }

    private void iterate() {
        // Show current board using current array
        paint(getGraphics());

        // Update the next array
        for (int row = 0; row < current.length; row++) {
            for (int col = 0; col < current[row].length; col++) {
                runTests(row, col);
            }
        }

        // Update current using next
        for (int row = 0; row < current.length; row++) {
            System.arraycopy(next[row], 0, current[row], 0, current[row].length);
        }
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        for (int row = 0; row < current.length; row++) {
            for (int col = 0; col < current[row].length; col++) {
                if (current[row][col]) {
                    g.setColor(getColor());
                } else {
                    g.setColor(Color.WHITE);
                }
                g.fillRect(row * CELL_WIDTH, col * CELL_HEIGHT, CELL_WIDTH, CELL_HEIGHT);
            }
        }
    }

    private void runTests(int row, int col) {
        if (cellAlive(row, col) && returnAliveNeighbors(row, col) < 2) {
            next[row][col] = false;
        } else if (cellAlive(row, col) && returnAliveNeighbors(row, col) > 3) {
            next[row][col] = false;
        } else if (cellDead(row, col) && returnAliveNeighbors(row, col) == 3) {
            next[row][col] = true;
        }
    }

    private int returnAliveNeighbors(int row, int col) {
        int neighbors = 0;

        int maxRow = current.length - 1;
        int maxCol = current[0].length - 1;

        if (row != 0 && col != 0 && current[row - 1][col - 1])
            neighbors++;                                                         // Above and to the left
        else if (row == 0 && col == 0 && current[maxRow][maxCol])
            neighbors++;

        if (row != 0 && current[row - 1][col])
            neighbors++;                                                         // Directly above
        else if (row == 0 && current[maxRow][col])
            neighbors++;

        if (row != 0 && col != maxCol && current[row - 1][col + 1])
            neighbors++;                                                         // Above and to the right
        else if (row == 0 && col == maxCol && current[maxRow][0])
            neighbors++;

        if (col != maxCol && current[row][col + 1])
            neighbors++;                                                         // Directly right
        else if (col == maxCol && current[row][0])
            neighbors++;

        if (row != maxRow && col != maxCol && current[row + 1][col + 1])
            neighbors++;                                                         // Below and to the right
        else if (row == maxRow && col == maxCol && current[0][0])
            neighbors++;

        if (row != maxRow && current[row + 1][col])
            neighbors++;                                                         // Directly below
        else if (row == maxRow && current[0][col])
            neighbors++;

        if (row != maxRow && col != 0 && current[row + 1][col - 1])
            neighbors++;                                                         // Below and to the left
        else if (row == maxRow && col == 0 && current[0][maxCol])
            neighbors++;

        if (col != 0 && current[row][col - 1])
            neighbors++;                                                         // Directly left
        else if (col == 0 && current[row][maxCol])
            neighbors++;

        return neighbors;
    }

    private boolean cellAlive(int row, int col) {
        return current[row][col];
    }

    private boolean cellDead(int row, int col) {
        return !current[row][col];
    }

    private Color getColor() {
        if (TRIPPY) {
            Random random = new Random();
            return new Color(
                    random.nextInt(255),
                    random.nextInt(255),
                    random.nextInt(255)
            );
        } else {
            return new Color(
                    (float) COLOR.getRed(),
                    (float) COLOR.getGreen(),
                    (float) COLOR.getBlue(),
                    (float) COLOR.getOpacity()
            );
        }
    }
}
