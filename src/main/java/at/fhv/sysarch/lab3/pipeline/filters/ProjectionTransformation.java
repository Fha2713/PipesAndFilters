package at.fhv.sysarch.lab3.pipeline.filters;

import at.fhv.sysarch.lab3.obj.Face;
import at.fhv.sysarch.lab3.pipeline.data.Pair;
import com.hackoeur.jglm.Mat4;
import com.hackoeur.jglm.Vec4;
import javafx.scene.paint.Color;

public class ProjectionTransformation implements IPushFilter<Pair<Face, Color>> {

    private final Mat4 projMatrix;
    private IPushFilter<Pair<Face, Color>> successor;

    public ProjectionTransformation(Mat4 projMatrix) {
        this.projMatrix = projMatrix;
    }

    @Override
    public void setSuccessor(IPushFilter<?> successor) {
        this.successor = (IPushFilter<Pair<Face, Color>>) successor;
    }

    @Override
    public void push(Pair<Face, Color> input) {
        Face face = input.fst();
        Color color = input.snd();

        Vec4 v1 = projMatrix.multiply(face.getV1());
        Vec4 v2 = projMatrix.multiply(face.getV2());
        Vec4 v3 = projMatrix.multiply(face.getV3());

        Face transformed = new Face(v1, v2, v3, face.getN1(), face.getN2(), face.getN3());

        successor.push(new Pair<>(transformed, color));
    }
}
