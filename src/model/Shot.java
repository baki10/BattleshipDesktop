package model;

public class Shot {
    private Coordinates coordinates;
    private boolean shot;

    Shot(Coordinates coordinates, boolean shot) {
        this.coordinates = coordinates;
        this.shot = shot;
    }

    public int getX() {
        return coordinates.getX();
    }

    public int getY() {
        return coordinates.getY();
    }

    public boolean isShot() {
        return shot;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }
}