package controller;

import gui.Breakout;
import logic.brick.Brick;
import logic.brick.GlassBrick;
import logic.brick.MetalBrick;
import logic.brick.WoodenBrick;
import logic.level.CreatableLevel;
import logic.level.Level;
import logic.level.NullLevel;

import java.util.*;

/**
 * Game logic controller class.
 *
 * @author Juan-Pablo Silva
 * @author Rodrigo Llull Torres
 */
public class Game extends Observable implements Observer {
    private int ballsAmount;
    private int currentPoints;
    private Level currentLevel;
    private int currentLevelPoints;

    public Game(int balls) {
        ballsAmount= balls;
        currentPoints= 0;
        currentLevelPoints= 0;
        currentLevel= new NullLevel();
    }

    /**
     * This method is just an example. Change it or delete it at wish.
     * <p>
     * Checks whether the game has a winner or not
     *
     * @return true if the game has a winner, false otherwise
     */
    public boolean winner() {
        return !currentLevel.isPlayableLevel() && currentPoints > 0;
    }

    /**
     * Creates a new level with all brick types
     * @param name the new level name
     * @param numberOfBricks amount of bricks the level is going to have
     * @param probOfGlass creation probability of GlassBrick (probability of WoodenBrick is 1 - probOfGlass)
     * @param probOfMetal creation probability of MetalBrick
     * @param seed the seed to generate pseudo random numbers
     * @return the level with all type of bricks generated
     */
    public Level newLevelWithBricksFull(String name, int numberOfBricks, double probOfGlass, double probOfMetal, int seed) {
        Random random= new Random(seed);
        ArrayList<Brick> bricks= createBricksNoMetalList(numberOfBricks, probOfGlass, random);
        double r;
        for (int i= 0; i< numberOfBricks; i++) {
            r= random.nextDouble();
            if (r <= probOfMetal) {
                Brick brick = new MetalBrick();
                brick.connect(this);
                bricks.add(brick);
            }
        }
        return new CreatableLevel(name, bricks);
    }

    /**
     * Creates a new level with only Glass and Wooden bricks
     * @param name the new level name
     * @param numberOfBricks amount of bricks the level is going to have
     * @param probOfGlass creation probability of GlassBrick (probability of WoodenBrick is 1 - probOfGlass)
     * @param seed the seed to generate pseudo random numbers
     * @return the level with only Glass and Wooden brick generated
     */
    public Level newLevelWithBricksNoMetal(String name, int numberOfBricks, double probOfGlass, int seed) {
        Random random= new Random(seed);
        ArrayList<Brick> bricks= createBricksNoMetalList(numberOfBricks, probOfGlass, random);
        return new CreatableLevel(name, bricks);
    }

    /**
     * Generates a list with bricks of type glass and wooden
     * @param numberOfBricks amount of bricks to generate
     * @param probOfGlass    glass creation probability
     * @return the list of bricks generated
     */
    private ArrayList<Brick> createBricksNoMetalList(int numberOfBricks, double probOfGlass, Random random) {
        ArrayList<Brick> bricks= new ArrayList<>();
        Brick brick;
        double r;
        for (int i= 0; i< numberOfBricks; i++) {
            r= random.nextDouble();
            if (r <= probOfGlass)
                brick= new GlassBrick();
            else
                brick= new WoodenBrick();
            brick.connect(this);
            bricks.add(brick);
        }
        return bricks;
    }

    /**
     * Gets the first level in the game
     * @return the current level
     */
    public Level getCurrentLevel() {
        return currentLevel;
    }

    /**
     * Gets the first's level brick list
     * @return the brick list of current level
     */
    public List<Brick> getBricks() {
        return currentLevel.getBricks();
    }

    /**
     * Asks whether the current level has next playable level
     * @return true if the current level has a playable level next to it
     */
    public boolean hasNextLevel() {
        return currentLevel.hasNextLevel();
    }

    /**
     * Pass to the next level of the current {@link Level}. Ignores all conditions and skip to the next level.
     * Also, notifies the Breakout to refresh the level draws
     */
    public void goNextLevel() {
        currentLevel= currentLevel.getNextLevel();
        notifyToBreakout(); // “change the level, please”
    }

    /**
     * Gets whether the current {@link Level} is playable or not.
     *
     * @return true if the current level is playable, false otherwise
     */
    public boolean hasCurrentLevel() {
        return currentLevel.isPlayableLevel();
    }

    /**
     * Gets the name of the current {@link Level}.
     *
     * @return the name of the current level
     */
    public String getLevelName() {
        return currentLevel.getName();
    }


    /**
     * Sets the actual level to «level»
     * @param level the new current level
     */
    public void setCurrentLevel(Level level) {
        currentLevel= level;
    }

    /**
     * Adds a level to the list of {@link Level} to play. This adds the level in the last position of the list.
     *
     * @param level the level to be added
     */
    public void addPlayingLevel(Level level) {
        currentLevel= currentLevel.addPlayingLevel(level);
    }

    /**
     * Gets the number of points required to pass to the next level. Gets the points obtainable in the current {@link Level}.
     *
     * @return the number of points in the current level
     */
    public int getLevelPoints() {
        return currentLevel.getPoints();
    }

    /**
     * Gets the accumulated points through all levels and current {@link Level}.
     *
     * @return the cumulative points
     */
    public int getCurrentPoints() {
        return currentPoints;
    }

    /**
     * Gets the current number of available balls to play.
     *
     * @return the number of available balls
     */
    public int getBallsLeft() {
        return ballsAmount;
    }

    /**
     * Reduces the count of available balls and returns the new number.
     *
     * @return the new number of available balls
     */
    public int dropBall() {
        if (ballsAmount > 0)
            ballsAmount-=1;
        return ballsAmount;
    }

    /**
     * Checks whether the game is over or not. A game is over when the number of available balls are 0 or the player won the game.
     *
     * @return true if the game is over, false otherwise
     */
    public boolean isGameOver() {
        return ballsAmount == 0 || winner();
    }

    /**
     * Gets the number of bricks not destroyed yet
     * @return number of intact bricks in current level
     */
    public int getNumberOfBricks() {
        return currentLevel.getNumberOfBricks();
    }

    /**
     * Increases the amount of balls in 1
     */
    public void raiseBall() {
        ballsAmount+= 1;
    }

    /**
     * Increases the actual score gained from the level and the total of all levels
     * @param score how much point to increase
     */
    public void raiseScore(int score) {
        currentPoints+= score;
        currentLevelPoints+= score;
    }

    /**
     * Checks whether it is necessary to pass to next level
     */
    private void checkLevelChange() {
        if (currentLevelPoints == getLevelPoints()) {
            currentLevelPoints= 0;
            goNextLevel();
        }
    }


    @Override
    // Observer faculties
    public void update(Observable o, Object arg) {
        ((Brick)o).accept(this);
        checkLevelChange();
    }

    /**
     * When necessary to inform the GUI to draw the new level
     */
    private void notifyToBreakout() {
        setChanged();
        notifyObservers();
    }

    // Observable faculties

    public void accept(Breakout breakout) {

    }

    public void connect(Breakout breakout) {
        addObserver(breakout);
    }

}
