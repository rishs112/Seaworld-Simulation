import java.util.List;
import java.util.Optional;

public class Nodes {
    private int gCost;
    private int fCost;
    private int hCost;

    private Point position;
    private Point end;
    private Nodes parent = null;

    public Nodes(Point position, Point end, Nodes parent) {
        this.position = position;
        this.end = end;
        this.parent = parent;

        this.gCost = parent.getGCost() + 1;
        this.hCost = computeDistance();
        this.fCost = gCost + hCost;

    }

    public Nodes(Point position, Point end) {

        this.position = position;
        this.end = end;

        this.hCost = computeDistance();
        this.gCost = 0;
        this.fCost = gCost + hCost;

    }

    public int getGCost() {
        return gCost;
    }

    public int getfCost() {
        return this.fCost;
    }

    public int getHcost() {
        return this.hCost;
    }

    public int computeDistance() {
        return (Math.abs(this.position.x - end.x) + Math.abs(this.position.y - end.y));
    }

    public Point getPosition() {
        return this.position;
    }

    public Point getEnd() {
        return this.end;
    }

    public Nodes getParent() {
        return this.parent;
    }

    public boolean equals(Nodes other) {
        Point parent_pos = this.parent.getPosition();

        return this.fCost == other.getfCost() &&
                this.gCost == other.getGCost() &&
                this.hCost == other.getHcost() &&
                this.position.equals(other.getPosition()) &&
                parent_pos.equals(other.getParent().getPosition());


    }
}