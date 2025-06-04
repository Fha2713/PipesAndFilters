package at.fhv.sysarch.lab3.pipeline.filters;

import at.fhv.sysarch.lab3.obj.Face;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class DepthSorting implements IPushFilter<Face> {

    private final List<Face> faceBuffer = new ArrayList<>();
    private IPushFilter<Face> successor;

    @Override
    public void setSuccessor(IPushFilter<?> successor) {
        this.successor = (IPushFilter<Face>) successor;
    }

    @Override
    public void push(Face face) {
        faceBuffer.add(face);  // Nur zwischenspeichern
    }

    /**
     * Diese Methode muss explizit vom aufrufenden System pro Frame aufgerufen werden.
     */
    public void flush() {
        faceBuffer.sort(Comparator.comparingDouble(this::averageDepth).reversed());
        for (Face face : faceBuffer) {
            if (successor != null) {
                successor.push(face);
            }
        }
        faceBuffer.clear();
    }

    private double averageDepth(Face face) {
        return (face.getV1().getZ() + face.getV2().getZ() + face.getV3().getZ()) / 3.0;
    }
}
