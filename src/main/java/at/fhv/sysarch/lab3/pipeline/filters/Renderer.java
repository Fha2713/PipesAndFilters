package at.fhv.sysarch.lab3.pipeline.filters;

import at.fhv.sysarch.lab3.obj.Face;
import at.fhv.sysarch.lab3.pipeline.data.Pair;
import at.fhv.sysarch.lab3.rendering.RenderingMode;
import com.hackoeur.jglm.Vec4;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Renderer implements IPushFilter<Pair<Face, Color>> {

    private final GraphicsContext gc;
    private final RenderingMode renderingMode;

    public Renderer(GraphicsContext gc, RenderingMode renderingMode) {
        this.gc = gc;
        this.renderingMode = renderingMode;
    }

    @Override
    public void setSuccessor(IPushFilter<?> successor) {
        // Renderer ist Sink, hat keinen Nachfolger
    }

    @Override
    public void push(Pair<Face, Color> input) {
        Face face = input.fst();
        Color color = input.snd();

        Vec4 v1 = face.getV1();
        Vec4 v2 = face.getV2();
        Vec4 v3 = face.getV3();

        gc.setStroke(color);
        gc.setFill(color);

        switch (renderingMode) {
            case POINT -> {
                gc.strokeLine(v1.getX(), v1.getY(), v1.getX(), v1.getY());
                gc.strokeLine(v2.getX(), v2.getY(), v2.getX(), v2.getY());
                gc.strokeLine(v3.getX(), v3.getY(), v3.getX(), v3.getY());
            }
            case WIREFRAME -> gc.strokePolygon(
                    new double[]{v1.getX(), v2.getX(), v3.getX()},
                    new double[]{v1.getY(), v2.getY(), v3.getY()},
                    3
            );
            case FILLED -> gc.fillPolygon(
                    new double[]{v1.getX(), v2.getX(), v3.getX()},
                    new double[]{v1.getY(), v2.getY(), v3.getY()},
                    3
            );
        }
    }
}
