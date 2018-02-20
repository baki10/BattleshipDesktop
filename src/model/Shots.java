package model;

import java.util.ArrayList;
import java.util.List;

public class Shots {
    private final int cellSize;
    private List<Shot> shots;

    public Shots(int cellSize) {
        this.cellSize = cellSize;
        shots = new ArrayList<>();
    }

    public void add(int x, int y, boolean shot) {
        shots.add(new Shot(new Coordinates(x,y), shot));
    }

    public boolean isShot(int x, int y) {
        for (Shot shot : shots) {
            if (shot.getCoordinates().equals(x, y) && shot.isShot()) {
                return true;
            }
        }
        return false;
    }

    public Shot getMarkedShot(int x, int y) {
        for (Shot marked : shots) {
            if (marked.getCoordinates().equals(x, y)) {
                if (!marked.isShot()) {
                    return marked;
                }
            }
        }
        return null;
    }

    public void removeMarked(Shot shot) {
        shots.remove(shot);
    }

    public List<Shot> getShots() {
        return shots;
    }

    public int getCellSize() {
        return cellSize;
    }
}