package com.mygdx.game.utiltts;

public enum Direction {
    UP(0, 1, 90), DOWN(0, -1, 270), LEFT(-1, 0, 180), RIGHT(1, 0, 0);

    private final int vx;
    private final int vy;
    private final float angle;

    public int getVx() {
        return vx;
    }

    public int getVy() {
        return vy;
    }

    public float getAngle() {
        return angle;
    }

    Direction(int vx, int vy, float angle){
        this.vx = vx;
        this.vy = vy;
        this.angle = angle;
    }
}
