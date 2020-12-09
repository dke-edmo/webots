package Graph;

public class ObjectFactory {

    String robot_type = "";     //Specify what robot type you want

    public ObjectFactory(String robot_type){
        this.robot_type = robot_type;
        System.out.println("Chosen robot is: " + this.robot_type);
    }

    public Graph getRobot(){
        if(this.robot_type.equals("robot1")){return robot1();}
        else if(this.robot_type.equals("robot2")){return robot2();}
        else{
            System.out.println("Wrong label of robot input");
            System.out.close();
        }

        return robot1();
    }

    public Graph robot1(){
        //Set up some Nodes
        Node a = new Node("a", 0,0,0);
        Node b = new Node("b", 1,0,0);
        Node c = new Node("c", 2,0,0);
        Node d = new Node("d", 3,0,0);
        Node e = new Node("e", 4,0,0);
        Node f = new Node("f", 0,1,0);
        Node g = new Node("g", 0,2,0);
        Node h = new Node("h", 0,2.45,-0.5);
        //Create a graph structure representing a robot
        Graph graph = new Graph(this.robot_type);
        //Add some mass to them
        a.addMass(1);
        b.addMass(1);
        c.addMass(1);
        d.addMass(1);
        e.addMass(1);
        f.addMass(1);
        g.addMass(1);
        h.addMass(1);
        //Add the nodes to the graph
        graph.addNode(a);
        graph.addNode(b);
        graph.addNode(c);
        graph.addNode(d);
        graph.addNode(e);
        graph.addNode(f);
        graph.addNode(g);
        graph.addNode(h);

        //Here is an example on how to connect nodes via edges
        //Specify what is your main node, and what are the other endpoints
        //TODO --> Change rules regarding weight of edges for their acceptance
        //Example: we only accept edges of length 1 max. So c and f are rejected, but b is accepted.
        graph.connectNodes("a", new String[]{"b","c","f"});

        //Connect some more
        graph.connectNodes("b", new String[]{"a","c"});
        graph.connectNodes("c", new String[]{"b","d"});
        graph.connectNodes("d", new String[]{"c","e"});
        graph.connectNodes("f", new String[]{"g"});
        graph.connectNodes("g", new String[]{"h","f"});
        //Nodes e and f have already been connected above, so no need to put them again :)

        return graph;
    }

    public Graph robot2(){
        Node a = new Node("a", 0,1,0);
        Node b = new Node("b", 1,1,0);
        Node c = new Node("c", 1,1,1);
        Node d = new Node("d", 0,1,1);
        Node e = new Node("e", 0,0,0);
        Node f = new Node("f", 1,0,0);
        Node g = new Node("g", 1,0,1);
        Node h = new Node("h", 0,0,1);
        //Create a graph structure representing a robot
        Graph graph = new Graph(this.robot_type);
        //Add some mass to them
        a.addMass(1);
        b.addMass(1);
        c.addMass(1);
        d.addMass(1);
        e.addMass(1.5);
        f.addMass(1.5);
        g.addMass(1.5);
        h.addMass(1.5);

        //Add the nodes to the graph
        graph.addNode(a);
        graph.addNode(b);
        graph.addNode(c);
        graph.addNode(d);
        graph.addNode(e);
        graph.addNode(f);
        graph.addNode(g);
        graph.addNode(h);

        graph.connectNodes("a", new String[]{"e","b","d"});
        graph.connectNodes("b", new String[]{"a","f","c"});
        graph.connectNodes("c", new String[]{"f","d","b"});
        graph.connectNodes("d", new String[]{"h","c","a"});

        return graph;
    }

}
