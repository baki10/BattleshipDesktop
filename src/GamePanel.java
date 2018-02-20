import model.Cell;
import model.Ship;
import model.Ships;
import model.Shot;
import model.Shots;

import javax.swing.*;
import java.awt.*;

class GamePanel extends JPanel {

    private BattleShipGui game;
    private static final Color DEFAULT_COLOR = Color.lightGray;
    private static final Color BORDER_COLOR = Color.black;
    private static final Color CELL_HIT_COLOR = Color.red;
    private static final Color CELL_MISSED_COLOR = Color.gray;


    GamePanel(int width, int height, BattleShipGui game) {
        setPreferredSize(new Dimension(width, height));
        setBackground(Color.white);
        setBorder(BorderFactory.createLineBorder(BORDER_COLOR));
        this.game = game;
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        int cellSize = (int) getSize().getHeight() / BattleShipGui.FIELD_SIZE;
        g.setColor(DEFAULT_COLOR);
        for (int i = 1; i < BattleShipGui.FIELD_SIZE; i++) {
            g.drawLine(0, i * cellSize, BattleShipGui.FIELD_SIZE * cellSize, i * cellSize);
            g.drawLine(i * cellSize, 0, i * cellSize, BattleShipGui.FIELD_SIZE * cellSize);
        }
        if (cellSize == BattleShipGui.COMPUTER_CELL_SIZE) {
            shotsPaint(game.getMyShots(), g);
            shipsPaint(game.getComputerShips(), g);
        } else {
            shotsPaint(game.getComputerShots(), g);
            shipsPaint(game.getMyShips(), g);
        }
    }

    private void shipsPaint(Ships ships, Graphics g) {
        if (ships == null) {
            return;
        }
        int cellSize = ships.getCellSize();
        boolean hidden = ships.hidden();
        for (Ship ship : ships.getShips()) {
            for (Cell cell : ship.getCells()) {
                if (!hidden || cell.isHit()) {
                    g.setColor(cell.isHit() ? CELL_HIT_COLOR : DEFAULT_COLOR);
                    int x = cell.getX() * cellSize + 1;
                    int y = cell.getY() * cellSize + 1;
                    g.fill3DRect(x, y, cellSize - 2, cellSize - 2, true);
                }
            }
        }
    }

    private void shotsPaint(Shots shots, Graphics g) {
        if (shots == null) {
            return;
        }
        g.setColor(CELL_MISSED_COLOR);
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