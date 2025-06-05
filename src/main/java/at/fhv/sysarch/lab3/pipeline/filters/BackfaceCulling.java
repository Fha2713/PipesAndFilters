package at.fhv.sysarch.lab3.pipeline.filters;

import at.fhv.sysarch.lab3.obj.Face;
import com.hackoeur.jglm.Vec3;


public class BackfaceCulling implements IPullFilter<Face>, IPushFilter<Face> {

    private IPullFilter<Face> predecessor;
    private IPushFilter<Face> successor;

    @Override
    public void setSuccessor(IPushFilter<?> successor) {
        this.successor = (IPushFilter<Face>) successor;
    }

    @Override
    public void push(Face face) {
        if (isFaceVisible(face) && successor != null) {
            successor.push(face);
        }
    }

    @Override
    public void setPredecessor(IPullFilter<?> predecessor) {
        this.predecessor = (IPullFilter<Face>) predecessor;
    }

    @Override
    public Face pull() {
        if (predecessor == null) return null;

        Face face;
        do {
            face = predecessor.pull();
            if (face == null) return null;
        } while (!isFaceVisible(face));

        return face;
    }

    private boolean isFaceVisible(Face face) {
        Vec3 v1 = face.getV1().toVec3();
        Vec3 v2 = face.getV2().toVec3();
        Vec3 v3 = face.getV3().toVec3();

        Vec3 normal = v2.subtract(v1).cross(v3.subtract(v1));
        Vec3 viewVector = new Vec3(0, 0, -1);

        return normal.dot(viewVector) < 0;
    }
}
