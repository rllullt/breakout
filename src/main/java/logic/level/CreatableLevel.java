package logic.level;

import logic.brick.Brick;

import java.util.ArrayList;

/**
 * Class that instantiates a creatable level, that is, not null
 * @author Rodrigo Llull Torres
 */
public class CreatableLevel extends AbstractLevel {
    public CreatableLevel(String name, ArrayList<Brick> brickList) {
        setName(name);
        setBrickList(brickList);
        setNextLevel(new NullLevel());
    }
    public CreatableLevel(String name) {
        this(name, new ArrayList<Brick>());
    }

    public CreatableLevel() {
        this("");
    }

    @Override
    public boolean isPlayableLevel() {
        return true;
    }

    @Override
    public boolean hasNextLevel() {
        return getNextLevel().isPlayableLevel();
    }

    @Override
    public Level addPlayingLevel(Level level) {
        setNextLevel(getNextLevel().addPlayingLevel(level));
        return this;
    }

}
