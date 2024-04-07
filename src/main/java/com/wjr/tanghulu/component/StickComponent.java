package com.wjr.tanghulu.component;

import com.almasb.fxgl.core.math.FXGLMath;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.component.Component;
import com.wjr.tanghulu.TangApp;
import com.wjr.tanghulu.config.GameConfig;
import com.wjr.tanghulu.config.TangType;
import javafx.geometry.Point2D;
import javafx.scene.input.MouseButton;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static com.almasb.fxgl.dsl.FXGL.spawn;
import static com.wjr.tanghulu.config.GameConfig.*;

public class StickComponent extends Component {

    // stick上最大只能有这么多的tang
    private static final int maxTangIndex = GameConfig.defaultStickHeight / GameConfig.defaultTangHeight - 1;

    //要生成tang的index, 最大不超过maxTangIndex
    private int tangIndex;

    //是否被点击
    private boolean clicked;

    //被选择的tang
    private List<Entity> selectedTangs;

    //所有的tang
    private List<Entity> tangs = new LinkedList<>();

    @Override
    public void onAdded() {
        entity.getViewComponent().addOnClickHandler(e -> {
            if (e.getButton() == MouseButton.PRIMARY) {
                clicked();
            }
        });
    }

    public void spawnTang() {
        if(clicked) return;

        TangType type = TangType.values()[FXGLMath.random(0, 8)];

        int remainNum = maxTangIndex - tangIndex;
        int randomNum = FXGLMath.random(1, remainNum);
        randomNum = Math.min(randomNum, FXGLMath.random(3, 5));
        for (int i = 0; i < randomNum; i++) {
            if (tangIndex >= maxTangIndex) {
                return;
            }
            double tangx = entity.getCenter().getX() - (double) defaultTangWidth / 2;
            double tangy = entity.getBottomY() - (2 + tangIndex) * defaultTangHeight;
            Entity tang = spawn("tang", new SpawnData(new Point2D(tangx, tangy)).put("type",type));
            tangs.add(tang);
            tangIndex++;

            tang.getComponent(TangComponent.class).setStick(entity);
        }
    }

    public boolean moveToCurrentStick(StickComponent otherStickComponent) {
        try{
            List<Entity> otherSelectedTangs = otherStickComponent.getSelectedTangs();
            //判断是否是同种类型
            if(!tangs.isEmpty() && otherSelectedTangs.get(0).getInt(tangCode) != tangs.get(tangs.size() - 1).getInt(tangCode)){
                return false;
            }

            ArrayList<Entity> remove = new ArrayList<>();
            for (Entity moved : otherSelectedTangs) {
                if (tangIndex >= maxTangIndex) continue;
                double tangx = entity.getCenter().getX() - (double) defaultTangWidth / 2;
                double tangy = entity.getBottomY() - (2 + tangIndex) * defaultTangHeight;
                moved.setPosition(new Point2D(tangx, tangy));
                moved.setScaleUniform(1);
                tangs.add(moved);
                tangIndex++;

                moved.getComponent(TangComponent.class).setStick(entity);

                otherStickComponent.tangIndex--;

                remove.add(moved);
            }
            int size = otherSelectedTangs.size();
            otherSelectedTangs.removeAll(remove);
            otherStickComponent.tangs.removeAll(remove);
            return size == remove.size();
        }catch (Exception e) {
            System.out.println(e);
            return false;
        }
    }

    public void clicked() {
        /**
         * 第一次点击, 判断是否有其他被选中的tang
         *  如果有则执行移动, 将被选中的tang移动到当前的stick中
         *  如果没有则执行选中逻辑,如放大, 设置选中属性
         * 第二次点击, 直接取消选中, 并且还原大小
         */
        Entity selectedStick = FXGL.<TangApp>getAppCast().getSelectedStick();

        selectedTangs = selectedEntity();

        //点击空stick不做处理
        if(selectedStick == null && selectedTangs.isEmpty()) return;

        if (clicked) {
            //第二次点击, 直接取消选中, 并且还原大小
            clicked = false;
            entity.setScaleX(1);
            selectedTangs.forEach(e -> e.setScaleUniform(1));
            selectedTangs = null;
            FXGL.<TangApp>getAppCast().setSelectedStick(null);
            return;
        }

        //第一次点击, 判断是否有其他被选中的tang
        if (selectedStick == null) {
            //如果没有则执行选中逻辑,如放大, 设置选中属性
            clicked = true;
            entity.setScaleX(scaleSize);
            selectedTangs.forEach(e -> e.setScaleUniform(scaleSize));
            FXGL.<TangApp>getAppCast().setSelectedStick(entity);
            return;
        }

        //如果有则执行移动, 将被选中的tang移动到当前的stick中
        StickComponent otherStickComponent = selectedStick.getComponent(StickComponent.class);
        boolean full = moveToCurrentStick(otherStickComponent);
        if (full) {
            selectedStick.setScaleX(1);
            otherStickComponent.setClicked(false);
            FXGL.<TangApp>getAppCast().setSelectedStick(null);
        }
    }

    private List<Entity> selectedEntity() {
        List<Entity> result = new ArrayList<>();

        int size = tangs.size();
        if (size == 0) return result;

        //获取最顶部的tang对象
        Entity peak = tangs.get(tangs.size() - 1);
        result.add(peak);

        if (size == 1) return result;

        for (int i = size - 2; i >= 0; i--) {
            Entity sameEntity = tangs.get(i);
            if (sameEntity.getInt(tangCode) == peak.getInt(tangCode)) {
                result.add(sameEntity);
            } else {
                break;
            }
        }
        return result;
    }

    public void compose() {
        //只有满了可以合成
        if(isNotFull() || tangs.isEmpty()) return;

        //所有的元素相同可以合成
        int base = tangs.get(0).getInt(tangCode);
        for (Entity tang : tangs) {
            if(tang.getInt(tangCode) != base){
                return;
            }
        }

        //具体的合成逻辑
        for (Entity tang : tangs) {
            tang.removeFromWorld();
        }
        tangs.clear();
        selectedTangs = null;
        clicked = false;
        tangIndex = 0;
    }

    public boolean isNotFull() {
        return tangIndex < maxTangIndex;
    }


    public List<Entity> getSelectedTangs() {
        return selectedTangs;
    }

    public void setClicked(boolean clicked) {
        this.clicked = clicked;
    }

    public void setSelectedTangs(List<Entity> selectedTangs) {
        this.selectedTangs = selectedTangs;
    }
}
