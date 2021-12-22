import java.util.List;
import processing.core.PImage;

final class Background
{
   private String id;
   public List<PImage> images;
   public int imageIndex;

   public Background(String id, List<PImage> images)
   {
      this.id = id;
      this.images = images;
   }

   public static final String BGND_KEY = "background";
   public static final String Other_Background = "purpleback";
   private static final int BGND_NUM_PROPERTIES = 4;
   private static final int BGND_ID = 1;
   private static final int BGND_COL = 2;
   private static final int BGND_ROW = 3;

   public static boolean parseBackground(String[] properties, WorldModel world, ImageStore imageStore) {
      if (properties.length == BGND_NUM_PROPERTIES)
      {
         Point pt = new Point(Integer.parseInt(properties[BGND_COL]),
                 Integer.parseInt(properties[BGND_ROW]));
         String id = properties[BGND_ID];
         world.setBackground(pt, new Background(id, Functions.getImageList(imageStore, id)));
      }

      return properties.length == BGND_NUM_PROPERTIES;
   }

   public PImage getCurrentImage() {
      return this.images.get(this.imageIndex);
   }
}
