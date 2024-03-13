package gui;

import com.almasb.fxgl.app.FXGL;
import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.input.Input;
import com.almasb.fxgl.input.UserAction;
import com.almasb.fxgl.physics.CollisionHandler;
import com.almasb.fxgl.physics.HitBox;
import com.almasb.fxgl.settings.GameSettings;
import controller.Game;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import logic.brick.Brick;

import java.util.*;

import static gui.BreakoutFactory.*;

public class Breakout extends GameApplication implements Observer {
    private Game game= new Game(3);
    private Integer currentLevel= 1;

	@Override
	protected void initSettings(GameSettings gameSettings) {
		/*
        Code for JavaFX application.
        (Stage, scene, scene graph)
        */
		int WIDTH = 800;
		int HEIGHT = 600;
		gameSettings.setWidth(WIDTH);
		gameSettings.setHeight(HEIGHT);
		gameSettings.setTitle("Breikaut - preAlfa");
		gameSettings.setVersion("0.1");
		gameSettings.setFontGame("Deja Vu Sans Mono");

        // The game
        game.connect(this);

        drawLevel();
	}

	@Override
	public void initGame() {
		Entity player= newPlayer(FXGL.getAppWidth()/2.0, FXGL.getAppHeight() - 30);
		Entity backGround= newBackGround(FXGL.getAppWidth(), FXGL.getAppHeight());
		Entity staticBall= newBall(player); // this ball stays static over the player
		Entity walls= newWalls();
		getGameWorld().addEntities(player, backGround, staticBall, walls);
		setInitialLevel();
	}

	@Override
	public void initPhysics() {
		getPhysicsWorld().setGravity(0, 0);
		// Collision Ball - Wall handler
		getPhysicsWorld().addCollisionHandler(new CollisionHandler(Types.BALL, Types.WALL) {
            @Override
            protected void onHitBoxTrigger(Entity ball, Entity wall, HitBox boxBall, HitBox boxWall) {
                if (boxWall.getName().equals("BOT")) {
					game.dropBall();
					getGameState().increment("numberOfBalls", -1);
					ball.removeFromWorld();
					if (game.getBallsLeft() == 0) {
						finishGame(false);
					}
					for (Entity player : getGameWorld().getEntitiesByType(Types.PLAYER)) {
						Entity staticBall = newBall(player);
						getGameWorld().addEntity(staticBall);
						player.getComponent(PlayerControl.class).setBallNotShot();
					}
                }
            }
        });

		// Collision Ball - Brick handler
		getPhysicsWorld().addCollisionHandler(new CollisionHandler(Types.BALL, Types.BRICK) {
			@Override
			protected void onHitBoxTrigger(Entity ball, Entity brick, HitBox boxBall, HitBox boxBrick) {
				brick.getComponent(BrickControl.class).hit();
				BrickControl brickControl= brick.getComponent(BrickControl.class);
				if (brickControl.isDestroyed()) {
					getGameState().increment("totalPoints", brickControl.getScore());
					getGameState().increment("currentLevelPoints", brickControl.getScore());
					brick.removeFromWorld();
				}
			}
		});
	}

	@Override
	public void initUI() {
		if (!game.isGameOver()) {
			ArrayList<Text> texts= new ArrayList();
			Text levelInformation1= new Text();
			texts.add(levelInformation1);
			levelInformation1.setText("Level: ");
			Text levelInformation2 = new Text();
			levelInformation2.textProperty().bind(getGameState().intProperty("currentLevel").asString());
			texts.add(levelInformation2);

			Text totalPoints1= new Text();
			totalPoints1.setText("Total Points: ");
			texts.add(totalPoints1);
			Text totalPoints2= new Text();
			totalPoints2.textProperty().bind(getGameState().intProperty("totalPoints").asString());
			texts.add(totalPoints2);

			Text currentLevelPoints1= new Text();
			currentLevelPoints1.setText("Current Level Points: ");
			texts.add(currentLevelPoints1);
			Text currentLevelPoints2= new Text();
			currentLevelPoints2.textProperty().bind(getGameState().intProperty("currentLevelPoints").asString());
			texts.add(currentLevelPoints2);

			texts.get(0).setX(10);
			texts.get(0).setY(30);
			texts.get(0).setFont(Font.font("Deja Vu Sans Mono", 16));
			texts.get(0).setFill(Color.YELLOW);
			getGameScene().addUINode(texts.get(0));

			for (int i= 1; i< texts.size(); i++) {
				Text text = texts.get(i);
				text.setX(10 + 290 * i / 2 + texts.get(i - 1).getWrappingWidth());
				text.setY(30);
				text.setFont(Font.font("Deja Vu Sans Mono", 16));
				text.setFill(Color.YELLOW);
				getGameScene().addUINode(text);
			}

			Text balls= new Text();
			balls.textProperty().bind(getGameState().intProperty("numberOfBalls").asString());
			balls.setX(10);
			balls.setY(50);
			balls.setFont(Font.font("Deja Vu Sans Mono", 16));
			balls.setFill(Color.YELLOW);
			getGameScene().addUINode(balls);
		}
		else { // the game is over
			finishGame(true);
		}
	}

