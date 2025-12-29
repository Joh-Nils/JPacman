package main;

import Scenes.PlayingScene;

import java.awt.*;
import java.util.*;

public class AStar {
    private final GamePanel gp;

    private Path[] pathPoints;

    public AStar(GamePanel gp) {
        this.gp = gp;
    }

    public void setPathPoints(Path[] pathPoints) {
        this.pathPoints = pathPoints;
    }

    public char getDirection(int x, int y, int goalX, int goalY, char not) {
        if (pathPoints == null) return ' ';

        Node tree = new Node(x, y);

        NeighbourStruct ns = getNeighbours(x, y, tree);

        switch (not) {
            case 'U' -> ns.Up = null;
            case 'D' -> ns.Down = null;
            case 'L' -> ns.Left = null;
            case 'R' -> ns.Right = null;
        }

        tree.setNeighbours(ns);

        Node goal = check(tree, goalX, goalY);
        if (goal == null) {

            return ' ';
        }

        Stack<Character> directions = new Stack<>();

        Node current = goal;
        while (!current.equals(tree)) {
            if (current.y() > current.parent().y()) directions.push('D');
            else if (current.y() < current.parent().y()) directions.push('U');
            else if (current.x() > current.parent().x()) directions.push('R');
            else if (current.x() < current.parent().x()) directions.push('L');

            current = current.parent();
        }

        if (directions.empty()) {
            char ret = ' ';
            if (gp.currentScene instanceof PlayingScene playingScene) {
                for (Path path : playingScene.PathPoints) {
                    if (path.x() == x && Math.abs(path.y() - y) < 1.0) {
                        ret = path.directions().charAt(0);
                        break;
                    }
                    if (path.y() == y && Math.abs(path.x() - x) < 1.0) {
                        ret = path.directions().charAt(0);
                        break;
                    }
                }
            }

            return ret;
        }

        return directions.pop();
    }

