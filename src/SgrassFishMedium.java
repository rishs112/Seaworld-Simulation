import processing.core.PImage;

import java.util.List;

public abstract class SgrassFishMedium extends AnimationAction{

    protected String id;
    protected int imageIndex;
    protected int actionPeriod;

    public static final String FISH_KEY = "fish";
    protected static final int FISH_NUM_PROPERTIES = 5;
    protected static final int FISH_ID = 1;
    protected static final int FISH_COL = 2;
    protected static final int FISH_ROW = 3;
    protected static final int FISH_ACTION_PERIOD = 4;
    public static final int FISH_REACH = 1;

    protected final String CRAB_ID_SUFFIX = " -- crab";
    protected final int CRAB_PERIOD_SCALE = 4;
    protected final int CRAB_ANIMATION_MIN = 50;
    protected final int CRAB_ANIMATION_MAX = 150;

    public static final String SGRASS_KEY = "seaGrass";
    protected static final int SGRASS_NUM_PROPERTIES = 5;
    protected static final int SGRASS_ID = 1;
    protected static final int SGRASS_COL = 2;
    protected static final int SGRASS_ROW = 3;
    protected static final int SGRASS_ACTION_PERIOD = 4;
    protected final String FISH_ID_PREFIX = "fish -- ";
    protected final int FISH_CORRUPT_MIN = 20000;
    protected final int FISH_CORRUPT_MAX = 30000;

    abstract Activity createActivityAction(WorldModel world, ImageStore imageStore);
}
