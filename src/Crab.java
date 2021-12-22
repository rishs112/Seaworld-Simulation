import processing.core.PImage;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Crab extends CrabOctoMedium implements OtherFuncs {

    private int imageIndex;
    private int actionPeriod;
    private int animationPeriod;
    public static final String CRAB_KEY = "crab";

    public Crab(Point position, List<PImage> images, int actionPeriod, int animationPeriod) {
        this.position = position;
        this.images = images;
        this.imageIndex = 0;
        this.actionPeriod = actionPeriod;
        this.animationPeriod = animationPeriod;

        paths = new AStarPathingStrategy();
    }

    @Override
    public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore) {
        scheduler.scheduleEvent(this, createActivityAction(world, imageStore),actionPeriod);
        scheduler.scheduleEvent(this, createAnimationAction(0), getAnimationPeriod());
    }

    @Override
    public int getAnimationPeriod() {
        return animationPeriod;
    }

    @Override
    public void executeEntityActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        Optional<Entity> crabTarget = world.findNearest(this.position, Sgrass.class);
        long nextPeriod = actionPeriod;

        if (crabTarget.isPresent()) {
            Point tgtPos = crabTarget.get().getPosition();

            if (moveEntity(world, crabTarget.get(), scheduler)) {
                Quake quake = Quake.createQuake(tgtPos, Functions.getImageList(imageStore, Quake.QUAKE_KEY));

                world.addEntity(quake);
                nextPeriod += actionPeriod;
                quake.scheduleActions(scheduler, world, imageStore);
            }
        }
        scheduler.scheduleEvent(this, this.createActivityAction(world, imageStore), nextPeriod);
    }

    public static Crab createCrab(String id, Point position, int actionPeriod, int animationPeriod, List<PImage> images) {
        return new Crab(position, images, actionPeriod, animationPeriod);
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
    @Override
    public Animation createAnimationAction(int repeatCount) {
        return new Animation(this, repeatCount);
    }

    @Override
    public boolean moveEntity(WorldModel world, Entity target, EventScheduler scheduler) {
        if (this.position.adjacent(target.getPosition())) {
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

    @Override
    public Point nextPositionEntity(WorldModel world,
                                  Point destPos) {

        Point currPos = this.position;
        Predicate<Point> canPassThrough = point -> {
            Optional<Entity> getPoint = world.getOccupant(point);
            return !(getPoint.isPresent()) && world.withinBounds(currPos);
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
