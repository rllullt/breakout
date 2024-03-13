package gui;

import com.almasb.fxgl.core.math.Vec2;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.physics.PhysicsComponent;

class BallControl extends Component {

    private PhysicsComponent physicsComponent;

    private float speedX= 6;
    private float speedY= 6;
    private Vec2 velocity= new Vec2(speedX, speedY);
    private boolean shot= false;

    private int ballRadius= 10;

    @Override
    public void onUpdate(double tpf) {
        if (shot) {
            // Need to keep the velocity
            // X axis
            if (Math.abs(physicsComponent.getVelocityX()) < speedX*60) // probably 60 is the FPS
                physicsComponent.setLinearVelocity(Math.signum(physicsComponent.getVelocityX())*speedX*60,
                        physicsComponent.getVelocityY());

            // Y axis
            if (Math.abs(physicsComponent.getVelocityY()) < speedY*60)
                physicsComponent.setLinearVelocity(physicsComponent.getVelocityX(),
                        Math.signum(physicsComponent.getVelocityY())*speedY*60);
        }
        else { // not shot
            physicsComponent.setLinearVelocity(0.0, 0.0);
        }
    }

    void shoot() {
        if (!shot) {
            physicsComponent.setBodyLinearVelocity(velocity);
            shot= true;
        }
    }

    void setNotShot(double x, double y) {
        entity.setPosition(x, y - 2*ballRadius);
        shot= false;
    }
}
