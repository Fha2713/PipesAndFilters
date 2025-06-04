package at.fhv.sysarch.lab3.pipeline.filters;

import at.fhv.sysarch.lab3.obj.Face;
import com.hackoeur.jglm.*;

public class ModelViewTransformation implements IPushFilter<Face> {

    private IPushFilter<Face> successor;
    private Mat4 modelViewMatrix;
    //private Mat4 viewTransformed = null;

    public Mat4 rotate(float rad, Vec3 modelRotAxis) {
        return Matrices.rotate(rad, modelRotAxis);
    }

//    private Mat4 scale(float factor) {
//        return new Mat4(
//                new Vec4(factor, 0, 0, 0),
//                new Vec4(0, factor, 0, 0),
//                new Vec4(0, 0, factor, 0),
//                new Vec4(0, 0, 0, 1)
//        );
//    }

//    public Mat4 translate(Mat4 rotated, Mat4 modelTranslation) {
//        //Mat4 scaled = scale(50f);
//        return modelTranslation.multiply(rotated);
//    }
//
//    public void viewTranslate(Mat4 translated, Mat4 viewTransform) {
//        this.viewTransformed = viewTransform.multiply(translated);
//    }

    public void setModelViewMatrix(Mat4 modelViewMatrix) {
        this.modelViewMatrix = modelViewMatrix;
    }

    @Override
    public void setSuccessor(IPushFilter<?> successor) {
        this.successor = (IPushFilter<Face>) successor;
    }

    @Override
    public void push(Face face) {
        Vec4 v1new = modelViewMatrix.multiply(face.getV1());
        Vec4 v2new = modelViewMatrix.multiply(face.getV2());
        Vec4 v3new = modelViewMatrix.multiply(face.getV3());
        Vec4 n1new = modelViewMatrix.multiply(face.getN1());
        Vec4 n2new = modelViewMatrix.multiply(face.getN2());
        Vec4 n3new = modelViewMatrix.multiply(face.getN3());

        Face transformedFace = new Face(v1new, v3new, v2new, n1new, n3new, n2new);

        successor.push(transformedFace);
    }
}