    private Node check(Node root, int goalX, int goalY) {
        if (root == null) return null;

        Queue<Node> queue = new LinkedList<>();
        Set<Node> checked = new HashSet<>();

        queue.add(root);
        checked.add(root);

        while (!queue.isEmpty()) {
            Node tree = queue.poll();

            if (tree.x() == goalX && tree.y() == goalY) {
                return tree;
            } else if (tree.x() == goalX && tree.parent() != null) {
                if ((tree.y() > goalY && tree.parent().y() < goalY) ||
                        (tree.y() < goalY && tree.parent().y() > goalY)) {
                    return tree;
                }
            } else if (tree.y() == goalY && tree.parent() != null) {
                if ((tree.x() > goalX && tree.parent().x() < goalX) ||
                        (tree.x() < goalX && tree.parent().x() > goalX)) {
                    return tree;
                }
            }


            NeighbourStruct ns = tree.getKinds();
            if (ns == null) continue;

            if (ns.Up != null && checked.add(ns.Up)) {
                ns.Up.setNeighbours(getNeighbours(ns.Up.x(), ns.Up.y(), ns.Up));
                queue.add(ns.Up);
            } else if (ns.Up != null && checked.contains(ns.Up)) {
                Node n = ns.Up;

                if (n.x() == goalX && n.y() == goalY) {
                    n.setParent(tree);
                    return n;
                } else if (n.x() == goalX) {
                    if ((n.y() > goalY && tree.y() < goalY) ||
                            (n.y() < goalY && tree.y() > goalY)) {
                        n.setParent(tree);
                        return n;
                    }
                } else if (n.y() == goalY) {
                    if ((n.x() > goalX && tree.x() < goalX) ||
                            (n.x() < goalX && tree.x() > goalX)) {
                        n.setParent(tree);
                        return n;
                    }
                }
            } else if (ns.Up != null && ns.Up.equals(tree.parent())) {
                ns.Up = null;
            }

            if (ns.Down != null && checked.add(ns.Down)) {
                ns.Down.setNeighbours(getNeighbours(ns.Down.x(), ns.Down.y(), ns.Down));
                queue.add(ns.Down);
            } else if (ns.Down != null && checked.contains(ns.Down)) {
                Node n = ns.Down;

                if (n.x() == goalX && n.y() == goalY) {
                    n.setParent(tree);
                    return n;
                } else if (n.x() == goalX) {
                    if ((n.y() > goalY && tree.y() < goalY) ||
                            (n.y() < goalY && tree.y() > goalY)) {
                        n.setParent(tree);
                        return n;
                    }
                } else if (n.y() == goalY) {
                    if ((n.x() > goalX && tree.x() < goalX) ||
                            (n.x() < goalX && tree.x() > goalX)) {
                        n.setParent(tree);
                        return n;
                    }
                }
            } else if (ns.Down != null && ns.Down.equals(tree.parent())) {
                ns.Down = null;
            }

            if (ns.Left != null && checked.add(ns.Left)) {
                ns.Left.setNeighbours(getNeighbours(ns.Left.x(), ns.Left.y(), ns.Left));
                queue.add(ns.Left);
            } else if (ns.Left != null && checked.contains(ns.Left)) {
                Node n = ns.Left;

                if (n.x() == goalX && n.y() == goalY) {
                    n.setParent(tree);
                    return n;
                } else if (n.x() == goalX) {
                    if ((n.y() > goalY && tree.y() < goalY) ||
                            (n.y() < goalY && tree.y() > goalY)) {
                        n.setParent(tree);
                        return n;
                    }
                } else if (n.y() == goalY) {
                    if ((n.x() > goalX && tree.x() < goalX) ||
                            (n.x() < goalX && tree.x() > goalX)) {
                        n.setParent(tree);
                        return n;
                    }
                }
            } else if (ns.Left != null && ns.Left.equals(tree.parent())) {
                ns.Left = null;
            }

            if (ns.Right != null && checked.add(ns.Right)) {
                ns.Right.setNeighbours(getNeighbours(ns.Right.x(), ns.Right.y(), ns.Right));
                queue.add(ns.Right);
            } else if (ns.Right != null && checked.contains(ns.Right)) {
                Node n = ns.Right;

                if (n.x() == goalX && n.y() == goalY) {
                    n.setParent(tree);
                    return n;
                } else if (n.x() == goalX) {
                    if ((n.y() > goalY && tree.y() < goalY) ||
                            (n.y() < goalY && tree.y() > goalY)) {
                        n.setParent(tree);
                        return n;
                    }
                } else if (n.y() == goalY) {
                    if ((n.x() > goalX && tree.x() < goalX) ||
                            (n.x() < goalX && tree.x() > goalX)) {
                        n.setParent(tree);
                        return n;
                    }
                }
            } else if (ns.Right != null && ns.Right.equals(tree.parent())) {
                ns.Right = null;
            }
        }

        return null;
    }


    private NeighbourStruct getNeighbours(int x, int y, Node parent) {
        NeighbourStruct neighbourStruct = new NeighbourStruct();

        int UpAbs = Integer.MAX_VALUE;
        int DownAbs = Integer.MAX_VALUE;
        int LeftAbs = Integer.MAX_VALUE;
        int RightAbs = Integer.MAX_VALUE;

        for (Path path : pathPoints) {
            if (path.x() == x) {
                int abs = Math.abs(path.y() - y);
                if (path.directions().contains("D") && path.y() < y && UpAbs > abs) {
                    neighbourStruct.Up = new Node(path.x(), path.y(), path.directions(), parent);
                    UpAbs = abs;
                } else if (path.directions().contains("U") && path.y() > y && DownAbs > abs) {
                    neighbourStruct.Down = new Node(path.x(), path.y(), path.directions(), parent);
                    DownAbs = abs;
                }

                if (path.y() < y && abs < UpAbs && !path.directions().contains("D")) {
                    neighbourStruct.Up = null;
                    UpAbs = abs;
                }
                if (path.y() > y && abs < DownAbs && !path.directions().contains("U")) {
                    neighbourStruct.Down = null;
                    DownAbs = abs;
                }

                continue;
            }
            if (path.y() == y) {

                int abs = Math.abs(path.x() - x);
                if (path.directions().contains("R") && path.x() < x && LeftAbs > abs) {
                    neighbourStruct.Left = new Node(path.x(), path.y(), path.directions(), parent);
                    LeftAbs = abs;
                } else if (path.directions().contains("L") && path.x() > x && RightAbs > abs) {
                    neighbourStruct.Right = new Node(path.x(), path.y(), path.directions(), parent);
                    RightAbs = abs;
                }

                if (path.x() < x && abs < LeftAbs && !path.directions().contains("R")) {
                    neighbourStruct.Left = null;
                    LeftAbs = abs;
                }
                if (path.x() > x && abs < RightAbs && !path.directions().contains("L")) {
                    neighbourStruct.Right = null;
                    RightAbs = abs;
                }
            }
        }

        return neighbourStruct;
    }
}

