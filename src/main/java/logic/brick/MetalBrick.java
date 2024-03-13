package logic.brick;

import controller.Game;

/**
 * Generates a metal brick
 * @author Rodrigo Llull Torres
 */
public class MetalBrick extends AbstractBrick {
    public MetalBrick() {
        setLife(10);
        setScore(0);
    }

    @Override
    public void accept(Game game) {
        game.raiseBall(); // increase the amount of balls in 1
    }

    @Override
    public boolean isMetalBrick() {
        return true;
    }
}
