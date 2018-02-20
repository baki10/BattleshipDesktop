package model;

public class Coordinates {

    private final int x, y;

    Coordinates(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean equals(int x, int y) {
        return this.x == x && this.y == y;
    }
}
