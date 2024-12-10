package com.ciril.forestfiresimulation.model;

import lombok.Data;

@Data
public class Forest {
    public enum State {
        EMPTY, FIRE, ASH
    }

    private State[][] grid;
    private int height;
    private int width;

    // Constructeur principal
    public Forest(int height, int width) {
        this.height = height;
        this.width = width;
        this.grid = new State[height][width];
        initializeForest();
    }

    // Constructeur de copie
    public Forest(Forest other) {
        this.height = other.height;
        this.width = other.width;
        this.grid = new State[height][width];
        for (int i = 0; i < height; i++) {
            System.arraycopy(other.grid[i], 0, this.grid[i], 0, width);
        }
    }

    private void initializeForest() {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                grid[i][j] = State.EMPTY;
            }
        }
    }

    public void setFire(int x, int y) {
        if (isValidPosition(x, y)) {
            grid[x][y] = State.FIRE;
        }
    }

    public State getState(int x, int y) {
        if (isValidPosition(x, y)) {
            return grid[x][y];
        }
        throw new IllegalArgumentException("Invalid coordinates: (" + x + ", " + y + ")");
    }

    public void setState(int x, int y, State state) {
        if (isValidPosition(x, y)) {
            grid[x][y] = state;
        } else {
            throw new IllegalArgumentException("Invalid coordinates: (" + x + ", " + y + ")");
        }
    }

    private boolean isValidPosition(int x, int y) {
        return x >= 0 && x < height && y >= 0 && y < width;
    }
}
