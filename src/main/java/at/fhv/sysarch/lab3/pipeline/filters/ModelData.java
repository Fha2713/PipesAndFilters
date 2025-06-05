package at.fhv.sysarch.lab3.pipeline.filters;

import at.fhv.sysarch.lab3.obj.Face;
import at.fhv.sysarch.lab3.obj.Model;

import java.util.Iterator;

public class ModelData implements IPushFilter<Face>, IPullFilter<Face> {

    private Iterator<Face> faceIterator;
    private IPushFilter<Face> successor;

    @Override
    public void setSuccessor(IPushFilter<?> successor) {
        this.successor = (IPushFilter<Face>) successor;
    }

    @Override
    public void push(Face data) {
        throw new UnsupportedOperationException("ModelSource cannot receive pushed data.");
    }

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

    public void setModel(Model model) {
        this.faceIterator = model.getFaces().iterator();
    }

    public void run(Model model) {
        model.getFaces().forEach(successor::push);
    }
}
