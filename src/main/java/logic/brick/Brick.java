package logic.brick;

import controller.Game;

/**
 * Interface that represents a brick object.
 * <p>
 * All bricks should implement this interface.
 *
 * @author Juan-Pablo Silva
 */
public interface Brick {
    /**
     * Defines that a brick has been hit.
     * Implementations should consider the events that a hit to a brick can trigger.
     */
    void hit();

    /**
     * Gets whether the brick object is destroyed or not.
     *
     * @return true if the brick is destroyed, false otherwise
     */
    boolean isDestroyed();

    /**
     * Gets the points corresponding to the destroying of a brick object.
     *
     * @return the associated points of a brick object
     */
    int getScore();

    /**
     * Gets the remaining hits the brick has to receive before being destroyed.
     *
     * @return the remaining hits to destroy de brick
     */
    int remainingHits();

    /**
     * Sets the life of a Brick
     * @param life the «life» or more properly how many hits last to destroy the brick
     * @author Rodrigo Llull Torres
     */
    void setLife(int life);

    /**
     * Sets the score amount of the Brick
     * @param score points to be earned when the Brick is destroyed
     * @author Rodrigo Llull Torres
     */
    void setScore(int score);

    /**
     * Adds a game as oberver to the brick
     * @param game the game where the brick participates
     * @author Rodrigo Llull Torres
     */
    void connect(Game game);

    /**
     * Makes a brick accept a game to make changes in it
     * @param game the game to make changes
     * @author Rodrigo Llull Torres
     */
    void accept(Game game);

    /**
     * Asks whether a Brick is of type GlassBrick
     * @return true if the brick is a GlasBrick, false otherwise
     */
    boolean isGlassBrick();

    /**
     * Asks if a Brick is of type WoodenBrick
     * @return true if the brick is a WoodenBrick, false otherwise
     */
    boolean isWoodenBrick();

    /**
     * Asks if a Brick is of type MetalBrick
     * @return true if the brick is a MetalBrick, false otherwise
     */
    boolean isMetalBrick();
}
