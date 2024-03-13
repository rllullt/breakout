package gui;

import com.almasb.fxgl.entity.component.Component;
import logic.brick.Brick;

class BrickControl extends Component {
    private Brick brick;

    BrickControl(Brick brick) {
        this.brick= brick;
    }

    public void hit() {
        brick.hit();
    }

    public boolean isDestroyed() {
        return brick.isDestroyed();
    }

    public int getScore() {
        return brick.getScore();
    }
}
