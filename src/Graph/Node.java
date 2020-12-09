package Graph;

import java.util.ArrayList;
import java.util.Iterator;

public class Node {

    String label;
    double X,Y,Z;
    ArrayList<Node> endPoints;
    double mass = 0;            //in grams

    public Node( String label, double X, double Y, double Z){
        this.label = label;
        this.X = X;
        this.Y = Y;
        this.Z = Z;
        this.endPoints = new ArrayList<>();
    }

    public void update(double X, double Y, double Z){
        this.X = X;
        this.Y = Y;
        this.Z = Z;
    }

    public void addEdge(Node node, double length){
        Iterator<Node> iter = this.endPoints.iterator();
        while(iter.hasNext()){
            Node n = iter.next();
            if(n.equals(node)){
                System.out.println("Node " + this.label + " is already connected to Node " + node.label);
                return;
            }
        }

        //If the distance exceeds the specified length, rejects. Possibly change to have it match exactly
        if(getDistance(node) <= length){
            System.out.println("Point can be added as an edge: " + node.label + " distance is + " + this.getDistance(node));
            this.endPoints.add(node);
        } else {
            System.out.println("The point cannot be added as an edge for Node " + this.label + ": \"" + node.label + "\" distance is: " + this.getDistance(node));
        }

    }

    public void addMass(double mass){
        this.mass = mass;
    }

    public double getDistance(Node node){
        return Math.sqrt(Math.pow(node.X-this.X, 2) + Math.pow(node.Y-this.Y, 2) + Math.pow(node.Z-this.Z, 2));
    }

    public String dispCoords(){
        return "(" + this.X + ", " + this.Y + ", " + this.Z + ")";
    }

}
