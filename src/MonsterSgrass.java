import processing.core.PImage;

import java.util.List;
import java.util.Optional;

public class MonsterSgrass extends SgrassFishMedium implements OtherFuncs {

    private String id;
    private int imageIndex;
    private Point position;
    private List<PImage> images;
    private int resourceLimit;
    private int actionPeriod;
    private int animationPeriod;

    public static String MONSTER_ID;
    public static String MONSTER_KEY;

    public MonsterSgrass(String id, Point position,
                  List<PImage> images, int actionPeriod) {
        this.id = id;
        this.position = position;
        this.images = images;
        this.actionPeriod = actionPeriod;
    }

    @Override
    public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore) {
        scheduler.scheduleEvent(this, createActivityAction(world, imageStore), actionPeriod);
        scheduler.scheduleEvent(this, createAnimationAction(0), getAnimationPeriod());
    }

    @Override
    int getAnimationPeriod() {
        return this.animationPeriod;
    }

    public Animation createAnimationAction(int repeatCount) {
        return new Animation(this, repeatCount);    }

    @Override
    void nextImage() {
        this.imageIndex = (this.imageIndex + 1) % this.images.size();
    }

    @Override
    public void executeEntityActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        Optional<Point> openPt = world.findOpenAround(this.position);

        if (openPt.isPresent()) {
            Fish fish = Fish.createFish(FISH_ID_PREFIX + this.id, openPt.get(), FISH_CORRUPT_MIN + Functions.rand.nextInt(FISH_CORRUPT_MAX - FISH_CORRUPT_MIN), Functions.getImageList(imageStore, Fish.FISH_KEY));
            world.addEntity(fish);
            fish.scheduleActions(scheduler, world, imageStore);
        }

        scheduler.scheduleEvent(this,
                createActivityAction(world, imageStore),
                actionPeriod);
    }

    public static MonsterSgrass createMonsterSgrass(String id, Point position, int actionPeriod, List<PImage> images) {
        return new MonsterSgrass(id, position, images, actionPeriod);
    }
    /*
    public static boolean parseSgrass(String[] properties, WorldModel world, ImageStore imageStore) {
        if (properties.length == SGRASS_NUM_PROPERTIES)
        {
            Point pt = new Point(Integer.parseInt(properties[SGRASS_COL]),
                    Integer.parseInt(properties[SGRASS_ROW]));
            MonsterSgrass entity = createMonsterSgrass(properties[SGRASS_ID], pt, Integer.parseInt(properties[SGRASS_ACTION_PERIOD]), Functions.getImageList(imageStore, SGRASS_KEY));
            world.tryAddEntity(entity);
        }
        return properties.length == SGRASS_NUM_PROPERTIES;
    }

     */

    @Override
    public Activity createActivityAction(WorldModel world, ImageStore imageStore) {
        return new Activity(this, world, imageStore);
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
}
