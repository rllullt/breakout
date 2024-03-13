package logic.brick;

/**
 * Generates a glass brick
 * @author Rodrigo Llull Torres
 */
public class GlassBrick extends AbstractBrick {
    public GlassBrick() {
        setLife(1);
        setScore(50);
    }

    @Override
    public boolean isGlassBrick() {
        return true;
    }
}
