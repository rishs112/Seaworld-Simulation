import processing.core.PImage;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

public class Octopus extends CrabOctoMedium implements OtherFuncs {

    private String id;
    private int imageIndex;
    private int resourceLimit;
    private int actionPeriod;
    private int animationPeriod;

    public Octopus(String id, Point position, List<PImage> images, int resourceLimit, int actionPeriod, int animationPeriod) {

        this.id = id;
        this.position = position;
        this.images = images;
        this.imageIndex = 0;
        this.resourceLimit = resourceLimit;
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
        return new Animation(this, repeatCount);    }

    @Override
    public void executeEntityActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        Optional<Entity> fullTarget = world.findNearest(this.position,
                Atlantis.class);

        if (fullTarget.isPresent() &&
                moveEntity(world, fullTarget.get(), scheduler)) {
            //at atlantis trigger animation
            ((Atlantis) fullTarget.get()).scheduleActions(scheduler, world, imageStore);

            //transform to unfull
            transformFull(world, scheduler, imageStore);
        }
        else {
            scheduler.scheduleEvent(this, createActivityAction(world, imageStore), actionPeriod);
        }
    }

    public void transformFull(WorldModel world, EventScheduler scheduler, ImageStore imageStore) {
        OctoNotFull octo = OctoNotFull.createOctoNotFull(this.id, this.resourceLimit,
                this.position, actionPeriod, this.animationPeriod,
                this.images);

        world.removeEntity(this);
        scheduler.unscheduleAllEvents(this);

        world.addEntity(octo);
        octo.scheduleActions(scheduler, world, imageStore);
    }

    @Override
    public boolean moveEntity(WorldModel world, Entity target, EventScheduler scheduler) {
        if (this.position.adjacent(target.getPosition())) {
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

    public static Octopus createOctopus(String id, int resourceLimit, Point position, int actionPeriod, int animationPeriod, List<PImage> images) {
        return new Octopus( id, position, images, resourceLimit, actionPeriod, animationPeriod);
    }

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
    public Point nextPositionEntity(WorldModel world, Point destPos) {
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
