package com.wjr.tanghulu.config;

import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public enum TangType {
    RED(0, Color.RED),
    ORANGE(1, Color.ORANGE),
    YELLOW(2, Color.YELLOW),
    GREEN(3, Color.GREEN),
    CYAN(4, Color.CYAN),
    BLUE(5, Color.BLUE),
    MAGENTA(6, Color.MAGENTA),
    PINK(7, Color.PINK),
    LIGHTGRAY(8, Color.LIGHTGRAY);

    private final int code;
    private final Color color;

    TangType(int code, Color color) {
        this.code = code;
        this.color = color;
    }

    public int getCode() {
        return code;
    }

    public Color getColor() {
        return color;
    }

}
