package at.fhv.sysarch.lab3.pipeline.filters;

import at.fhv.sysarch.lab3.obj.Face;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class DepthSorting implements IPushFilter<Face> {

    private final List<Face> buffer = new ArrayList<>();
    private IPushFilter<Face> successor;

    @Override
    public void setSuccessor(IPushFilter<?> successor) {
        this.successor = (IPushFilter<Face>) successor;
    }

    @Override
    public void push(Face f) {
        buffer.add(f);
    }

    // Muss am Ende eines Frames aufgerufen werden!
    public void flush() {
        buffer.stream()
                .sorted(Comparator.comparingDouble(this::averageZ).reversed()) // von hinten nach vorne
                .forEach(successor::push);

        buffer.clear(); // wichtig für nächsten Frame!
    }

    private double averageZ(Face face) {
        double z1 = face.getV1().getZ();
        double z2 = face.getV2().getZ();
        double z3 = face.getV3().getZ();
        return (z1 + z2 + z3) / 3.0;
    }

}
