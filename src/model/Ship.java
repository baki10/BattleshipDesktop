package model;

import java.util.ArrayList;
import java.util.List;

public class Ship {

    private List<Cell> cells;

    private static final int[] TOUCHED_DELTAS = {-1, 0, 1};

    Ship(int x, int y, int size, boolean horizontal) {
        cells = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            int cellX = x + i * (horizontal ? 0 : 1);
            int cellY = y + i * (horizontal ? 1 : 0);
            cells.add(new Cell(new Coordinates(cellX, cellY)));
        }
    }

    public boolean isOutOfField(int bottom, int top) {
        for (Cell cell : cells) {
            if (cell.getX() < bottom
                    || cell.getX() > top
                    || cell.getY() < bottom
                    || cell.getY() > top) {
                return true;
            }
        }
        return false;
    }

    public boolean isTouching(Ship otherShip) {
        for (Cell cell : cells) {
            for(Cell otherCell: otherShip.getCells()){
                if(isTouchedCells(cell, otherCell)){
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isTouchedCells(Cell cell, Cell otherCell) {
        for (int deltaX : TOUCHED_DELTAS) {
            for (int deltaY : TOUCHED_DELTAS) {
                if (otherCell.getCoordinates().equals(cell.getX() + deltaX, cell.getY() + deltaY)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean checkHit(int x, int y) {
        for (Cell cell : cells) {
            if (cell.checkHit(x, y)) {
                return true;
            }
        }
        return false;
    }

    public boolean isAlive() {
        for (Cell cell : cells) {
            if (!cell.isHit()) {
                return true;
            }
        }
        return false;
    }

    public List<Cell> getCells() {
        return cells;
    }
}