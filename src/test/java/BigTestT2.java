import facade.HomeworkTwoFacade;
import logic.brick.Brick;
import logic.brick.GlassBrick;
import logic.brick.MetalBrick;
import logic.brick.WoodenBrick;
import logic.level.Level;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.Assert.*;

public class BigTestT2 {
    private HomeworkTwoFacade hw2;
    private final int glassScore = 50;
    private final int woodenScore = 200;
    private final int seed = 0;
    private final int initialBalls = 3;

    @Before
    public void setUp() {
        hw2 = new HomeworkTwoFacade();
    }

    @Test
    public void testInitialSettings() {
        // general state
        assertFalse(hw2.isGameOver());
        assertFalse(hw2.winner());
        assertFalse(hw2.hasNextLevel());
        assertFalse(hw2.hasCurrentLevel());
        assertEquals(0, hw2.numberOfBricks());
        assertEquals(3, hw2.getBallsLeft());
        assertEquals("", hw2.getLevelName());
        assertEquals(0, hw2.getCurrentPoints());
        assertEquals(0, hw2.getLevelPoints());
        assertTrue(hw2.getBricks().isEmpty());

        // Level
        Level level = hw2.getCurrentLevel();
        assertEquals("", level.getName());
        assertEquals(0, level.getNumberOfBricks());
        assertTrue(level.getBricks().isEmpty());
        assertEquals(level, level.getNextLevel());
        assertFalse(level.isPlayableLevel());
        assertFalse(level.hasNextLevel());
        assertEquals(0, level.getPoints());

        // Bricks
        GlassBrick glassBrick = new GlassBrick();
        WoodenBrick woodenBrick = new WoodenBrick();
        MetalBrick metalBrick = new MetalBrick();

        assertFalse(glassBrick.isDestroyed());
        assertFalse(woodenBrick.isDestroyed());
        assertFalse(metalBrick.isDestroyed());

        assertEquals(1, glassBrick.remainingHits());
        assertEquals(3, woodenBrick.remainingHits());
        assertEquals(10, metalBrick.remainingHits());

        assertEquals(glassScore, glassBrick.getScore());
        assertEquals(woodenScore, woodenBrick.getScore());
        assertEquals(0, metalBrick.getScore());
    }

    @Test
    public void testPlayableLevelNoMetal() {
        String name = "Level 1";
        int numberOfBricks = 20;

        // only GlassBricks
        Level levelOnlyGlass = hw2.newLevelWithBricksNoMetal(name, numberOfBricks, 1, seed);
        // basic
        assertEquals(name, levelOnlyGlass.getName());
        assertEquals(numberOfBricks, levelOnlyGlass.getNumberOfBricks());
        assertNotEquals(levelOnlyGlass, levelOnlyGlass.getNextLevel());
        assertTrue(levelOnlyGlass.isPlayableLevel());
        assertFalse(levelOnlyGlass.hasNextLevel());
        assertEquals(numberOfBricks * glassScore, levelOnlyGlass.getPoints());
        assertFalse(levelOnlyGlass.getBricks().isEmpty());
        assertEquals(numberOfBricks, levelOnlyGlass.getBricks().size());
        // count
        long numberOfGlass = levelOnlyGlass.getBricks()
                .stream()
                .filter(brick -> brick instanceof GlassBrick)
                .count();
        long numberOfWood = levelOnlyGlass.getBricks()
                .stream()
                .filter(brick -> brick instanceof WoodenBrick)
                .count();
        assertEquals(0, numberOfWood);
        assertEquals(numberOfBricks, numberOfGlass);


        // only WoodenBricks
        Level levelOnlyWood = hw2.newLevelWithBricksNoMetal(name, numberOfBricks, 0, seed);
        // basic
        assertEquals(name, levelOnlyWood.getName());
        assertEquals(numberOfBricks, levelOnlyWood.getNumberOfBricks());
        assertNotEquals(levelOnlyWood, levelOnlyWood.getNextLevel());
        assertTrue(levelOnlyWood.isPlayableLevel());
        assertFalse(levelOnlyWood.hasNextLevel());
        assertEquals(numberOfBricks * woodenScore, levelOnlyWood.getPoints());
        assertFalse(levelOnlyWood.getBricks().isEmpty());
        assertEquals(numberOfBricks, levelOnlyWood.getBricks().size());
        // count
        numberOfGlass = levelOnlyWood.getBricks()
                .stream()
                .filter(brick -> brick instanceof GlassBrick)
                .count();
        numberOfWood = levelOnlyWood.getBricks()
                .stream()
                .filter(brick -> brick instanceof WoodenBrick)
                .count();
        assertEquals(numberOfBricks, numberOfWood);
        assertEquals(0, numberOfGlass);


        // both
        // 9 Glass, 11 Wooden
        Level level = hw2.newLevelWithBricksNoMetal(name, numberOfBricks, 0.5, seed);
        // basic
        assertEquals(name, level.getName());
        assertEquals(numberOfBricks, level.getNumberOfBricks());
        assertNotEquals(level, level.getNextLevel());
        assertTrue(level.isPlayableLevel());
        assertFalse(level.hasNextLevel());
        assertFalse(level.getBricks().isEmpty());
        assertEquals(numberOfBricks, level.getBricks().size());
        // count
        numberOfGlass = level.getBricks()
                .stream()
                .filter(brick -> brick instanceof GlassBrick)
                .count();
        numberOfWood = level.getBricks()
                .stream()
                .filter(brick -> brick instanceof WoodenBrick)
                .count();
        // hardcoded values for seed=0
        assertEquals(11, numberOfWood);
        assertEquals(9, numberOfGlass);
        assertEquals(11 * woodenScore + 9 * glassScore, level.getPoints());
    }

