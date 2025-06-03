package at.fhv.sysarch.lab3.pipeline.filters;

import at.fhv.sysarch.lab3.obj.ColorFace;
import at.fhv.sysarch.lab3.obj.Face;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Renderer implements IPushFilter<ColorFace> {

    private final GraphicsContext gc;

    public Renderer(GraphicsContext gc, Color color) {
        this.gc = gc;
    }

    @Override
    public void setSuccessor(IPushFilter successor) {
    }

    @Override
    public void push(ColorFace cf) {

        Face f = cf.getFace();
        gc.setStroke(cf.getShadedColor());

        gc.strokeLine(f.getV1().getX(), f.getV1().getY(), f.getV2().getX(), f.getV2().getY());
        gc.strokeLine(f.getV1().getX(), f.getV1().getY(), f.getV3().getX(), f.getV3().getY());
        gc.strokeLine(f.getV2().getX(), f.getV2().getY(), f.getV3().getX(), f.getV3().getY());
    }
}
