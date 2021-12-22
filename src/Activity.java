public class Activity implements Action{

    private OtherFuncs entity;
    private WorldModel world;
    private ImageStore imageStore;

    public Activity(OtherFuncs entity, WorldModel world,
                    ImageStore imageStore) {
        this.entity = entity;
        this.world = world;
        this.imageStore = imageStore;
    }
    public void executeAction(EventScheduler scheduler)
    {
        this.entity.executeEntityActivity(this.world, this.imageStore, scheduler);

        /*switch (this.entity)
        {
            case this.entity instanceof Octopus:
                (Octopus)entity.executeOctoFullActivity(this.world,
                        this.imageStore, scheduler);
                break;

            case this.entity instanceof OctoNotFull:
                entity.executeOctoNotFullActivity(this.world,
                        this.imageStore, scheduler);
                break;

            case this.entity instanceof Fish:
                entity.executeFishActivity(this.world, this.imageStore,
                        scheduler);
                break;

            case this.entity instanceof Crab:
                entity.executeCrabActivity(this.world,
                        this.imageStore, scheduler);
                break;

            case this.entity instanceof Quake:
                entity.executeQuakeActivity(this.world, this.imageStore,
                        scheduler);
                break;

            case this.entity instanceof Sgrass:
                entity.executeSgrassActivity(this.world, this.imageStore,
                        scheduler);
                break;

            case this.entity instanceof Atlantis:
                entity.executeAtlantisActivity(this.world, this.imageStore,
                        scheduler);
                break;

            default:
                throw new UnsupportedOperationException(
                        String.format("executeActivityAction not supported for %s",
                                entity.getClass()));
        }

         */
    }
    }
