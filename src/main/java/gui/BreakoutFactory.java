package gui;

import com.almasb.fxgl.app.FXGL;
import com.almasb.fxgl.entity.Entities;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.RenderLayer;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.almasb.fxgl.physics.BoundingShape;
import com.almasb.fxgl.physics.HitBox;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.physics.box2d.dynamics.BodyType;
import com.almasb.fxgl.physics.box2d.dynamics.FixtureDef;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import logic.brick.Brick;

class BreakoutFactory {
	enum Types {
		PLAYER,
		BALL,
		WALL,
		BRICK;
	}
	private static int playerWidth= 200;
	private static int playerHeight= 25;
	private static double brickWidth= 70; // in function of the width + how many are there
	private static double brickHeight= 0.8*playerHeight;
	private static int topBorder= 50; // the top of the screen to locate bricks
	private static int leftBorder= 50; // the left of the screen to locte bricks

	static Entity newPlayer(double x, double y) {
		PhysicsComponent physicsComponent = new PhysicsComponent();
		physicsComponent.setBodyType(BodyType.KINEMATIC);
		physicsComponent.setFixtureDef(new FixtureDef().density(0f).restitution(0f).friction(1f));
		return Entities.builder()
				.at(x - playerWidth / 2.0, y - playerHeight)
				.type(Types.PLAYER)
				.viewFromNodeWithBBox(FXGL.getAssetLoader()
								.loadTexture("player.png", playerWidth, playerHeight))
				.with(physicsComponent, new CollidableComponent(true))
				.with(new PlayerControl())
				.build();
	}

	static Entity newBackGround(int width, int height) {
		return Entities.builder()
				.viewFromNode(new Rectangle(width, height, Color.BLACK))
				.renderLayer(RenderLayer.BACKGROUND)
				.build();
	}

	static Entity newBall(Entity entity) {
		PhysicsComponent physicsComponent= new PhysicsComponent();
		physicsComponent.setBodyType(BodyType.DYNAMIC);
		physicsComponent.setFixtureDef(new FixtureDef().density(1f).restitution(1f).friction(1f));

		int ballRadius = 10;
		return Entities.builder()
				.at((entity.getX()+entity.getRightX()) / 2.0, entity.getY() - ballRadius) // over the Entity
				.type(Types.BALL)
				.bbox(new HitBox("Ball", BoundingShape.circle(ballRadius)))
				.viewFromNode(new Circle(ballRadius, Color.SILVER))
				.with(physicsComponent, new CollidableComponent(true))
                .with(new BallControl())
				.build();
	}

	static Entity newWalls() {
	    PhysicsComponent physicsComponent= new PhysicsComponent();
	    physicsComponent.setFixtureDef(new FixtureDef().density(0f).restitution(0f).friction(0f));
		Entity walls= Entities.makeScreenBounds(1);
		walls.setType(Types.WALL);
		walls.addComponent(new CollidableComponent(true));
		return walls;
	}

	static Entity newBrick(Brick brick, int number) {
		PhysicsComponent physicsComponent= new PhysicsComponent();
		physicsComponent.setBodyType(BodyType.STATIC);
		physicsComponent.setFixtureDef(new FixtureDef().density(1f).restitution(1f).friction(1f)); // the same as the ball

		String textureNamePrefix;
		if (brick.isGlassBrick())
			textureNamePrefix= "glass";
		else if (brick.isWoodenBrick())
			textureNamePrefix= "wooden";
		else
			textureNamePrefix= "metal";

		double epsilon= 1;
		double posX= leftBorder + ((number % 10) * (brickWidth + epsilon));
		double posY= topBorder + ((number / 10) + 1) * (brickHeight + epsilon);

		return Entities.builder()
				.at(posX, posY)
				.type(Types.BRICK)
				.viewFromNodeWithBBox(FXGL.getAssetLoader()
						.loadTexture(textureNamePrefix+".png", brickWidth, brickHeight))
				.with(physicsComponent, new CollidableComponent(true))
				.with(new BrickControl(brick))
				.build();
	}
}
