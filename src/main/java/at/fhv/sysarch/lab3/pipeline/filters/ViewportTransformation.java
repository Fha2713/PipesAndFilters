package at.fhv.sysarch.lab3.pipeline.filters;

import at.fhv.sysarch.lab3.obj.ColorFace;
import at.fhv.sysarch.lab3.obj.Face;
import com.hackoeur.jglm.Mat4;
import com.hackoeur.jglm.Vec4;

public class ViewportTransformation implements IPushFilter<ColorFace> {

    private final Mat4 viewportTransform;
    private final float viewportHeight;
    private IPushFilter<ColorFace> successor;

    public ViewportTransformation(Mat4 viewportTransform, float viewportHeight) {
        this.viewportTransform = viewportTransform;
        this.viewportHeight = viewportHeight;

    }

    @Override
    public void setSuccessor(IPushFilter<?> successor) {
        this.successor = (IPushFilter<ColorFace>) successor;
    }

    @Override
    public void push(ColorFace cf) {
        Face f = cf.getFace();

        Vec4 v1 = viewportTransform.multiply(f.getV1());
        Vec4 v2 = viewportTransform.multiply(f.getV2());
        Vec4 v3 = viewportTransform.multiply(f.getV3());

        Face transformed = new Face(v1, v2, v3, f.getN1(), f.getN2(), f.getN3());

        ColorFace result = new ColorFace(transformed, cf.getColor(), cf.getIntensity());
        successor.push(result);
    }
}
