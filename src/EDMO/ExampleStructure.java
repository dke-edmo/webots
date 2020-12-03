package EDMO;

/**
 * @author Tomasz Darmetko
 */
public class ExampleStructure {
    public static void main(String[] args) {

        Module moduleA = new Module();
        Module moduleL = new Module();
        Module moduleR = new Module();

        new Structure(
            new Connection(moduleA, moduleL, Connection.Connector.L, Connection.Connector.B, Connection.Orientation.H),
            new Connection(moduleA, moduleR, Connection.Connector.R, Connection.Connector.B, Connection.Orientation.H)
        );

    }
}
