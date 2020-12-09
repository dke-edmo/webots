package Graph;

import java.util.ArrayList;
import java.util.Iterator;

public class Graph {

    public String label = "";
    public ArrayList<Node> nodes;
    public Node centerOfMass = new Node("centerOfMass", 0,0,0);    //Default center of mass
    double mass = 0;

    public Graph(String label){
        this.label = label;
        this.nodes = new ArrayList<>();
    }

    /**
     * Here is a menu to call the different functions that the robot should call at every timestep
     */
    public void think(){
        computeCenterOfMass();
        //Do other stuff
    }

    /**
     * R = 1/M Sum{mi * ri}
     */
    public void computeCenterOfMass(){
        double xs = 0, ys = 0, zs = 0;

        Iterator<Node> iterator = this.nodes.iterator();
        while(iterator.hasNext()){
            Node node = iterator.next();
            xs += (node.X * node.mass);
            ys += (node.Y * node.mass);
            zs += (node.Z * node.mass);
        }
        this.centerOfMass.update(xs/this.mass, ys/this.mass, zs/this.mass);

    }

    public void addNode(Node node){

        Iterator<Node> iter = this.nodes.iterator();

        //add the first element
        if(this.nodes.size()==0){
            System.out.println("No nodes yet, let's add the first one: " + node.label);
            this.nodes.add(node);
        }
        else {  //or add others while checking for duplicates
            boolean flag = false;
            while (iter.hasNext()) {
                Node n2 = iter.next();
                if (node.label.equals(n2.label)) {
                    System.out.println("Can't be added. Two nodes must have different labels, node1(" + node.label+"), node2("+n2.label+")");
                    flag = true;
                }
            }
            if(!flag){
                System.out.println("Yes it is, can be added for Node(" + node.label + ")");
                this.nodes.add(node);
                this.mass += node.mass; //Update the mass of the graph
                System.out.println("mass: " + mass);
            }
        }


    }

    public void connectNodes(String node, String[] labels){
        Iterator<Node> iter1 = this.nodes.iterator();

        while(iter1.hasNext()){
            Node n = iter1.next();
            if(node.equals(n.label)){

                Iterator<Node> iter2 = this.nodes.iterator();
                while(iter2.hasNext()){
                    Node n2 = iter2.next();
                    for(int i=0; i< labels.length;i++){
                        if(n2.label.equals(labels[i])){
                            System.out.println("Found a match to connect: node(" + n.label + ") and node(" + n2.label +").");
                            n.addEdge(n2,1);
                            n2.addEdge(n,1);
                        }
                    }
                }

            }

        }

    }

    public void printNodes(){
        for(int i=0; i<this.nodes.size(); i++){
            System.out.print(this.nodes.get(i).label + ", ");
        }
        System.out.println("");
    }

}
