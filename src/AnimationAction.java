import java.nio.file.Path;

public abstract class AnimationAction extends ActivityAction{

    //interface shared by Octo and Crab??
    abstract int getAnimationPeriod();
    abstract Animation createAnimationAction(int repeatCount);
    abstract void nextImage();
        //return new Animation(this, null, null, repeatCount);

    //Atlantis, Crab, OctoNotFull, Octo, Quake
}
