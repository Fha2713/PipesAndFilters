package at.fhv.sysarch.lab3.pipeline.filters;

import at.fhv.sysarch.lab3.obj.Face;
import com.hackoeur.jglm.*;

public class ModelViewTransformation implements IPushFilter<Face>, IPullFilter<Face> {

    private Mat4 modelViewMatrix;
    private IPushFilter<Face> successor;
    private IPullFilter<Face> predecessor;

    @Override
    public void setSuccessor(IPushFilter<?> successor) {
        this.successor = (IPushFilter<Face>) successor;
    }

    @Override
    public void push(Face face) {
        Vec4 v1 = modelViewMatrix.multiply(face.getV1());
        Vec4 v2 = modelViewMatrix.multiply(face.getV2());
        Vec4 v3 = modelViewMatrix.multiply(face.getV3());

        Face transformed = new Face(v1, v2, v3, face.getN1(), face.getN2(), face.getN3());
        successor.push(transformed);
    }

    @Override
    public void setPredecessor(IPullFilter<?> predecessor) {
        this.predecessor = (IPullFilter<Face>) predecessor;
    }

    @Override
    public Face pull() {
        Face face = predecessor.pull();
        if (face == null) return null;

        Vec4 v1 = modelViewMatrix.multiply(face.getV1());
        Vec4 v2 = modelViewMatrix.multiply(face.getV2());
        Vec4 v3 = modelViewMatrix.multiply(face.getV3());

        return new Face(v1, v2, v3, face.getN1(), face.getN2(), face.getN3());
    }

    public void setModelViewMatrix(Mat4 modelViewMatrix) {
        this.modelViewMatrix = modelViewMatrix;
    }

    public Mat4 rotate(float rad, Vec3 modelRotAxis) {
        return Matrices.rotate(rad, modelRotAxis);
    }
}
