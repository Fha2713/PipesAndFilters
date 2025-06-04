package at.fhv.sysarch.lab3.pipeline.filters;

import at.fhv.sysarch.lab3.obj.Face;
import at.fhv.sysarch.lab3.pipeline.data.Pair;
import com.hackoeur.jglm.Mat4;
import com.hackoeur.jglm.Vec4;
import javafx.scene.paint.Color;

public class ViewportTransformation implements IPushFilter<Pair<Face, Color>>, IPullFilter<Pair<Face, Color>> {

    private final Mat4 viewportTransform;
    private final float viewportHeight;
    private IPushFilter<Pair<Face, Color>> successor;
    private IPullFilter<Pair<Face, Color>> predecessor;

    public ViewportTransformation(Mat4 viewportTransform, float viewportHeight) {
        this.viewportTransform = viewportTransform;
        this.viewportHeight = viewportHeight;
    }

    // Push-Teil
    @Override
    public void setSuccessor(IPushFilter<?> successor) {
        this.successor = (IPushFilter<Pair<Face, Color>>) successor;
    }

    @Override
    public void push(Pair<Face, Color> input) {
        Pair<Face, Color> output = transform(input);
        if (successor != null) {
            successor.push(output);
        }
    }

    // Pull-Teil
    @Override
    public void setPredecessor(IPullFilter<?> predecessor) {
        this.predecessor = (IPullFilter<Pair<Face, Color>>) predecessor;
    }

    @Override
    public Pair<Face, Color> pull() {
        if (predecessor == null) return null;

        Pair<Face, Color> input = predecessor.pull();
        if (input == null) return null;

        return transform(input);
    }

    // Transformation (gleich für push und pull)
    private Pair<Face, Color> transform(Pair<Face, Color> input) {
        Face f = input.fst();
        Color c = input.snd();

        Vec4 v1 = viewportTransform.multiply(f.getV1());
        Vec4 v2 = viewportTransform.multiply(f.getV2());
        Vec4 v3 = viewportTransform.multiply(f.getV3());

        Face transformed = new Face(v1, v2, v3, f.getN1(), f.getN2(), f.getN3());

        return new Pair<>(transformed, c);
    }
}
