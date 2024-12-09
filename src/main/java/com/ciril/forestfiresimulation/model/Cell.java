package com.ciril.forestfiresimulation.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Cell {
    public enum State {
        EMPTY, FIRE, ASH
    }

    private int x;
    private int y;
    private State state;

}
