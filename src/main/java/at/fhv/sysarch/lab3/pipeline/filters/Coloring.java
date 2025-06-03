package at.fhv.sysarch.lab3.pipeline.filters;

import at.fhv.sysarch.lab3.obj.ColorFace;
import at.fhv.sysarch.lab3.obj.Face;
import javafx.scene.paint.Color;

public class Coloring implements IPushFilter<Face> {

    private final Color color;
    private IPushFilter<ColorFace> successor;

    public Coloring(Color color) {
        this.color = color;
    }

    @Override
    public void setSuccessor(IPushFilter<?> successor) {
        this.successor = (IPushFilter<ColorFace>) successor;
    }

    @Override
    public void push(Face f) {
        ColorFace colored = new ColorFace(f, color, 1.0f); // intensity = 1.0 → wird später ersetzt
        successor.push(colored);
    }
}
