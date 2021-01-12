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

        try {
            new Structure(
                new Connection(moduleA, moduleL, Connection.Connector.L, Connection.Connector.B, Connection.Orientation.H),
                new Connection(moduleA, moduleR, Connection.Connector.R, Connection.Connector.B, Connection.Orientation.H),
                new Connection(moduleL, moduleR, Connection.Connector.T, Connection.Connector.T, Connection.Orientation.H)
            );
        } catch (Exception e) {
            System.out.println("Expected exception: " + e.getMessage());
        }

        try {
            Module moduleA2 = new Module();
            Module moduleL2 = new Module();
            Module moduleR2 = new Module();
            new Structure(
                new Connection(moduleA, moduleL, Connection.Connector.L, Connection.Connector.B, Connection.Orientation.H),
                new Connection(moduleA, moduleR, Connection.Connector.R, Connection.Connector.B, Connection.Orientation.H),
                new Connection(moduleA2, moduleL2, Connection.Connector.L, Connection.Connector.B, Connection.Orientation.H),
                new Connection(moduleA2, moduleR2, Connection.Connector.R, Connection.Connector.B, Connection.Orientation.H)
            );
        } catch (Exception e) {
            System.out.println("Expected exception: " + e.getMessage());
        }

        try {
            new Structure(
                new Connection(moduleA, moduleL, Connection.Connector.T, Connection.Connector.B, Connection.Orientation.H),
                new Connection(moduleA, moduleR, Connection.Connector.T, Connection.Connector.B, Connection.Orientation.H)
            );
        } catch (Exception e) {
            System.out.println("Expected exception: " + e.getMessage());
        }

    }
}
