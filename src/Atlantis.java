import processing.core.PImage;

import java.util.List;

public class Atlantis extends AnimationAction implements OtherFuncs {

    private int imageIndex;
    private int actionPeriod;
    private int animationPeriod;

    public static final String ATLANTIS_KEY = "atlantis";
    private static final int ATLANTIS_NUM_PROPERTIES = 4;
    private static final int ATLANTIS_ID = 1;
    private static final int ATLANTIS_COL = 2;
    private static final int ATLANTIS_ROW = 3;
    private final int ATLANTIS_ANIMATION_PERIOD = 70;
    private final int ATLANTIS_ANIMATION_REPEAT_COUNT = 7;

    public Atlantis(Point position, List<PImage> images, int actionPeriod, int animationPeriod) {
        this.position = position;
        this.images = images;
        this.imageIndex = 0;
        this.actionPeriod = actionPeriod;
        this.animationPeriod = animationPeriod;
    }

    @Override
    public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore) {
        scheduler.scheduleEvent(this, createAnimationAction(ATLANTIS_ANIMATION_REPEAT_COUNT), getAnimationPeriod());
    }
    @Override
    public int getAnimationPeriod() {
        return animationPeriod;
    }

    @Override
    public Animation createAnimationAction(int repeatCount) {
        return new Animation(this, repeatCount);
    }

    @Override
    public void executeEntityActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        scheduler.unscheduleAllEvents(this);
        world.removeEntity(this);
    }

    public static Atlantis createAtlantis(String id, Point position, List<PImage> images) {
        return new Atlantis(position, images, 0, 0);
    }

    public static boolean parseAtlantis(String[] properties, WorldModel world, ImageStore imageStore) {
        if (properties.length == ATLANTIS_NUM_PROPERTIES)
        {
            Point pt = new Point(Integer.parseInt(properties[ATLANTIS_COL]),
                    Integer.parseInt(properties[ATLANTIS_ROW]));
            Atlantis entity = createAtlantis(properties[ATLANTIS_ID], pt, Functions.getImageList(imageStore, ATLANTIS_KEY));
            world.tryAddEntity(entity);
        }

        return properties.length == ATLANTIS_NUM_PROPERTIES;
    }

    @Override
    public Point getPosition() {
        return this.position;
    }

    @Override
    public void setPosition(Point position) {
        this.position = position;
    }

    @Override
    public PImage getCurrentImage(Object entity) {
        return this.images.get(this.imageIndex);
    }

    @Override
    public int getActionPeriod() {
        return this.actionPeriod;
    }

    @Override
    public void nextImage() {
        this.imageIndex = (this.imageIndex + 1) % this.images.size();
    }

    @Override
    public Activity createActivityAction(WorldModel world, ImageStore imageStore) {
        return null;
    }
}