	private void finishGame(boolean win) {
		if (win)
			System.out.println("Game finished with exit code YOU WIN!");
		else
			System.out.println("Game finished with exit code YOU LOST!");
	}

	@Override
	public void initGameVars(Map<String, Object> vars) {
		vars.put("totalPoints", 0);
		vars.put("currentLevelPoints", 0);
		vars.put("currentLevel", 1);
		vars.put("numberOfBalls", 3);
	}

	@Override
	public void initInput() {
		Input input= getInput();

		// To move the bar right
		input.addAction(new UserAction("Move Right") {
			@Override
			protected void onAction() {
				getGameWorld().getEntitiesByType(Types.PLAYER).forEach(
						p -> p.getComponent(PlayerControl.class).moveRight());
			}
		}, KeyCode.RIGHT);

		// To move the bar left
		input.addAction(new UserAction("Move Left") {
			@Override
			protected void onAction() {
				getGameWorld().getEntitiesByType(Types.PLAYER).forEach(
						p -> p.getComponent(PlayerControl.class).moveLeft());
			}
		}, KeyCode.LEFT);

		// To throw the ball
		input.addAction(new UserAction("Throw Ball") {
            @Override
            protected void onActionBegin() {
            	getGameWorld().getEntitiesByType(Types.BALL).forEach(
            			b -> b.getComponent(BallControl.class).shoot()
				);
            	getGameWorld().getEntitiesByType(Types.PLAYER).forEach(
            			p -> p.getComponent(PlayerControl.class).setBallShot()
				);
            }
        }, KeyCode.SPACE);

		// To add a new level
		input.addAction(new UserAction("New Level") {
			@Override
			protected void onActionBegin() {
				generateLevel();
			}
		}, KeyCode.N);
	}

	private void generateLevel() {
		double probOfGlass= currentLevel / Math.pow(currentLevel, 1.3); // decreases
		double probOfMetal= (double)currentLevel / (currentLevel + 6); // increases
		game.addPlayingLevel(game.newLevelWithBricksFull(
				"Level "+currentLevel, getTotalBricks(),
				probOfGlass, probOfMetal, (int) System.currentTimeMillis())
				.reorderBricks()
		);
	}

	private void drawLevel() {
		List<Brick> bricks= game.getBricks();
		for (int i= 0; i< bricks.size(); i++) {
			Entity brickDrawing= newBrick(bricks.get(i), i);
			getGameWorld().addEntity(brickDrawing);
		}
	}

	private void setInitialLevel() {
		generateLevel();
		drawLevel();
	}

	private int getTotalBricks() {
		return 19 + currentLevel; // 20 is the base number of bricks
	}

	@Override
    // The Observer behaviour
    public void update(Observable o, Object arg) {
        ((Game)o).accept(this);
        getGameWorld().getEntitiesByType(Types.BRICK).forEach(Entity::removeFromWorld);
        if (game.winner()) {
			finishGame(true);
		}
		getGameState().setValue("currentLevelPoints", 0);
		getGameState().increment("currentLevel", +1);
		drawLevel();
		currentLevel++;
	}

	public static void main(String[] args) {
		launch(args);
	}
}
