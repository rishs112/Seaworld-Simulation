import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class AStarPathingStrategy implements PathingStrategy {
    public List<Point> computePath(Point start, Point end, Predicate<Point> canPassThrough, BiPredicate<Point, Point> withinReach, Function<Point, Stream<Point>> potentialNeighbors) {
        List<Point> path = new LinkedList<>();

        List<Nodes> closeList = new ArrayList<>();
        List<Nodes> openList = new ArrayList<>();

        Nodes truStart = new Nodes(start, end);

        openList.add(truStart);

        while (!openList.isEmpty()) {

            double val = Integer.MAX_VALUE;
            int i = -1;
            int index = 0;
            for (Nodes node : openList) {
                i++;
                if (node.getfCost() < val) {
                    val = node.getfCost();
                    index = i;
                }
            }
            Nodes curr = openList.get(index);
            openList.remove(openList.indexOf(curr));
            closeList.add(curr);

            List<Point> actualNeighbours = potentialNeighbors.apply(curr.getPosition()).filter(canPassThrough).filter(p -> !(p.equals(end)) && !(p.equals(start))).collect(Collectors.toList());

            for (Point actual : actualNeighbours) {
                if (withinReach.test(actual, end)) {
                    Nodes newActual = new Nodes(actual, end, curr);
                    List<Point> output = initiatePath(path, newActual); //separate function
                    return output;
                }

                Nodes newN = new Nodes(actual, end, curr);
                boolean better = false;

                for (Nodes n : openList) {
                    if (newN.equals(n)) {
                        better = true;
                    }
                }
                for (Nodes n: closeList) {
                    if (newN.getPosition().equals(n.getPosition())) {
                        better = true;
                    }
                }
                if (!better) {
                    openList.add(newN);
                }
            }
        }
        return path;
    }

    public List<Point> initiatePath(List<Point> cPath, Nodes currP) {
        cPath.add(currP.getPosition());

        if (currP.getParent() == null) {
            return cPath;
        }
        return initiatePath(cPath, currP.getParent());
    }
}
