package Webots;

import java.util.List;
import java.util.stream.Stream;

public class EDMOCollection {

    List<EDMO> edmos;

    public EDMOCollection(EDMO ...edmos) {
        this(List.of(edmos));
    }

    public EDMOCollection(List<EDMO> edmos) {
        this.edmos = edmos;
    }

    public void emit(double hingePosition) {
        getStream().forEach(e -> e.getCommunicator().emit(hingePosition));
    }

    public void clearReceiver() {
        getStream().forEach(e -> e.getCommunicator().clearReceiver());
    }

    public Stream<EDMO> getStream() {
        return edmos.stream();
    }

}
