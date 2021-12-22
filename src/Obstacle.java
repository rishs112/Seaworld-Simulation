import processing.core.PImage;

import java.util.List;

public class Obstacle extends Entity{

    private int imageIndex;
    private int actionPeriod;

    public static final String OBSTACLE_KEY = "obstacle";
    private static final int OBSTACLE_NUM_PROPERTIES = 4;
    private static final int OBSTACLE_ID = 1;
    private static final int OBSTACLE_COL = 2;
    private static final int OBSTACLE_ROW = 3;

    public Obstacle(Point position, List<PImage> images, int actionPeriod) {
        this.position = position;
        this.images = images;
        this.imageIndex = 0;
        this.actionPeriod = actionPeriod;
    }

    public static Obstacle createObstacle(String id, Point position,List<PImage> images) {
        return new Obstacle(position, images, 0);
    }

    public static boolean parseObstacle(String[] properties, WorldModel world, ImageStore imageStore) {
        if (properties.length == OBSTACLE_NUM_PROPERTIES)
        {
            Point pt = new Point(
                    Integer.parseInt(properties[OBSTACLE_COL]),
                    Integer.parseInt(properties[OBSTACLE_ROW]));
            Obstacle entity = createObstacle(properties[OBSTACLE_ID], pt, Functions.getImageList(imageStore, OBSTACLE_KEY));
            world.tryAddEntity(entity);
        }

        return properties.length == OBSTACLE_NUM_PROPERTIES;
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