    @Test
    public void testPlayableLevelWithMetal() {
        String name = "Level 1 with metal";
        int numberOfBricks = 20;

        // no metal
        Level levelNoMetal = hw2.newLevelWithBricksFull(name, numberOfBricks, 0.5, 0, seed);
        // basic
        assertEquals(name, levelNoMetal.getName());
        assertEquals(numberOfBricks, levelNoMetal.getNumberOfBricks());
        assertNotEquals(levelNoMetal, levelNoMetal.getNextLevel());
        assertTrue(levelNoMetal.isPlayableLevel());
        assertFalse(levelNoMetal.hasNextLevel());
        assertFalse(levelNoMetal.getBricks().isEmpty());
        assertEquals(numberOfBricks, levelNoMetal.getBricks().size());
        // count
        long numberOfGlass = levelNoMetal.getBricks()
                .stream()
                .filter(brick -> brick instanceof GlassBrick)
                .count();
        long numberOfWood = levelNoMetal.getBricks()
                .stream()
                .filter(brick -> brick instanceof WoodenBrick)
                .count();
        long numberOfMetal = levelNoMetal.getBricks()
                .stream()
                .filter(brick -> brick instanceof MetalBrick)
                .count();
        // hardcoded values for seed=0
        assertEquals(11, numberOfWood);
        assertEquals(9, numberOfGlass);
        assertEquals(11 * woodenScore + 9 * glassScore, levelNoMetal.getPoints());
        assertEquals(0, numberOfMetal);


        // 100% of metal
        Level levelWithFullMetal = hw2.newLevelWithBricksFull(name, numberOfBricks, 0.5, 1, seed);
        // basic
        assertEquals(name, levelWithFullMetal.getName());
        assertEquals(numberOfBricks * 2, levelWithFullMetal.getNumberOfBricks());
        assertNotEquals(levelWithFullMetal, levelWithFullMetal.getNextLevel());
        assertTrue(levelWithFullMetal.isPlayableLevel());
        assertFalse(levelWithFullMetal.hasNextLevel());
        assertFalse(levelWithFullMetal.getBricks().isEmpty());
        assertEquals(numberOfBricks * 2, levelWithFullMetal.getBricks().size());
        // count
        numberOfGlass = levelWithFullMetal.getBricks()
                .stream()
                .filter(brick -> brick instanceof GlassBrick)
                .count();
        numberOfWood = levelWithFullMetal.getBricks()
                .stream()
                .filter(brick -> brick instanceof WoodenBrick)
                .count();
        numberOfMetal = levelWithFullMetal.getBricks()
                .stream()
                .filter(brick -> brick instanceof MetalBrick)
                .count();
        // hardcoded values for seed=0
        assertEquals(11, numberOfWood);
        assertEquals(9, numberOfGlass);
        assertEquals(11 * woodenScore + 9 * glassScore, levelWithFullMetal.getPoints());
        assertEquals(20, numberOfMetal);


        // both
        // 9 Glass, 11 Wooden, 9 Metal
        Level level = hw2.newLevelWithBricksFull(name, numberOfBricks, 0.5, 0.5, seed);
        // basic
        assertEquals(name, level.getName());
        assertEquals(numberOfBricks + 9, level.getNumberOfBricks());
        assertNotEquals(level, level.getNextLevel());
        assertTrue(level.isPlayableLevel());
        assertFalse(level.hasNextLevel());
        assertFalse(level.getBricks().isEmpty());
        assertEquals(numberOfBricks + 9, level.getBricks().size());
        // count
        numberOfGlass = level.getBricks()
                .stream()
                .filter(brick -> brick instanceof GlassBrick)
                .count();
        numberOfWood = level.getBricks()
                .stream()
                .filter(brick -> brick instanceof WoodenBrick)
                .count();
        numberOfMetal = level.getBricks()
                .stream()
                .filter(brick -> brick instanceof MetalBrick)
                .count();
        // hardcoded values for seed=0
        assertEquals(11, numberOfWood);
        assertEquals(9, numberOfGlass);
        assertEquals(11 * woodenScore + 9 * glassScore, level.getPoints());
        assertEquals(9, numberOfMetal);
    }

