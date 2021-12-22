import processing.core.PImage;

import java.util.List;

public class Fish extends SgrassFishMedium implements OtherFuncs {

    private String id;

    public Fish(String id, Point position,
                  List<PImage> images, int actionPeriod) {
        this.id = id;
        this.position = position;
        this.images = images;
        this.imageIndex = 0;
        this.actionPeriod = actionPeriod;
    }

    @Override
    public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore) {
        scheduler.scheduleEvent(this, createActivityAction(world, imageStore), actionPeriod);
    }

    @Override
    public void executeEntityActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        Point pos = this.position;  // store current position before removing

        world.removeEntity(this);
        scheduler.unscheduleAllEvents(this);

        Crab crab = Crab.createCrab(this.id + CRAB_ID_SUFFIX, pos, actionPeriod / CRAB_PERIOD_SCALE, CRAB_ANIMATION_MIN + Functions.rand.nextInt(CRAB_ANIMATION_MAX - CRAB_ANIMATION_MIN), Functions.getImageList(imageStore, Crab.CRAB_KEY));

        world.addEntity(crab);
        crab.scheduleActions(scheduler, world, imageStore);
    }

    public static Fish createFish(String id, Point position, int actionPeriod, List<PImage> images) {
        return new Fish(id, position, images, actionPeriod);
    }

    public static boolean parseFish(String[] properties, WorldModel world, ImageStore imageStore) {
        if (properties.length == FISH_NUM_PROPERTIES)
        {
            Point pt = new Point(Integer.parseInt(properties[FISH_COL]),
                    Integer.parseInt(properties[FISH_ROW]));
            Fish entity = createFish(properties[FISH_ID], pt, Integer.parseInt(properties[FISH_ACTION_PERIOD]), Functions.getImageList(imageStore, FISH_KEY));
            world.tryAddEntity(entity);
        }

        return properties.length == FISH_NUM_PROPERTIES;
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
    public Activity createActivityAction(WorldModel world, ImageStore imageStore) {
        return new Activity(this, world, imageStore);
    }

    @Override
    int getAnimationPeriod() {
        return 0;
    }

    @Override
    Animation createAnimationAction(int repeatCount) {
        return null;
    }

    @Override
    void nextImage() {

    }
}
