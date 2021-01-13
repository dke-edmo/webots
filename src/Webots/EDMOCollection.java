package Webots;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class EDMOCollection {

    private List<EDMO> edmos;

    public EDMOCollection(EDMO ...edmos) {
        this(List.of(edmos));
    }

    public EDMOCollection(List<EDMO> edmos) {
        this.edmos = new ArrayList<>(edmos);
    }

    public void add(EDMO edmo) {
        edmos.add(edmo);
    }

    public void add(EDMOCollection edmos) {
        this.edmos.addAll(edmos.getEdmos());
    }

    public List<EDMO> getEdmos() {
        return edmos;
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