    @Test
    public void testBallBehaviour() {
        assertFalse(hw2.isGameOver());
        for (int i = initialBalls; i > 0; i--) {
            assertEquals(i - 1, hw2.dropBall());
            assertEquals(i - 1, hw2.getBallsLeft());
        }
        assertTrue(hw2.isGameOver());

        assertEquals(0, hw2.dropBall());
        assertEquals(0, hw2.getBallsLeft());
        assertTrue(hw2.isGameOver());
    }

    @Test
    public void testNewLevelBehaviour() {
        String name = "Test Level";
        // 16 Glass, 4 Wooden, 5 Metal
        Level level = hw2.newLevelWithBricksFull(name, 20, 0.8, 0.2, seed);

        hw2.setCurrentLevel(level);

        assertEquals(25, hw2.numberOfBricks());
        assertEquals(25, hw2.getBricks().size());
        assertTrue(hw2.hasCurrentLevel());
        assertFalse(hw2.hasNextLevel());
        assertEquals(name, hw2.getLevelName());
        assertEquals(level, hw2.getCurrentLevel());
        assertEquals(4 * woodenScore + 16 * glassScore, hw2.getLevelPoints());
    }

    @Test
    public void testNextLevelAddition() {
        String name = "Test Level";
        List<Level> levels = new ArrayList<>();

        Level tempLevel = hw2.newLevelWithBricksFull(name, 20, 0.5, 0.5, seed);
        assertFalse(tempLevel.hasNextLevel());
        tempLevel.addPlayingLevel(hw2.newLevelWithBricksFull(name, 20, 0.5, 0.5, seed));
        assertTrue(tempLevel.hasNextLevel());

        Level level = hw2.newLevelWithBricksFull(name, 20, 0.5, 0.5, seed);
        repeat(5, () -> {
            Level newLevel = hw2.newLevelWithBricksFull(name, 20, 0.5, 0.5, seed);
            levels.add(newLevel);
            level.addPlayingLevel(newLevel);
        });
        level.addPlayingLevel(hw2.newLevelWithBricksFull(name, 20, 0.5, 0.5, seed));

        assertTrue(levels
                .stream()
                .map(Level::hasNextLevel)
                .reduce(
                        true,
                        (a, b) -> a && b));
    }

    @Test
    public void testNextLevelAdditionHW2() {
        String name = "Test Level";

        Level tempLevel = hw2.newLevelWithBricksFull(name, 20, 0.5, 0.5, seed);
        hw2.setCurrentLevel(tempLevel);
        assertFalse(hw2.hasNextLevel());
        hw2.addPlayingLevel(hw2.newLevelWithBricksFull(name, 20, 0.5, 0.5, seed));
        assertTrue(hw2.hasNextLevel());

        Level level = hw2.newLevelWithBricksFull(name, 20, 0.5, 0.5, seed);
        hw2.setCurrentLevel(level);
        repeat(5, () -> hw2.addPlayingLevel(hw2.newLevelWithBricksFull(name, 20, 0.5, 0.5, seed)));
        hw2.addPlayingLevel(hw2.newLevelWithBricksFull(name, 20, 0.5, 0.5, seed));

        repeat(5, () -> {
            assertTrue(hw2.hasCurrentLevel());
            Level currentLevel = hw2.getCurrentLevel();
            hw2.goNextLevel();
            assertNotEquals(currentLevel, hw2.getCurrentLevel());
        });
    }

