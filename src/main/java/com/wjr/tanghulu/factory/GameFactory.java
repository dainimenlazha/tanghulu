package com.wjr.tanghulu.factory;

import com.almasb.fxgl.core.math.FXGLMath;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import com.wjr.tanghulu.component.StickComponent;
import com.wjr.tanghulu.component.TangComponent;
import com.wjr.tanghulu.config.TangType;
import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.scene.shape.Rectangle;

import static com.almasb.fxgl.dsl.FXGL.entityBuilder;
import static com.wjr.tanghulu.config.GameConfig.*;

public class GameFactory implements EntityFactory {

    @Spawns("tang")
    public Entity newTang(SpawnData data) {
        TangType type = data.get("type");
        Entity tang = entityBuilder(data)
                .type(GameType.TANG)
                .viewWithBBox(new Rectangle(defaultTangWidth, defaultTangHeight, type.getColor()))
                .with(new TangComponent())
                .zIndex(Integer.MAX_VALUE)
                .collidable()
                .build();
        tang.setProperty(tangCode, type.getCode());
        tang.setScaleOrigin(new Point2D((double) defaultTangWidth / 2, (double) defaultTangHeight / 2));
        return tang;
    }

    @Spawns("stick")
    public Entity newStick(SpawnData data) {
        Entity entity = entityBuilder(data)
                .type(GameType.STICK)
                .with(new StickComponent())
                .zIndex(1)
                .viewWithBBox(new Rectangle(defaultStickWidth, defaultStickHeight))
                .build();
        entity.setScaleOrigin(new Point2D((double) defaultStickWidth / 2, (double) defaultStickHeight / 2));
        return entity;
    }

}
