/*
Entity ideally would includes functions for how all the entities in our virtual world might act...
 */

import processing.core.PImage;

import java.util.List;

public abstract class Entity {
   protected List<PImage> images;
   protected Point position;

   abstract Point getPosition();
   abstract void setPosition(Point position);
   abstract PImage getCurrentImage(Object entity);
   abstract int getActionPeriod();

   /*
   Atlantis
Crab
Fish
Obstacle
Octo
OctoNotFull
Quake
Sgrass

    */
}

