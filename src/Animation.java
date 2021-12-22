public class Animation implements Action{

    private AnimationAction entity;
    //private Entity entity;
    //private WorldModel world;
    //private ImageStore imageStore;
    private int repeatCount;

    public Animation(AnimationAction entity, //WorldModel world, ImageStore imageStore,
                      int repeatCount) {
        this.entity = entity;
       // this.world = world;
       // this.imageStore = imageStore;
        this.repeatCount = repeatCount;
    }

    public void executeAction(EventScheduler scheduler)
    {
        this.entity.nextImage(); //doesn't work

        if (this.repeatCount != 1)
        {
            scheduler.scheduleEvent(this.entity, this.entity.createAnimationAction(Math.max(this.repeatCount - 1, 0)), this.entity.getAnimationPeriod());
        }
    }
}

