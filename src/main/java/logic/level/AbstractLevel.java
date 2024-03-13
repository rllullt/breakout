package logic.level;

import logic.brick.Brick;

import java.util.ArrayList;
import java.util.List;

/**
 * Abstract class of a Level. Contains common methods for Creatable and Null levels
 * @author Rodrigo Llull Torres
 */
abstract class AbstractLevel implements Level {
    private String name;
    private List<Brick> brickList;
    private Level nextLevel;

    // Setters

    /**
     * Sets the name of a level
     * @param name the name of the level
     */
    void setName(String name) {
        this.name = name;
    }

    /**
     * Sets the list of bricks the level is going to have
     * @param brickList the list with the bricks to add
     */
    void setBrickList(List<Brick> brickList) {
        this.brickList = brickList;
    }

    /**
     * Sets the next level (every level knows which level comes next)
     * @param nextLevel the level coming next
     */
    @Override
    public void setNextLevel(Level nextLevel) {
        this.nextLevel = nextLevel;
    }

    @Override
    public Level reorderBricks() {
        List<Brick> newBrickList= new ArrayList<>();
        for (int i= 0; i< brickList.size(); i+= 3)
            newBrickList.add(brickList.get(i));
        for (int i= 1; i< brickList.size(); i+= 3)
            newBrickList.add(brickList.get(i));
        for (int i= 2; i< brickList.size(); i+= 3)
            newBrickList.add(brickList.get(i));
        brickList= newBrickList;
        return this;
    }

    // Getters

    /**
     * Gets the level's name
     * @return the name of the level
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * Gets the level's brick list
     * @return the list of bricks of the current level
     */
    @Override
    public List<Brick> getBricks() {
        return brickList;
    }

    /**
     * Gets the level coming next
     * @return the next level
     */
    @Override
    public Level getNextLevel() {
        return nextLevel;
    }

    @Override
    public int getPoints() {
        int points= 0;
        for (Brick brick : brickList)
            points+= brick.getScore();
        return points;
    }

    /**
     * Gets the number of {@link Brick} in the level.
     *
     * @return the number of Bricks in the level
     */
    @Override
    public int getNumberOfBricks() {
        int cant= 0;
        for (Brick brick : brickList)
            if (!brick.isDestroyed())
                cant+= 1;
        return cant;

    }

}
