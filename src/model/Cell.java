package model;

public class Cell {

    private Coordinates coordinates;
    private boolean hit;

    Cell(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    public int getX() {
        return coordinates.getX();
    }

    public int getY() {
        return coordinates.getY();
    }

    public boolean checkHit(int x, int y) {
        if (coordinates.equals(x, y)) {
            hit = true;
            return true;
        }
        return false;
    }

    public boolean isHit(){
        return hit;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }
}