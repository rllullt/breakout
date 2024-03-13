package logic.level;

import logic.brick.Brick;

import java.util.ArrayList;

/**
 * Class that generates the null level
 */
public class NullLevel extends AbstractLevel {
    public NullLevel() {
        setName("");
        setBrickList(new ArrayList<Brick>());
        setNextLevel(this);
    }

    @Override
    public boolean isPlayableLevel() {
        return false;
    }

    @Override
    public boolean hasNextLevel() {
        return false;
    }

    @Override
    public Level addPlayingLevel(Level level) {
        level.setNextLevel(this);
        return level;
    }

}
