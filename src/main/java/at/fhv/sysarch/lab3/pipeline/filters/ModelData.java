package at.fhv.sysarch.lab3.pipeline.filters;

import at.fhv.sysarch.lab3.obj.Face;
import at.fhv.sysarch.lab3.obj.FaceFinished;
import at.fhv.sysarch.lab3.obj.Model;

import java.util.Deque;
import java.util.Iterator;

public class ModelData implements IPushFilter<Void>, IPullFilter<Face> {

    private Iterator<Face> faceIterator;
    private IPushFilter<Face> successor;
    private Deque<Face> faces;


    // Quelle bekommt ein Model, initialisiert Iterator
    public void setModel(Model model) {
        this.faceIterator = model.getFaces().iterator();
    }

    // === Push-Seite ===
    @Override
    public void setSuccessor(IPushFilter<?> successor) {
        this.successor = (IPushFilter<Face>) successor;
    }

    @Override
    public void push(Void ignored) {
        if (faceIterator == null) throw new IllegalStateException("Model not set");
        while (faceIterator.hasNext()) {
            Face face = faceIterator.next();
            successor.push(face);
        }
        // Optional: Endsignal z.B. FaceFinished senden
        successor.push(new FaceFinished(null,null,null,null,null,null));
    }

    // === Pull-Seite ===
    @Override
    public void setPredecessor(IPullFilter<?> predecessor) {
        throw new UnsupportedOperationException("ModelData is a source");
    }

    @Override
    public Face pull() {
        if (faceIterator != null && faceIterator.hasNext()) {
            return faceIterator.next();
        } else {
            return null;
        }
    }

    public void run(Model model) {
        model.getFaces().forEach(successor::push);
    }
}
