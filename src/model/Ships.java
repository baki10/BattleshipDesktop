package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Ships {
    private static final int[] SHIP_SIZES = {4, 3, 3, 2, 2, 2, 1, 1, 1, 1};

    private final int cellSize;
    private List<Ship> ships;
    private boolean hidden;

    public Ships(int fieldSize, int cellSize, boolean hidden) {
        ships = new ArrayList<>();
        Random random = new Random();
        for (int size : SHIP_SIZES) {
            Ship ship;
            do {
                int x = random.nextInt(fieldSize);
                int y = random.nextInt(fieldSize);
                boolean horizontal = random.nextBoolean();
                ship = new Ship(x, y, size, horizontal);
            } while (notValidShipPlacement(ship, fieldSize));
            ships.add(ship);
        }
        this.cellSize = cellSize;
        this.hidden = hidden;
    }

    private boolean notValidShipPlacement(Ship ship, int fieldSize) {
        return ship.isOutOfField(0, fieldSize - 1) || isTouching(ship);
    }

    public List<Ship> getShips() {
        return ships;
    }

    public int getCellSize() {
        return cellSize;
    }

    public boolean hidden() {
        return hidden;
    }

    private boolean isTouching(Ship newShip) {
        for (Ship ship : ships) {
            if (ship.isTouching(newShip)) {
                return true;
            }
        }
        return false;
    }

    public boolean checkHit(int x, int y) {
        for (Ship ship : ships) {
            if (ship.checkHit(x, y)) {
                return true;
            }
        }
        return false;
    }

    public boolean allDead() {
        for (Ship ship : ships) {
            if (ship.isAlive()) {
                return false;
            }
        }
        return true;
    }
}