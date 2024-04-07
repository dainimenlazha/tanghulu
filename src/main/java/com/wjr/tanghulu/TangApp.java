package com.wjr.tanghulu;

import com.almasb.fxgl.app.ApplicationMode;
import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.core.math.FXGLMath;
import com.almasb.fxgl.entity.Entity;
import com.wjr.tanghulu.component.StickComponent;
import com.wjr.tanghulu.factory.GameFactory;
import com.wjr.tanghulu.factory.GameType;
import javafx.scene.control.Button;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.almasb.fxgl.dsl.FXGL.*;
import static com.wjr.tanghulu.config.GameConfig.*;

/**
 * 合成糖葫芦
 */
public class TangApp extends GameApplication {

    private Entity selectedStick;

    @Override
    protected void initInput() {

    }

    @Override
    protected void initGameVars(Map<String, Object> vars) {

    }

    @Override
    protected void initUI() {

    }

    @Override
    protected void initGame() {
        getGameWorld().addEntityFactory(new GameFactory());

        double windowWidth = getAppWidth();
        double windowHeight = getAppHeight();

        double horizontalSpacing = (windowWidth - COLUMNS * defaultStickWidth) / (COLUMNS + 1);
        double verticalSpacing = (windowHeight - ROWS * defaultStickHeight) / (ROWS + 1);

        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLUMNS; col++) {
                double x = horizontalSpacing * (col + 1) + col * defaultStickWidth;
                double y = verticalSpacing * (row + 1) + row * defaultStickHeight;
                Entity stick = spawn("stick", x, y);
                stick.getComponent(StickComponent.class).spawnTang();
            }
        }

        Button spawnTangsButton = new Button("生成糖葫芦");
        spawnTangsButton.setTranslateX(150);
        spawnTangsButton.setTranslateY(15);
        spawnTangsButton.setOnAction(e -> randomSpawnTangs());

        Button compositeTangsButton = new Button("合成糖葫芦");
        compositeTangsButton.setTranslateX(300);
        compositeTangsButton.setTranslateY(15);
        compositeTangsButton.setOnAction(e -> compose());

        getGameScene().addUINodes(spawnTangsButton, compositeTangsButton);
    }

    private void compose(){
        List<Entity> stickEntities = getGameWorld().getEntitiesByType(GameType.STICK);
        for (Entity stickEntity : stickEntities) {
            StickComponent component = stickEntity.getComponent(StickComponent.class);
            component.compose();
        }
    }

    private void randomSpawnTangs() {
        List<Entity> stickEntities = getGameWorld().getEntitiesByType(GameType.STICK);
        List<StickComponent> notFull = new ArrayList<>();
        stickEntities.forEach(stickEntity -> {
            StickComponent stickComponent = stickEntity.getComponent(StickComponent.class);
            if (stickComponent.isNotFull()) notFull.add(stickComponent);
        });

        if (notFull.isEmpty()) return;

        List<Integer> usedIndex = new ArrayList<>();
        for (int i = 0; i < notFull.size() - 1; ) {
            int random = FXGLMath.random(0, notFull.size() - 1);
            if (usedIndex.contains(random)) {
                continue;
            }
            usedIndex.add(random);
            StickComponent stickComponent = notFull.get(random);
            stickComponent.spawnTang();
            i++;
        }
    }

    @Override
    protected void initPhysics() {

    }

    @Override
    protected void initSettings(GameSettings settings) {
        settings.setApplicationMode(isRelease ? ApplicationMode.RELEASE : ApplicationMode.DEBUG);
        settings.setWidth(defaultWidth);
        settings.setHeight(defaultHeight);
        settings.setIntroEnabled(introEnable);
        settings.setMainMenuEnabled(menuEnable);
        settings.setGameMenuEnabled(menuEnable);
        settings.setFullScreenAllowed(fullScreenEnable);
        settings.setFullScreenFromStart(fullScreenEnable);
        //显示帧数等信息
        settings.setProfilingEnabled(profilingEnable);
        settings.setTitle("合成糖葫芦");
        settings.setVersion("0.0.1");
    }

    @Override
    protected void onUpdate(double tpf) {
    }

    public Entity getSelectedStick() {
        return selectedStick;
    }

    public void setSelectedStick(Entity selectedStick) {
        this.selectedStick = selectedStick;
    }

    public static void main(String[] args) {
        launch(args);
    }


}