class NeighbourStruct {
    Node Up;
    Node Left;
    Node Right;
    Node Down;
}

class Node {
    private NeighbourStruct neighbourStruct;

    private final int x;
    private final int y;

    private Node parent;

    private String directions = "";

    public Node(int x, int y) {
        this.x = x;
        this.y = y;
        this.parent = null;
    }

    public Node(int x, int y, String directions, Node parent) {
        this.x = x;
        this.y = y;
        this.directions = directions;
        this.parent = parent;
    }

    public int x() {
        return x;
    }

    public int y() {
        return y;
    }

    public Node parent() {
        return this.parent;
    }
    public void setParent(Node parent) {
        this.parent = parent;
    }

    public String directions() {
        return directions;
    }

    public void setNeighbours(NeighbourStruct neighbourStruct) {
        this.neighbourStruct = neighbourStruct;
    }

    public NeighbourStruct getKinds() {
        return neighbourStruct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Node node)) return false;
        return this.x == node.x && this.y == node.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        if (neighbourStruct == null) return "Node, " + directions + " | " + x + " | " + y;
        StringBuilder builder = new StringBuilder();
        builder.append("{\n\tNode, ").append(directions).append(" | ").append(x).append(" | ").append(y);

        if (neighbourStruct.Up != null) builder.append("\n\tUp:\n").append(neighbourStruct.Up.toString("\t\t"));
        if (neighbourStruct.Left != null) builder.append("\n\tLeft:\n").append(neighbourStruct.Left.toString("\t\t"));
        if (neighbourStruct.Right != null) builder.append("\n\tRight:\n").append(neighbourStruct.Right.toString("\t\t"));
        if (neighbourStruct.Down != null) builder.append("\n\tDown:\n").append(neighbourStruct.Down.toString("\t\t"));

        builder.append("\n}");

        return builder.toString();
    }

    public String toString(String tabs) {

        if (neighbourStruct == null) return "";
        StringBuilder builder = new StringBuilder();
        builder.append(tabs).append("{\n").append(tabs).append("\t").append("Node, ").append(directions).append(" | ").append(x).append(" | ").append(y);

        if (neighbourStruct.Up != null) builder.append("\n").append(tabs).append("\t").append("Up:\n").append(neighbourStruct.Up.toString(tabs + "\t"));
        if (neighbourStruct.Left != null) builder.append("\n").append(tabs).append("\t").append("Left:\n").append(neighbourStruct.Left.toString(tabs + "\t"));
        if (neighbourStruct.Right != null) builder.append("\n").append(tabs).append("\t").append("Right:\n").append(neighbourStruct.Right.toString(tabs + "\t"));
        if (neighbourStruct.Down != null) builder.append("\n").append(tabs).append("\t").append("Down:\n").append(neighbourStruct.Down.toString(tabs + "\t"));

        builder.append("\n").append(tabs).append("}");

        return builder.toString();

    }
}