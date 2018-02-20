import model.Ships;
import model.Shot;
import model.Shots;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Random;

public class BattleShipGui extends JFrame {

    private static final String TITLE = "Battle Ship";
    private static final String NEW_GAME = "New game";
    private static final String EXIT = "Exit";
    private static final String YOU_WON = "YOU WON!";
    private static final String COMPUTER_WON = "COMPUTER WON!";
    static final int FIELD_SIZE = 10;
    private static final int COMPUTER_PANEL_SIZE = 400;
    static final int COMPUTER_CELL_SIZE = COMPUTER_PANEL_SIZE / FIELD_SIZE;
    private static final int MY_PANEL_SIZE = COMPUTER_PANEL_SIZE / 2;
    private static final int MY_CELL_SIZE = MY_PANEL_SIZE / FIELD_SIZE;
    private static final int MOUSE_BUTTON_LEFT = MouseEvent.BUTTON1;
    private static final int MOUSE_BUTTON_RIGHT = MouseEvent.BUTTON3;

    private JTextArea textArea;
    private JPanel computerBoard, myBoard;
    private Ships computerShips;
    private Ships myShips;
    private Shots computerShots;
    private Shots myShots;
    private boolean gameOver;

    public static void main(String[] args) {
        new BattleShipGui();
    }

    private BattleShipGui() {
        setTitle(TITLE);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        // boards
        computerBoard = new GamePanel(COMPUTER_PANEL_SIZE, COMPUTER_PANEL_SIZE, this);
        computerBoard.addMouseListener(computerBoardMouseAdapter());
        myBoard = new GamePanel(MY_PANEL_SIZE, MY_PANEL_SIZE, this);

        // buttons
        JButton newGameButton = new JButton(NEW_GAME);
        newGameButton.addActionListener(e -> {
            init();
            computerBoard.repaint();
            myBoard.repaint();
        });
        JButton exit = new JButton(EXIT);
        exit.addActionListener(e -> System.exit(0));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout());
        buttonPanel.add(newGameButton);
        buttonPanel.add(exit);

        // text area
        textArea = new JTextArea();
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);

        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BorderLayout());
        rightPanel.add(myBoard, BorderLayout.NORTH);
        rightPanel.add(scrollPane, BorderLayout.CENTER);
        rightPanel.add(buttonPanel, BorderLayout.SOUTH);

        setLayout(new BoxLayout(getContentPane(), BoxLayout.X_AXIS));
        add(computerBoard);
        add(rightPanel);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
        init();
    }

    private MouseListener computerBoardMouseAdapter() {
        return new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                if (gameOver) {
                    return;
                }
                int x = e.getX() / COMPUTER_CELL_SIZE;
                int y = e.getY() / COMPUTER_CELL_SIZE;
                if (e.getButton() == MOUSE_BUTTON_LEFT) {
                    if (!myShots.isShot(x, y)) {
                        myShots.add(x, y, true);
                        if (computerShips.checkHit(x, y)) {
                            // check for game over
                            if (computerShips.allDead()) {
                                textArea.append("\n" + YOU_WON);
                                gameOver = true;
                            }
                        } else {
                            // computer's turn
                            computerShoot();
                        }
                        computerBoard.repaint();
                        myBoard.repaint();
                        textArea.setCaretPosition(textArea.getText().length());
                    }
                }
                if (e.getButton() == MOUSE_BUTTON_RIGHT) {
                    Shot marked = myShots.getMarkedShot(x, y);
                    if (marked != null)
                        myShots.removeMarked(marked);
                    else
                        myShots.add(x, y, false);
                    computerBoard.repaint();
                }
            }
        };
    }

    private void init() {
        computerShips = new Ships(FIELD_SIZE, COMPUTER_CELL_SIZE, true);
        myShips = new Ships(FIELD_SIZE, MY_CELL_SIZE, false);
        computerShots = new Shots(MY_CELL_SIZE);
        myShots = new Shots(COMPUTER_CELL_SIZE);
        textArea.setText(NEW_GAME);
        gameOver = false;
    }

    private void computerShoot() {
        Random random = new Random();
        int x, y;
        do {
            x = random.nextInt(FIELD_SIZE);
            y = random.nextInt(FIELD_SIZE);
        } while (computerShots.isShot(x, y));
        computerShots.add(x, y, true);
        if (!myShips.checkHit(x, y)) {
            textArea.append("\n" + (x + 1) + ":" + (y + 1) + " Computer missed.");
        } else {
            textArea.append("\n" + (x + 1) + ":" + (y + 1) + " Computer hit the target.");
            textArea.setCaretPosition(textArea.getText().length());
            if (myShips.allDead()) {
                textArea.append("\n" + COMPUTER_WON);
                gameOver = true;
            } else
                computerShoot();
        }
    }

    public Ships getComputerShips() {
        return computerShips;
    }

    public Ships getMyShips() {
        return myShips;
    }

    public Shots getComputerShots() {
        return computerShots;
    }

    public Shots getMyShots() {
        return myShots;
    }
}