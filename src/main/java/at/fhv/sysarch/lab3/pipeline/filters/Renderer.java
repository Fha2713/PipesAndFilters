package at.fhv.sysarch.lab3.pipeline.filters;

import at.fhv.sysarch.lab3.obj.ColorFace;
import at.fhv.sysarch.lab3.obj.Face;
import at.fhv.sysarch.lab3.rendering.RenderingMode;
import com.hackoeur.jglm.Vec2;
import com.hackoeur.jglm.Vec4;
import javafx.scene.canvas.GraphicsContext;

public class Renderer implements IPushFilter<ColorFace> {

    private final GraphicsContext gc;
    private final RenderingMode renderingMode;

    public Renderer(GraphicsContext gc, RenderingMode renderingMode) {
        this.gc = gc;
        this.renderingMode = renderingMode;
    }

    @Override
    public void setSuccessor(IPushFilter successor) {
    }

    @Override
    public void push(ColorFace cf) {
        Face f = cf.getFace();
        Vec4 v1 = f.getV1();
        Vec4 v2 = f.getV2();
        Vec4 v3 = f.getV3();

        gc.setStroke(cf.getShadedColor());
        gc.setFill(cf.getShadedColor());

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
