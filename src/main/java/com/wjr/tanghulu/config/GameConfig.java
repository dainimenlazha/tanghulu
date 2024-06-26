package com.wjr.tanghulu.config;

/**
 * 游戏配置
 */
public interface GameConfig {

    boolean isRelease = false;

    int defaultWidth = 600;

    int defaultHeight = 800;

    int defaultTangWidth = 40;

    int defaultTangHeight = 20;

    int ROWS = 3;
    int COLUMNS = 4;

    int defaultStickWidth = 15;

    int defaultStickHeight = 200;

    boolean introEnable = false;

    boolean menuEnable = false;

    boolean fullScreenEnable = false;

    boolean profilingEnable = false;

    String tangCode = "code";

    double scaleSize = 1.3;

}
