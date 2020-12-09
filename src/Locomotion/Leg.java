package Locomotion;

import Graph.Node;

public class Leg {

    private Node root;
    private Node left;
    private Node right;

    private double distLeftRoot;
    private double distRightRoot;

    private double angleMotor;

    private double minAngle;
    private double maxAngle;


    /*
        Defines a set of three nodes linked as a balanced tree for teh sake of representation.
        Such that the root node represent the servo motor actuating the leg:

           O  --> Root Motor
         /   \
        O     O --> End points nodes

        The root motor is the actuator of the Leg.
        End points are not considered here as motors, but the end nodes can be reused in other Legs as motors.
        We only keep track of that Leg for actuation.

     */


    public Leg(Node left, Node root, Node right, double minAngle, double maxAngle){
        this.left = left;
        this.root = root;
        this.right = right;

        this.distLeftRoot = Math.abs(this.root.getDistance(this.left));         //Fixed size of parts, must never change
        this.distRightRoot = Math.abs(this.root.getDistance(this.right));

        //From the Nodes position values, compute the motor's angle
        this.updateAngle();

        this.minAngle = minAngle;
        this.maxAngle = maxAngle;
    }

    //Updates angle by checking the evolution of the Nodes Coordinates
    public double updateAngle(){
        double distRightLeft = Math.abs(this.left.getDistance(this.right)); //Only this distance changes when the motor angle changes

        //Pythagoreus
        double angle = Math.acos(( Math.pow(distLeftRoot, 2) + Math.pow(distRightRoot, 2) - Math.pow(distRightLeft, 2) ) / (2 * distLeftRoot * distRightRoot) );

        //reupdates the struct angle
        this.angleMotor = angle;

        return this.angleMotor;
    }

    public void incrementmotorAngle(double incr){

        this.angleMotor += incr;

    }

}
