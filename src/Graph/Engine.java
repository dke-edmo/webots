package Graph;

import javax.swing.*;
import java.util.ArrayList;

public class Engine {

    JFrame frame;
   // GUI gui;
    public final int TIMESTEP = 1000/30;   //in Milliseconds
    double angle = 0;

    public Engine(){

        //Create an Object Factory which will produce the graph for the robot, and possibly later objects
        //Specify which robot you want to use
        ObjectFactory factory = new ObjectFactory("robot2");
        //Retrieve the robot
        Graph graph = factory.getRobot();

        //Display the nodes belonging to that object, which is the robot
        graph.printNodes();

        ArrayList<Node> nodeList = new ArrayList<>();

        //Set up a GUI to see things. It's fun.
        //gui_interface(graph, nodeList);

        //Main loop which mimics the one from arduino. Computation etc MUST come from this loop
        while(true){
            loop(graph, nodeList);
        }

    }
    //Main Loop function, similar to what Arduino is doing
    public void loop(Graph robot, ArrayList<Node> nodeList){
        //Do some computation (Self-aware, sensors, actions, ...)
        robot.think();

        System.out.println("Center of mass of robot is: " + robot.centerOfMass.dispCoords());

      /*
        //Update the gui
        gui.sendData(robot, nodeList);
        // gui.addScale(new double[]{0.1,0,0});

        //Just messing around with visual effects, you can get rid of these :)
        angle += 0.02;
        gui.rotateX(Math.PI*angle);
        gui.rotateY(Math.PI*angle);
        gui.rotateZ(Math.PI*angle);

        gui.repaint();
        System.out.println("This is a step");*/
      //  wait(TIMESTEP);
    }

    //Function that just pauses the program for a certain time and deals with the timestep
    public void wait(int ms) {
        try { Thread.sleep(ms); }
        catch(InterruptedException ex) { Thread.currentThread().interrupt(); }
    }


   /* public void gui_interface(Graph robot, ArrayList<Node> nodeList){
        frame = new JFrame("Interface");
        frame.setSize(1000,800);
        gui = new GUI(1000,800);
        gui.sendData(robot, nodeList);
        frame.add(gui);
        frame.setVisible(true);
    }*/



}