    @Test
    public void testBricks() {
        GlassBrick glassBrick = new GlassBrick();
        WoodenBrick woodenBrick = new WoodenBrick();
        MetalBrick metalBrick = new MetalBrick();

        assertFalse(glassBrick.isDestroyed());
        assertFalse(woodenBrick.isDestroyed());
        assertFalse(metalBrick.isDestroyed());

        assertEquals(1, glassBrick.remainingHits());
        assertEquals(3, woodenBrick.remainingHits());
        assertEquals(10, metalBrick.remainingHits());

        glassBrick.hit();
        woodenBrick.hit();
        metalBrick.hit();

        assertTrue(glassBrick.isDestroyed());
        assertFalse(woodenBrick.isDestroyed());
        assertFalse(metalBrick.isDestroyed());

        assertEquals(0, glassBrick.remainingHits());
        assertEquals(2, woodenBrick.remainingHits());
        assertEquals(9, metalBrick.remainingHits());

        glassBrick.hit();
        woodenBrick.hit();
        metalBrick.hit();

        assertTrue(glassBrick.isDestroyed());
        assertFalse(woodenBrick.isDestroyed());
        assertFalse(metalBrick.isDestroyed());

        assertEquals(0, glassBrick.remainingHits());
        assertEquals(1, woodenBrick.remainingHits());
        assertEquals(8, metalBrick.remainingHits());

        woodenBrick.hit();
        repeat(8, metalBrick::hit);

        assertTrue(woodenBrick.isDestroyed());
        assertTrue(metalBrick.isDestroyed());

        assertEquals(0, woodenBrick.remainingHits());
        assertEquals(0, metalBrick.remainingHits());
    }

    @Test
    public void testBasicLevelAction() {
        // 9 Glass, 11 Wooden
        hw2.setCurrentLevel(hw2.newLevelWithBricksNoMetal("Level", 20, 0.5, seed));
        List<Brick> bricks = hw2.getBricks();
        List<Brick> glassBricks = bricks
                .stream()
                .filter(brick -> brick instanceof GlassBrick)
                .collect(Collectors.toList());
        List<Brick> woodenBricks = bricks
                .stream()
                .filter(brick -> brick instanceof WoodenBrick)
                .collect(Collectors.toList());

        int expectedScore = 0;
        assertEquals(expectedScore, hw2.getCurrentPoints());

        bricks.forEach(Brick::hit);
        expectedScore += glassScore * glassBricks.size();
        assertEquals(expectedScore, hw2.getCurrentPoints());

        assertTrue(glassBricks
                .stream()
                .allMatch(brick -> brick.remainingHits() == 0));
        assertTrue(woodenBricks
                .stream()
                .allMatch(brick -> brick.remainingHits() == 2));

        assertTrue(glassBricks
                .stream()
                .map(Brick::isDestroyed)
                .reduce(
                        true,
                        (a, b) -> a && b));
        assertFalse(woodenBricks
                .stream()
                .map(Brick::isDestroyed)
                .reduce(
                        false,
                        (a, b) -> a || b));

        repeat(2, () -> woodenBricks.forEach(Brick::hit));
        expectedScore += woodenScore * woodenBricks.size();
        assertEquals(expectedScore, hw2.getCurrentPoints());
        assertTrue(woodenBricks
                .stream()
                .allMatch(brick -> brick.remainingHits() == 0));

        repeat(20, () -> bricks.forEach(Brick::hit));
        assertEquals(expectedScore, hw2.getCurrentPoints());
        assertTrue(glassBricks
                .stream()
                .allMatch(brick -> brick.remainingHits() == 0));
        assertTrue(woodenBricks
                .stream()
                .allMatch(brick -> brick.remainingHits() == 0));
    }

