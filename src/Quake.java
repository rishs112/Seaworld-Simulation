import processing.core.PImage;

import java.util.List;

public class Quake extends AnimationAction implements OtherFuncs {

    private int imageIndex;
    private int actionPeriod;
    public static final String QUAKE_KEY = "quake";
    private static final String QUAKE_ID = "quake";
    private static final int QUAKE_ACTION_PERIOD = 1100;
    private int animationPeriod;

    private static final int QUAKE_ANIMATION_PERIOD = 100;
    private final int QUAKE_ANIMATION_REPEAT_COUNT = 10;

    public Quake(Point position,
                List<PImage> images,
                int actionPeriod, int animationPeriod) {
        this.position = position;
        this.images = images;
        this.imageIndex = 0;
        this.actionPeriod = actionPeriod;
        this.animationPeriod = animationPeriod;
    }
    @Override
    public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore) {
        scheduler.scheduleEvent(this, createActivityAction(world, imageStore), actionPeriod);
        scheduler.scheduleEvent(this, createAnimationAction(QUAKE_ANIMATION_REPEAT_COUNT), getAnimationPeriod());
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

    public static Quake createQuake(Point position, List<PImage> images) {
        return new Quake(position, images, QUAKE_ACTION_PERIOD, QUAKE_ANIMATION_PERIOD);
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
    public void nextImage() {
        this.imageIndex = (this.imageIndex + 1) % this.images.size();
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
    public Activity createActivityAction(WorldModel world, ImageStore imageStore) {
        return new Activity(this, world, imageStore);
    }
}
