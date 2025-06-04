package at.fhv.sysarch.lab3.pipeline.filters;

import at.fhv.sysarch.lab3.obj.ColorFace;
import at.fhv.sysarch.lab3.obj.Face;
import com.hackoeur.jglm.Mat4;
import com.hackoeur.jglm.Vec4;

public class ProjectionTransformation implements IPushFilter<ColorFace>{

    private final Mat4 projMatrix;
    private IPushFilter<ColorFace> successor;

    public ProjectionTransformation(Mat4 projMatrix) {
        this.projMatrix = projMatrix;
    }

    @Override
    public void setSuccessor(IPushFilter<?> successor) {
        this.successor = (IPushFilter<ColorFace>) successor;
    }

    @Override
    public void push(ColorFace cf) {
        // Vertices transformieren (view → clip space)
        Vec4 v1 = projMatrix.multiply(cf.getFace().getV1());
        Vec4 v2 = projMatrix.multiply(cf.getFace().getV2());
        Vec4 v3 = projMatrix.multiply(cf.getFace().getV3());

        // Normalen bleiben unverändert (nicht projiziert)
        Face transformed = new Face(v1, v2, v3, cf.getFace().getN1(), cf.getFace().getN2(), cf.getFace().getN3());

        ColorFace result = new ColorFace(transformed, cf.getColor(), cf.getIntensity());

        successor.push(result);
    }
}
