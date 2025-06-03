package at.fhv.sysarch.lab3.pipeline.filters;

import at.fhv.sysarch.lab3.obj.Face;
import at.fhv.sysarch.lab3.pipeline.PipelineData;
import com.hackoeur.jglm.*;

public class ModelTransformation implements IPushFilter<Face> {

    private IPushFilter<Face> successor;
    private Mat4 viewTransformed = null;

    public Mat4 rotate(float rad, Vec3 modelRotAxis) {
        return Matrices.rotate(rad, modelRotAxis);
    }

    private Mat4 scale(float factor) {
        return new Mat4(
                new Vec4(factor, 0, 0, 0),
                new Vec4(0, factor, 0, 0),
                new Vec4(0, 0, factor, 0),
                new Vec4(0, 0, 0, 1)
        );
    }

    public Mat4 translate(Mat4 rotated, Mat4 modelTranslation) {
        Mat4 scaled = scale(50f);
        return modelTranslation.multiply(rotated).multiply(scaled);
    }

    public void viewTranslate(Mat4 translated, Mat4 viewTransform) {
        this.viewTransformed = viewTransform.multiply(translated);
    }

    @Override
    public void setSuccessor(IPushFilter<?> successor) {
        this.successor = (IPushFilter<Face>) successor;
    }


    @Override
    public void push(Face f) {

        Vec4 v1 = viewTransformed.multiply(f.getV1());
        Vec4 v2 = viewTransformed.multiply(f.getV2());
        Vec4 v3 = viewTransformed.multiply(f.getV3());

        Vec4 n1 = viewTransformed.multiply(f.getN1());
        Vec4 n2 = viewTransformed.multiply(f.getN2());
        Vec4 n3 = viewTransformed.multiply(f.getN3());

        Face transformed = new Face(v1, v2, v3, n1, n2, n3);

        successor.push(transformed);
    }

}
