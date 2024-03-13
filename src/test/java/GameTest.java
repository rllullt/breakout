import controller.Game;
import logic.brick.Brick;
import logic.brick.GlassBrick;
import logic.brick.MetalBrick;
import logic.brick.WoodenBrick;
import logic.level.CreatableLevel;
import logic.level.Level;
import logic.level.NullLevel;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class GameTest {
	private Game game;
	private Level testLevel;
	private int glassPoints;
	private int woodenPoints;
	private int metalPoints;

	@Before
	public void setUp() {
		game= new Game(3);
		testLevel= new NullLevel();
		glassPoints= 50;
		woodenPoints= 200;
		metalPoints= 0;
	}

	@Test
	public void testCreateLevelNoMetal() {
		testLevel= game.newLevelWithBricksNoMetal("Level", 30, 0.6, 0);
		assertEquals(30, testLevel.getNumberOfBricks());
		assertEquals(16 * glassPoints + 14 * woodenPoints, testLevel.getPoints());
		assertFalse(testLevel.hasNextLevel());
		assertFalse(game.hasCurrentLevel());

		game.setCurrentLevel(testLevel);
		assertTrue(game.hasCurrentLevel());
		assertEquals(16*glassPoints + 14*woodenPoints, game.getLevelPoints());

		Level newLevel= new CreatableLevel("Another Level");

		game.addPlayingLevel(newLevel);
		assertNotEquals(newLevel, game.getCurrentLevel());
		game.goNextLevel();
		assertEquals(newLevel, game.getCurrentLevel());
	}

	@Test
	public void testCreateLevelAllBricks() {
		testLevel= game.newLevelWithBricksFull("Level", 30, 0.6, 0.3, 0);
		assertEquals(39, testLevel.getNumberOfBricks());
		assertEquals(16 * glassPoints + 14 * woodenPoints, testLevel.getPoints());
		assertFalse(testLevel.hasNextLevel());
		assertFalse(game.hasCurrentLevel());

		game.addPlayingLevel(testLevel);
		assertTrue(game.hasCurrentLevel());
		assertEquals(16*glassPoints + 14*woodenPoints, game.getLevelPoints());

		Level newLevel= new CreatableLevel("Another Level");

		game.addPlayingLevel(newLevel);
		assertNotEquals(newLevel, game.getCurrentLevel());
		game.goNextLevel();
		assertEquals(newLevel, game.getCurrentLevel());
	}

	@Test
	public void testChangeLevel() {
		assertFalse(game.hasNextLevel());
		assertFalse(game.hasCurrentLevel());

		game.addPlayingLevel(new CreatableLevel("Nivel"));
		assertTrue(game.hasCurrentLevel());
		assertFalse(game.hasNextLevel());
		assertEquals("Nivel", game.getLevelName());

		game.addPlayingLevel(new CreatableLevel("Level n"));
		assertTrue(game.getCurrentLevel().hasNextLevel());
		assertTrue(game.hasNextLevel());
		assertEquals("Nivel", game.getLevelName());

		game.goNextLevel();
		assertEquals("Level n", game.getLevelName());
		assertFalse(game.hasNextLevel());
	}

	@Test
	public void destroyBricks() {
		ArrayList<Brick> bricks= new ArrayList<Brick>();
		Brick metalBrick, glassBrick, woodenBrick;

		metalBrick= new MetalBrick();
		metalBrick.connect(game);
		bricks.add(metalBrick);

		glassBrick= new GlassBrick();
		glassBrick.connect(game);
		bricks.add(glassBrick);

		woodenBrick= new WoodenBrick();
		woodenBrick.connect(game);
		bricks.add(woodenBrick);

		game.setCurrentLevel(new CreatableLevel("Level", bricks));
		assertEquals(3, game.getBallsLeft());
		assertEquals(glassPoints + woodenPoints, game.getLevelPoints());
		assertEquals(0, game.getCurrentPoints());

		// Hit metal brick
		for (int i= 0; i< 10; i++)
			game.getBricks().get(0).hit();
		assertEquals(4, game.getBallsLeft());
		assertEquals(glassPoints + woodenPoints, game.getLevelPoints());
		assertEquals(0, game.getCurrentPoints());

		// Hit glass brick
		for (int i= 0; i< 1; i++)
			game.getBricks().get(1).hit();
		assertEquals(4, game.getBallsLeft());
		assertEquals(glassPoints + woodenPoints, game.getLevelPoints());
		assertEquals(glassPoints, game.getCurrentPoints());

		// Hit wooden brick
		for (int i= 0; i< 3; i++)
			game.getBricks().get(2).hit();
		assertEquals(4, game.getBallsLeft());
		assertEquals(0, game.getLevelPoints()); //the level changed
		assertEquals(glassPoints + woodenPoints, game.getCurrentPoints());
	}

	@Test
	public void testFinishLevel() {
		// Add 3 levels
		Level level1= game.newLevelWithBricksFull("Level 1", 30, 0.6, 0.3, 0);
		Level level2= game.newLevelWithBricksNoMetal("Level 2", 30, 0.6, 0);
		Level level3= game.newLevelWithBricksFull("Level 3", 30, 0.6, 0.3, 0);

		game.addPlayingLevel(level1);
		game.addPlayingLevel(level2);
		game.addPlayingLevel(level3);

		assertFalse(game.winner());

		// Win 2 levels
		List<Brick> bricks= game.getBricks();
		for (int i= 0; i< 10; i++)
			for (Brick brick : bricks)
				brick.hit();
		assertEquals("Level 2", game.getLevelName());
		assertEquals("Level 3", game.getCurrentLevel().getNextLevel().getName());
		assertTrue(game.hasCurrentLevel());
		assertTrue(game.hasNextLevel());
		assertFalse(game.isGameOver());

		bricks= game.getBricks();
		for (int i= 0; i< 10; i++)
			for (Brick brick : bricks)
				brick.hit();
		assertEquals("Level 3", game.getLevelName());
		assertTrue(game.hasCurrentLevel());
		assertFalse(game.hasNextLevel());
		assertFalse(game.isGameOver());

		assertFalse(game.winner());

		bricks= game.getBricks();
		for (int i= 0; i< 10; i++)
			for (Brick brick : bricks)
				brick.hit();
		assertEquals("", game.getLevelName());
		assertFalse(game.hasCurrentLevel());
		assertFalse(game.hasNextLevel());
		assertTrue(game.isGameOver());

		assertTrue(game.winner());
	}

}
