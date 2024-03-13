package logic.brick;

/**
 * Generates a wooden brick
 * @author Rodrigo Llull Torres
 */
public class WoodenBrick extends AbstractBrick {
    public WoodenBrick() {
        setLife(3);
        setScore(200);
    }

    @Override
    public boolean isWoodenBrick() {
        return true;
    }
}
