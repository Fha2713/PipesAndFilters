package at.fhv.sysarch.lab3.pipeline.filters;

import at.fhv.sysarch.lab3.obj.Face;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class DepthSorting implements IPushFilter<Face>, IPullFilter<Face> {

    private final List<Face> faceBuffer = new ArrayList<>();
    private IPushFilter<Face> successor;
    private IPullFilter<Face> predecessor;

    private Iterator<Face> sortedIterator = null;

    @Override
    public void setSuccessor(IPushFilter<?> successor) {
        this.successor = (IPushFilter<Face>) successor;
    }

    @Override
    public void push(Face face) {
        faceBuffer.add(face);  // Zwischenspeichern
    }

    @Override
    public void setPredecessor(IPullFilter<?> predecessor) {
        this.predecessor = (IPullFilter<Face>)predecessor;
    }

    @Override
    public Face pull() {
        if (sortedIterator == null || !sortedIterator.hasNext()) {
            fillAndSortFromPredecessor();
        }

        return (sortedIterator != null && sortedIterator.hasNext())
                ? sortedIterator.next()
                : null;
    }

    public void flush() {
        sortBuffer();
        while (sortedIterator.hasNext()) {
            Face face = sortedIterator.next();
            if (successor != null) {
                successor.push(face);
            }
        }
        faceBuffer.clear();
        sortedIterator = null;
    }

    private void fillAndSortFromPredecessor() {
        faceBuffer.clear();
        if (predecessor == null) return;

        Face face;
        while ((face = predecessor.pull()) != null) {
            faceBuffer.add(face);
        }
        sortBuffer();
    }

    private void sortBuffer() {
        faceBuffer.sort(Comparator.comparingDouble(this::averageDepth));
        sortedIterator = faceBuffer.iterator();
    }

    private double averageDepth(Face face) {
        return (face.getV1().getZ() + face.getV2().getZ() + face.getV3().getZ()) / 3.0;
    }
}
