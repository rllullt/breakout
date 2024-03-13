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

import static org.junit.Assert.*;

public class LevelTest {
	private Level level1;
	private Level level2;
	private Level level3;
	private Level nullLevel;

	@Before
	public void setUp() {
		level1= new CreatableLevel("Level 1");
		level2= new CreatableLevel("Level 2");
		level3= new CreatableLevel("Nivel 3");
		nullLevel= new NullLevel();
	}

	@Test
	public void getName() {
		assertEquals("Nivel 3", level3.getName());
		assertEquals("", nullLevel.getName());
	}

	@Test
	public void testBricks() {
		ArrayList<Brick> brickList= new ArrayList<Brick>();
		assertEquals(brickList, level1.getBricks());
		brickList.add(new GlassBrick());
		assertNotEquals(brickList, level1.getBricks());

		assertEquals(0, level1.getNumberOfBricks());

		brickList.add(new MetalBrick());
		brickList.add(new WoodenBrick());

		level2= new CreatableLevel("Level 2", brickList);

		assertEquals(3, level2.getNumberOfBricks());
		assertEquals(brickList, level2.getBricks());

		assertEquals(250, level2.getPoints());
		assertEquals(0, level3.getPoints());
		assertEquals(0, nullLevel.getPoints());
	}

	@Test
	public void levelBehaviour() {
		assertTrue(level3.isPlayableLevel());
		assertFalse(nullLevel.isPlayableLevel());

		// Adding next levels
		assertFalse(level1.hasNextLevel());
		level1.addPlayingLevel(level2);
		assertTrue(level1.hasNextLevel());

		assertFalse(level2.hasNextLevel());
		level2.addPlayingLevel(level3);
		assertTrue(level2.hasNextLevel());

		// Setting the next level
		assertFalse(level3.hasNextLevel());
		level3.setNextLevel(level1);
		assertTrue(level3.hasNextLevel());
	}

}
