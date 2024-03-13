package gui;

import com.almasb.fxgl.app.FXGL;
import com.almasb.fxgl.core.math.Vec2;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.physics.PhysicsComponent;

import java.util.List;

class PlayerControl extends Component {
	private static final float BOUNCE_FACTOR= 1.5f;
	private static final float SPEED_DECAY= 0.66f;

	private PhysicsComponent physicsComponent;
	private float speed= 0;

	private Vec2 velocity= new Vec2();

	private boolean ballShot= false;

	@Override
	public void onUpdate(double tpf) {
		if (ballShot)
			speed= 760*(float)tpf;
		else
			speed= 0;

		velocity.mulLocal(SPEED_DECAY);

		if (entity.getX() < 0)
			velocity.set(BOUNCE_FACTOR * (float) -entity.getX(), 0);
		else if (entity.getRightX() > FXGL.getApp().getWidth())
			velocity.set(BOUNCE_FACTOR * (float) -(entity.getRightX() - FXGL.getAppWidth()), 0);

		physicsComponent.setBodyLinearVelocity(velocity);
	}

	void moveRight() {
		velocity.set(speed, 0);
	}

	void moveLeft() {
		velocity.set(-speed, 0);
	}

    void setBallNotShot() {
        ballShot= false;
    }

    void setBallShot() {
		ballShot= true;
	}
}
