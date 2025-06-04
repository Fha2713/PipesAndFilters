package at.fhv.sysarch.lab3.pipeline.filters;

import at.fhv.sysarch.lab3.obj.Face;
import at.fhv.sysarch.lab3.pipeline.data.Pair;
import com.hackoeur.jglm.Mat4;
import com.hackoeur.jglm.Vec4;
import javafx.scene.paint.Color;

public class ScreenSpaceTransformation implements IPushFilter<Pair<Face, Color>> {

    private IPushFilter<Pair<Face, Color>> successor;
    private final Mat4 viewportTransform;
    private final float viewportHeight;

    public ScreenSpaceTransformation(Mat4 viewportTransform, float viewportHeight) {
        this.viewportTransform = viewportTransform;
        this.viewportHeight = viewportHeight;
    }

    @Override
    public void setSuccessor(IPushFilter<?> successor) {
        this.successor = (IPushFilter<Pair<Face, Color>>) successor;
    }

    @Override
    public void push(Pair<Face, Color> input) {
        Face face = input.fst();
        Color color = input.snd();

        Face screenFace = applyScreenSpaceTransformation(face);

        if (successor != null) {
            successor.push(new Pair<>(screenFace, color));
        }
    }

    private Face applyScreenSpaceTransformation(Face face) {
        Vec4 v1 = perspectiveDivide(face.getV1());
        Vec4 v2 = perspectiveDivide(face.getV2());
        Vec4 v3 = perspectiveDivide(face.getV3());

        v1 = viewportTransform.multiply(v1);
        v2 = viewportTransform.multiply(v2);
        v3 = viewportTransform.multiply(v3);

        return new Face(v1, v2, v3, face);
    }

    private Vec4 perspectiveDivide(Vec4 v) {
        return new Vec4(v.getX() / v.getW(), v.getY() / v.getW(), v.getZ() / v.getW(), 1.0f);
    }
}