    @Test
    public void testMetalBricks() {
        // 9 Glass, 11 Wooden, 9 Metal
        hw2.setCurrentLevel(hw2.newLevelWithBricksFull("Level", 20, 0.5, 0.5, seed));
        List<Brick> metalBricks = hw2.getBricks()
                .stream()
                .filter(brick -> brick instanceof MetalBrick)
                .collect(Collectors.toList());

        assertEquals(0, hw2.getCurrentPoints());
        assertEquals(initialBalls, hw2.getBallsLeft());

        metalBricks.forEach(Brick::hit);

        assertEquals(0, hw2.getCurrentPoints());
        assertEquals(initialBalls, hw2.getBallsLeft());

        assertTrue(metalBricks
                .stream()
                .allMatch(brick -> brick.remainingHits() == 9));
        assertFalse(metalBricks
                .stream()
                .map(Brick::isDestroyed)
                .reduce(
                        false,
                        (a, b) -> a || b));

        repeat(9, () -> metalBricks.get(0).hit());
        assertEquals(0, hw2.getCurrentPoints());
        assertEquals(initialBalls + 1, hw2.getBallsLeft());
        assertFalse(metalBricks
                .stream()
                .allMatch(brick -> brick.remainingHits() == 9));
        assertTrue(metalBricks
                .stream()
                .map(Brick::isDestroyed)
                .reduce(
                        false,
                        (a, b) -> a || b));
        assertEquals(0, metalBricks.get(0).remainingHits());

        repeat(9, () -> metalBricks.forEach(Brick::hit));
        assertEquals(0, hw2.getCurrentPoints());
        assertEquals(initialBalls + metalBricks.size(), hw2.getBallsLeft());

        assertTrue(metalBricks
                .stream()
                .allMatch(brick -> brick.remainingHits() == 0));
        assertTrue(metalBricks
                .stream()
                .map(Brick::isDestroyed)
                .reduce(
                        true,
                        (a, b) -> a && b));
    }

    @Test
    public void testGamePlay() {
        // 9 Glass, 11 Wooden, 3 Metal
        Level level1 = hw2.newLevelWithBricksFull("Level 1", 20, 0.5, 0.1, seed);
        // 10 Glass, 20 Wooden, 9 Metal
        Level level2 = hw2.newLevelWithBricksFull("Level 2", 30, 0.4, 0.3, seed);
        // 9 Glass, 31 Wooden, 12 Metal
        Level level3 = hw2.newLevelWithBricksFull("Level 3", 40, 0.2, 0.4, seed);
        hw2.setCurrentLevel(level1);
        hw2.addPlayingLevel(level2);
        hw2.addPlayingLevel(level3);

        int expectedScore = 0;

        assertFalse(hw2.winner());
        assertEquals(level1, hw2.getCurrentLevel());
        assertEquals(expectedScore, hw2.getCurrentPoints());
        assertEquals(initialBalls, hw2.getBallsLeft());
        assertTrue(hw2.hasCurrentLevel());
        assertTrue(hw2.hasNextLevel());

        // win level 1
        repeat(3, () -> hw2.getBricks().forEach(Brick::hit));
        expectedScore += glassScore * 9 + woodenScore * 11;

        assertFalse(hw2.winner());
        assertNotEquals(level1, hw2.getCurrentLevel());
        assertEquals(level2, hw2.getCurrentLevel());
        assertEquals(expectedScore, hw2.getCurrentPoints());
        assertEquals(initialBalls, hw2.getBallsLeft());
        assertTrue(hw2.hasCurrentLevel());
        assertTrue(hw2.hasNextLevel());

        // win level 2 and destroy all metal
        List<Brick> metalBricks = hw2.getBricks()
                .stream()
                .filter(brick -> brick instanceof MetalBrick)
                .collect(Collectors.toList());
        repeat(10, () -> metalBricks.forEach(Brick::hit));
        repeat(3, () -> hw2.getBricks().forEach(Brick::hit));

        expectedScore += glassScore * 10 + woodenScore * 20;

        assertFalse(hw2.winner());
        assertNotEquals(level2, hw2.getCurrentLevel());
        assertEquals(level3, hw2.getCurrentLevel());
        assertEquals(expectedScore, hw2.getCurrentPoints());
        assertEquals(initialBalls + metalBricks.size(), hw2.getBallsLeft());
        assertTrue(hw2.hasCurrentLevel());
        assertFalse(hw2.hasNextLevel());

        // win the game
        repeat(3, () -> hw2.getBricks().forEach(Brick::hit));
        expectedScore += glassScore * 9 + woodenScore * 31;
        assertTrue(hw2.winner());
        assertNotEquals(level3, hw2.getCurrentLevel());
        assertEquals(expectedScore, hw2.getCurrentPoints());
        assertEquals(initialBalls + metalBricks.size(), hw2.getBallsLeft());
        assertFalse(hw2.hasCurrentLevel());
        assertFalse(hw2.hasNextLevel());

    }

    private void repeat(int n, Runnable action) {
        IntStream.range(0, n).forEach(i -> action.run());
    }
}
