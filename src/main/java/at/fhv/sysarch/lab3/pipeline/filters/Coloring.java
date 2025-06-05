package at.fhv.sysarch.lab3.pipeline.filters;

import at.fhv.sysarch.lab3.obj.Face;
import at.fhv.sysarch.lab3.pipeline.data.Pair;
import javafx.scene.paint.Color;

public class Coloring implements IPushFilter<Face>, IPullFilter<Pair<Face, Color>> {

    private IPushFilter<Pair<Face, Color>> successor;
    private IPullFilter<Face> predecessor;
    private final Color modelColor;

    public Coloring(Color modelColor) {
        this.modelColor = modelColor;
    }

    @Override
    public void setSuccessor(IPushFilter<?> successor) {
        this.successor = (IPushFilter<Pair<Face, Color>>) successor;
    }

    @Override
    public void push(Face face) {
        Pair<Face, Color> colored = new Pair<>(face, modelColor);
        if (successor != null) successor.push(colored);
    }

    @Override
    public void setPredecessor(IPullFilter<?> predecessor) {
        this.predecessor = (IPullFilter<Face>) predecessor;
    }

    @Override
    public Pair<Face, Color> pull() {
        Face face = predecessor.pull();
        if (face == null) return null;
        return new Pair<>(face, modelColor);
    }
}
