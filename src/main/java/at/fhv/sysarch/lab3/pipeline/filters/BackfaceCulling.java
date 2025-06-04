package at.fhv.sysarch.lab3.pipeline.filters;

import at.fhv.sysarch.lab3.obj.Face;
import com.hackoeur.jglm.Vec3;

public class BackfaceCulling implements IPushFilter<Face> {

    private IPushFilter<Face> successor;

    @Override
    public void setSuccessor(IPushFilter<?> successor) {
        this.successor = (IPushFilter<Face>) successor;
    }

    @Override
    public void push(Face face) {
        if (isFaceVisible(face)) {
            if (successor != null) {
                successor.push(face);
            }
        }
        // sonst: cull – nichts weitergeben
    }

    private boolean isFaceVisible(Face face) {
        Vec3 v1 = face.getV1().toVec3();
        Vec3 v2 = face.getV2().toVec3();
        Vec3 v3 = face.getV3().toVec3();

        // Dreiecksnormalen-Vektor berechnen
        Vec3 normal = v2.subtract(v1).cross(v3.subtract(v1)).getUnitVector();

        // Blickrichtung in View-Space ist entlang -Z
        Vec3 viewDir = new Vec3(0, 0, -1);

        // Sichtbar, wenn der Winkel spitz ist → dot < 0
        return normal.dot(viewDir) < 0;
    }
}
