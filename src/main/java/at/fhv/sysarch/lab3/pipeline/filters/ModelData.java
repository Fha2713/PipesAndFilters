package at.fhv.sysarch.lab3.pipeline.filters;

import at.fhv.sysarch.lab3.obj.Face;
import at.fhv.sysarch.lab3.obj.Model;

public class ModelData implements IPushFilter<Void> {

    private IPushFilter<Face> successor;

    @Override
    public void setSuccessor(IPushFilter<?> successor) {
        this.successor = (IPushFilter<Face>) successor;
    }

    @Override
    public void push(Void ignored) {
        throw new UnsupportedOperationException("ModelSource is a source and cannot receive input.");
    }

    public void run(Model model) {
        model.getFaces().forEach(successor::push);
    }


}
