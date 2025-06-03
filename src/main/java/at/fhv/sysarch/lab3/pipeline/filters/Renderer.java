package at.fhv.sysarch.lab3.pipeline.filters;

import at.fhv.sysarch.lab3.obj.Face;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Renderer implements IPushFilter<Face> {

    private final Color color;
    private final GraphicsContext gc;

    public Renderer(GraphicsContext gc, Color color) {
        this.gc = gc;
        this.color = color;
    }

    @Override
    public void setSuccessor(IPushFilter successor) {
    }

    @Override
    public void push(Face f) {
        gc.setStroke(color);

        gc.strokeLine(f.getV1().getX(), f.getV1().getY(), f.getV2().getX(), f.getV2().getY());
        gc.strokeLine(f.getV1().getX(), f.getV1().getY(), f.getV3().getX(), f.getV3().getY());
        gc.strokeLine(f.getV2().getX(), f.getV2().getY(), f.getV3().getX(), f.getV3().getY());
    }
}
