package com.wjr.tanghulu.component;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.component.Component;
import javafx.scene.input.MouseButton;

public class TangComponent extends Component {

    private Entity stick;

    @Override
    public void onAdded() {
        entity.getViewComponent().addOnClickHandler(e -> {
            if(e.getButton() == MouseButton.PRIMARY){
                stick.getComponent(StickComponent.class).clicked();
            }
        });
    }

    public void setStick(Entity stick) {
        this.stick = stick;
    }
}
