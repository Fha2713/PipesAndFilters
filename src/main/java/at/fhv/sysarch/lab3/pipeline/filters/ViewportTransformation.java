package at.fhv.sysarch.lab3.pipeline.filters;

import at.fhv.sysarch.lab3.obj.Face;
import at.fhv.sysarch.lab3.pipeline.data.Pair;
import com.hackoeur.jglm.Mat4;
import com.hackoeur.jglm.Vec4;
import javafx.scene.paint.Color;

public class ViewportTransformation implements IPushFilter<Pair<Face, Color>> {

    private final Mat4 viewportTransform;
    private final float viewportHeight;
    private IPushFilter<Pair<Face, Color>> successor;

    public ViewportTransformation(Mat4 viewportTransform, float viewportHeight) {
        this.viewportTransform = viewportTransform;
        this.viewportHeight = viewportHeight;
    }

    @Override
    public void setSuccessor(IPushFilter<?> successor) {
        this.successor = (IPushFilter<Pair<Face, Color>>) successor;
    }

    @Override
    public void push(Pair<Face, Color> input) {
        Face f = input.fst();
        Color c = input.snd();

        Vec4 v1 = viewportTransform.multiply(f.getV1());
        Vec4 v2 = viewportTransform.multiply(f.getV2());
        Vec4 v3 = viewportTransform.multiply(f.getV3());

        Face transformed = new Face(v1, v2, v3, f.getN1(), f.getN2(), f.getN3());

        Pair<Face, Color> result = new Pair<>(transformed, c);
        successor.push(result);
    }
}
