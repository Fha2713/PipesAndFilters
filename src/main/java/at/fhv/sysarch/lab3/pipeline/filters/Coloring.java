package at.fhv.sysarch.lab3.pipeline.filters;

import at.fhv.sysarch.lab3.obj.Face;
import at.fhv.sysarch.lab3.pipeline.data.Pair;
import javafx.scene.paint.Color;

//public class Coloring implements IPushFilter<Face> {
//
//    private IPushFilter<Pair<Face, Color>> successor;
//    private final Color color;
//
//    public Coloring(Color color) {
//        this.color = color;
//    }
//
//    @Override
//    public void setSuccessor(IPushFilter<?> successor) {
//        this.successor = (IPushFilter<Pair<Face, Color>>) successor;
//    }
//
//    @Override
//    public void push(Face face) {
//        Pair<Face, Color> colored = new Pair<>(face, color);
//
//        successor.push(colored);
//    }
//}
