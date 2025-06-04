package at.fhv.sysarch.lab3.pipeline.filters;

import at.fhv.sysarch.lab3.obj.Face;
import at.fhv.sysarch.lab3.pipeline.data.Pair;
import at.fhv.sysarch.lab3.rendering.RenderingMode;
import com.hackoeur.jglm.Vec2;
import com.hackoeur.jglm.Vec4;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Renderer implements IPushFilter<Pair<Face, Color>>, IPullFilter<Pair<Face, Color>> {

    private IPullFilter<Pair<Face, Color>> predecessor;
    private final GraphicsContext graphicsContext;
    private final RenderingMode renderingMode;

    public Renderer(GraphicsContext gc, RenderingMode mode) {
        this.graphicsContext = gc;
        this.renderingMode = mode;
    }

    // --- Push ---
    @Override
    public void setSuccessor(IPushFilter<?> successor) {
        // Renderer ist Sink -> kein Nachfolger
    }

    @Override
    public void push(Pair<Face, Color> input) {
        drawFace(input.fst(), input.snd());
    }

    // --- Pull ---
    @Override
    public void setPredecessor(IPullFilter<?> predecessor) {
        this.predecessor = (IPullFilter<Pair<Face, Color>>) predecessor;
    }

    @Override
    public Pair<Face, Color> pull() {
        Pair<Face, Color> input = predecessor.pull();
        if (input != null) {
            drawFace(input.fst(), input.snd());
        }
        return input;
    }

    // === Zeichenmethode ===
    private void drawFace(Face face, Color color) {
        Vec2 v1 = toScreenCoordinates(face.getV1());
        Vec2 v2 = toScreenCoordinates(face.getV2());
        Vec2 v3 = toScreenCoordinates(face.getV3());

        graphicsContext.setStroke(color);
        graphicsContext.setFill(color);

        switch (renderingMode) {
            case POINT -> {
                graphicsContext.strokeLine(v1.getX(), v1.getY(), v1.getX(), v1.getY());
                graphicsContext.strokeLine(v2.getX(), v2.getY(), v2.getX(), v2.getY());
                graphicsContext.strokeLine(v3.getX(), v3.getY(), v3.getX(), v3.getY());
            }
            case WIREFRAME -> graphicsContext.strokePolygon(
                    new double[]{v1.getX(), v2.getX(), v3.getX()},
                    new double[]{v1.getY(), v2.getY(), v3.getY()},
                    3
            );
            case FILLED -> graphicsContext.fillPolygon(
                    new double[]{v1.getX(), v2.getX(), v3.getX()},
                    new double[]{v1.getY(), v2.getY(), v3.getY()},
                    3
            );
        }
    }

    private Vec2 toScreenCoordinates(Vec4 vec) {
        return new Vec2(vec.getX(), vec.getY());
    }

    public void renderAll() {
        Pair<Face, Color> item;
        while ((item = predecessor.pull()) != null) {
            drawFace(item.fst(), item.snd());
        }
    }
}
