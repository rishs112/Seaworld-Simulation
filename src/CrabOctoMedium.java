public abstract class CrabOctoMedium extends AnimationAction{

    public PathingStrategy paths;

    abstract boolean moveEntity(WorldModel world, Entity target, EventScheduler scheduler);
    abstract Point nextPositionEntity(WorldModel world, Point destPos);
}

