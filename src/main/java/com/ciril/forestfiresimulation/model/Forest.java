package com.ciril.forestfiresimulation.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Forest {
    private Cell[][] grid;
    private int height;
    private int width;

    public Forest(int height, int width) {
        this.height = height;
        this.width = width;
        this.grid = new Cell[height][width];
        initializeForest();
    }

    private void initializeForest() {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                grid[i][j] = new Cell(i, j, Cell.State.EMPTY);
            }
        }
    }

    public void setFire(int x, int y) {
        if (x >= 0 && x < height && y >= 0 && y < width) {
            grid[x][y].setState(Cell.State.FIRE);
        }
    }

    public Forest clone() {
        Forest clonedForest = new Forest(this.height, this.width);
        for (int i = 0; i < this.height; i++) {
            for (int j = 0; j < this.width; j++) {
                clonedForest.grid[i][j] = new Cell(i, j, this.grid[i][j].getState());
            }
        }
        return clonedForest;
    }
}
