package logic.brick;

import controller.Game;

import java.util.Observable;

/**
 * Abstract bricks class
 * Defines a brick element (from Brick Interface) as Observable
 * and defines the most part of the methods of a brick
 * @author Rodrigo Llull Torres
 */
abstract class AbstractBrick extends Observable implements Brick {
    private int life;
    private int score;
    private boolean neverNotifiedDestruction= true;

    @Override
    public void setLife(int life) {
        this.life = life;
    }

    @Override
    public void setScore(int score) {
        this.score = score;
    }

    @Override
    public void hit() {
        if (!isDestroyed()) // not destroyed yet
            life-= 1;
        if (isDestroyed() && neverNotifiedDestruction) { // destroyed just now
            notifyDestruction();
            neverNotifiedDestruction= false;
        }
    }

    @Override
    public int getScore() {
        return score;
    }

    @Override
    public boolean isDestroyed() {
        return life == 0;
    }

    @Override
    public int remainingHits() {
        return life;
    }

    // Observable faculties

    /**
     * Notifies the observers that something happened
     */
    private void notifyDestruction() {
        setChanged();
        notifyObservers();
    }

    @Override
    public void connect(Game game) {
        addObserver(game);
    }

    @Override
    public void accept(Game game) {
        game.raiseScore(score);
    }

    @Override
    public boolean isGlassBrick() {
        return false;
    }

    @Override
    public boolean isWoodenBrick() {
        return false;
    }

    @Override
    public boolean isMetalBrick() {
        return false;
    }
}
