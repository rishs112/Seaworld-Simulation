import processing.core.PImage;

import java.util.List;
import java.util.Optional;
import processing.core.PImage;

import java.util.List;
import java.util.Optional;

public class Monster extends AnimationAction implements OtherFuncs{

        private String id;
        private int imageIndex;
        private Point position;
        private List<PImage> images;
        private int resourceLimit;
        private int actionPeriod;
        private int animationPeriod;

    public Monster(Point position, List<PImage> images) {

            this.id = id;
            this.position = position;
            this.images = images;
            this.imageIndex = 0;
            this.resourceLimit = resourceLimit;
            this.actionPeriod = actionPeriod;
            this.animationPeriod = animationPeriod;
        }

        @Override
        public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore) {
            scheduler.scheduleEvent(this, createActivityAction(world, imageStore), actionPeriod);
            scheduler.scheduleEvent(this, createAnimationAction(0), getAnimationPeriod());
        }

        public int getAnimationPeriod() {
            return animationPeriod;
        }

        public Animation createAnimationAction(int repeatCount) {
            return new Animation(this, repeatCount);    }

        @Override
        public void executeEntityActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
            Optional<Entity> crabTarget = world.findNearest(this.position, Crab.class);
            long nextPeriod = actionPeriod;

            if (crabTarget.isPresent()) {
                Point tgtPos = crabTarget.get().getPosition();

                if (moveFullNotFull(world, crabTarget.get(), scheduler, imageStore)) {
                    MonsterSgrass msgrass = MonsterSgrass.createMonsterSgrass(MonsterSgrass.MONSTER_ID, tgtPos, 5000, Functions.getImageList(imageStore, "monstersgrass"));

                    world.removeEntity(this);
                    world.removeEntityAt(crabTarget.get().getPosition());
                    scheduler.unscheduleAllEvents(this);
                    world.addEntity(msgrass);
                    nextPeriod += actionPeriod;
                    msgrass.scheduleActions(scheduler, world, imageStore);
                }
            }
            scheduler.scheduleEvent(this, this.createActivityAction(world, imageStore), nextPeriod);
        }

        public void transformFull(WorldModel world, EventScheduler scheduler, ImageStore imageStore) {
            MonsterSgrass mons = MonsterSgrass.createMonsterSgrass(this.id, this.position, actionPeriod, this.images);

            world.removeEntity(this);
            scheduler.unscheduleAllEvents(this);

            world.addEntity(mons);
            mons.scheduleActions(scheduler, world, imageStore);
        }

        public boolean moveFullNotFull(WorldModel world, Entity target, EventScheduler scheduler, ImageStore imageStore) {
            if (this.position.adjacent(target.getPosition())) {
                return true;
            } else {
                Point nextPos = nextPositionMonster(world, target.getPosition());
                if(!(nextPos.x == -1 || nextPos.y == -1)) {
                    world.setBackgroundCell(nextPos, new Background(Background.Other_Background, Functions.getImageList(imageStore,"purpleback")));
                    world.setOccupancyCell(nextPos, null);

                }
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

        public static Monster createMonster(String id, int resourceLimit, Point position, int actionPeriod, int animationPeriod, List<PImage> images) {

            return new Monster(position, images);
        }

        public Activity createActivityAction(WorldModel world, ImageStore imageStore) {
            return new Activity(this, world, imageStore);
        }

        public Point getPosition() {
            return this.position;
        }

        public void setPosition(Point position) {
            this.position = position;
        }

        public void nextImage() {
            this.imageIndex = (this.imageIndex + 1) % this.images.size();
        }

        public PImage getCurrentImage(Object entity) {
            return this.images.get(this.imageIndex);
        }

        public int getActionPeriod() {
            return this.actionPeriod;
        }

        public Point nextPositionMonster(WorldModel world, Point destPos) {
            int horiz = Integer.signum(destPos.x - this.position.x);
            Point newPos = new Point(this.position.x + horiz,
                    this.position.y);

            if (horiz == 0 || world.isOccupied(newPos)) {
                int vert = Integer.signum(destPos.y - this.position.y);
                newPos = new Point(this.position.x,
                        this.position.y + vert);

                if (vert == 0 || world.isOccupied(newPos)) {
                    newPos = this.position;
                }
            }

            return newPos;
        }
}

