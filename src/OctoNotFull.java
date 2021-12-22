import processing.core.PImage;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

public class OctoNotFull extends CrabOctoMedium implements OtherFuncs {

    private String id;
    private int imageIndex;
    private int resourceLimit;
    private int resourceCount;
    private int actionPeriod;
    private int animationPeriod;

    public static final String OCTO_KEY = "octo";
    private static final int OCTO_NUM_PROPERTIES = 7;
    private static final int OCTO_ID = 1;
    private static final int OCTO_COL = 2;
    private static final int OCTO_ROW = 3;
    private static final int OCTO_LIMIT = 4;
    private static final int OCTO_ACTION_PERIOD = 5;
    private static final int OCTO_ANIMATION_PERIOD = 6;

    public OctoNotFull(String id, Point position,
                List<PImage> images, int resourceLimit, int resourceCount,
                int actionPeriod, int animationPeriod) {
        this.id = id;
        this.position = position;
        this.images = images;
        this.imageIndex = 0;
        this.resourceLimit = resourceLimit;
        this.resourceCount = resourceCount;
        this.actionPeriod = actionPeriod;
        this.animationPeriod = animationPeriod;

        paths = new AStarPathingStrategy();
    }
    @Override
    public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore) {
        scheduler.scheduleEvent(this, createActivityAction(world, imageStore), actionPeriod);
        scheduler.scheduleEvent(this, createAnimationAction(0), getAnimationPeriod());
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
        Optional<Entity> notFullTarget = world.findNearest(this.position,
                Fish.class);

        if (!notFullTarget.isPresent() ||
                !moveEntity(world, notFullTarget.get(), scheduler) ||
                !transformNotFull(world, scheduler, imageStore)) {
            scheduler.scheduleEvent(this,
                    createActivityAction(world, imageStore),
                    this.actionPeriod);
        }
    }

    public boolean transformNotFull(WorldModel world, EventScheduler scheduler, ImageStore imageStore) {
        if (this.resourceCount >= this.resourceLimit) {
            Octopus octo = Octopus.createOctopus(this.id, this.resourceLimit,
                    this.position, actionPeriod, this.animationPeriod, this.images);

            world.removeEntity(this);
            scheduler.unscheduleAllEvents(this);

            world.addEntity(octo);
            octo.scheduleActions(scheduler, world, imageStore);

            return true;
        }
        return false;
    }

    @Override
    public boolean moveEntity(WorldModel world, Entity target, EventScheduler scheduler) {
        if (this.position.adjacent(target.getPosition())) {
            this.resourceCount += 1;
            world.removeEntity(target);
            scheduler.unscheduleAllEvents(target);

            return true;
        } else {
            Point nextPos = nextPositionEntity(world, target.getPosition());

            if (!this.position.equals(nextPos)) {
                Optional<Entity> occupant = world.getOccupant(nextPos);
                if (occupant.isPresent()) {
                    scheduler.unscheduleAllEvents(occupant.get());
                }

                world.moveEntity(this, nextPos);
            }
            return false;
        }
    }

    public static OctoNotFull createOctoNotFull(String id, int resourceLimit, Point position, int actionPeriod, int animationPeriod, List<PImage> images) {
        return new OctoNotFull(id, position, images, resourceLimit, 0, actionPeriod, animationPeriod);
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

    public static boolean parseOcto(String [] properties, WorldModel world, ImageStore imageStore)
    {
        if (properties.length == OCTO_NUM_PROPERTIES)
        {
            Point pt = new Point(Integer.parseInt(properties[OCTO_COL]), Integer.parseInt(properties[OCTO_ROW]));
            OctoNotFull entity = createOctoNotFull(properties[OCTO_ID], Integer.parseInt(properties[OCTO_LIMIT]), pt, Integer.parseInt(properties[OCTO_ACTION_PERIOD]), Integer.parseInt(properties[OCTO_ANIMATION_PERIOD]), Functions.getImageList(imageStore, OCTO_KEY));
            world.tryAddEntity(entity);
        }

        return properties.length == OCTO_NUM_PROPERTIES;
    }

    @Override
    public Point nextPositionEntity(WorldModel world, Point destPos) {
        Point currPos = this.position;

        Predicate<Point> canPassThrough = point -> {
            Optional<Entity> getPoint = world.getOccupant(point);
            return !(getPoint.isPresent() && world.withinBounds(currPos) && !(getPoint.get().getClass() == Fish.class));
        };

        BiPredicate<Point, Point> withinReach = (p1, p2) -> p1.adjacent(p2);

        List<Point> x = paths.computePath(currPos, destPos, canPassThrough, withinReach, PathingStrategy.CARDINAL_NEIGHBORS);

        Collections.reverse(x);

        if(x.size() == 0) {
            return getPosition();
        }

        return x.get(1);
    }
}
