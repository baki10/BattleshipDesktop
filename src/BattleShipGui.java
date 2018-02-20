import model.Cell;
import model.Ship;
import model.Ships;
import model.Shot;
import model.Shots;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;

class BattleShipGui extends JFrame {

    private static final String TITLE = "Battle Ship";
    private static final String NEW_GAME = "New game";
    private static final String EXIT = "Exit";
    private static final String YOU_WON = "YOU WON!";
    private static final String COMPUTER_WON = "COMPUTER WON!";
    private static final int FIELD_SIZE = 10;
    private static final int COMPUTER_PANEL_SIZE = 400;
    private static final int COMPUTER_CELL_SIZE = COMPUTER_PANEL_SIZE / FIELD_SIZE;
    private static final int MY_PANEL_SIZE = COMPUTER_PANEL_SIZE / 2;
    private static final int MY_CELL_SIZE = MY_PANEL_SIZE / FIELD_SIZE;
    private static final int MOUSE_BUTTON_LEFT = MouseEvent.BUTTON1;
    private static final int MOUSE_BUTTON_RIGHT = MouseEvent.BUTTON3;

    private JTextArea textArea;
    private JPanel computerBoard, myBoard;
    private Ships computerShips, myShips;
    private Shots computerShots, myShots;
    private Random random;
    private boolean gameOver;

    public static void main(String[] args) {
        new BattleShipGui();
    }

    private BattleShipGui() {
        setTitle(TITLE);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        // boards
        computerBoard = new GamePanel(COMPUTER_PANEL_SIZE, COMPUTER_PANEL_SIZE);
        computerBoard.addMouseListener(computerBoardMouseAdapter());
        myBoard = new GamePanel(MY_PANEL_SIZE, MY_PANEL_SIZE);

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
        random = new Random();
    }

    private void computerShoot() {
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

    class GamePanel extends JPanel {

        GamePanel(int width, int height) {
            setPreferredSize(new Dimension(width, height));
            setBackground(Color.white);
            setBorder(BorderFactory.createLineBorder(Color.blue));
        }

        @Override
        public void paint(Graphics g) {
            super.paint(g);
            int cellSize = (int) getSize().getHeight() / BattleShipGui.FIELD_SIZE;
            g.setColor(Color.lightGray);
            for (int i = 1; i < BattleShipGui.FIELD_SIZE; i++) {
                g.drawLine(0, i * cellSize, BattleShipGui.FIELD_SIZE * cellSize, i * cellSize);
                g.drawLine(i * cellSize, 0, i * cellSize, BattleShipGui.FIELD_SIZE * cellSize);
            }
            if (cellSize == BattleShipGui.COMPUTER_CELL_SIZE) {
                shotsPaint(myShots, g);
                shipsPaint(computerShips, g);
            } else {
                shotsPaint(computerShots, g);
                shipsPaint(myShips, g);
            }
        }

        private void shipsPaint(Ships ships, Graphics g) {
            if(ships == null){
                return;
            }
            int cellSize = ships.getCellSize();
            boolean hidden = ships.hidden();
            for (Ship ship : ships.getShips()) {
                for (Cell cell : ship.getCells()) {
                    if (!hidden || cell.isHit()) {
                        g.setColor(cell.isHit() ? Color.red : Color.lightGray);
                        int x = cell.getX() * cellSize + 1;
                        int y = cell.getY() * cellSize + 1;
                        g.fill3DRect(x, y, cellSize - 2, cellSize - 2, true);
                    }
                }
            }
        }

        private void shotsPaint(Shots shots, Graphics g) {
            if(shots == null){
                return;
            }
            g.setColor(Color.black);
            int size = shots.getCellSize();
            for (Shot shot : shots.getShots()) {
                int x = shot.getX() * size + size / 2 - 3;
                int y = shot.getY() * size + size / 2 - 3;
                if (shot.isShot()) {
                    g.fillRect(x, y, 8, 8);
                } else {
                    g.drawRect(x, y, 8, 8);
                }
            }
        }
    }

}