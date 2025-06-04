package at.fhv.sysarch.lab3.pipeline.filters;

import at.fhv.sysarch.lab3.obj.Face;
import com.hackoeur.jglm.*;

public class ViewingTransformation implements IPushFilter<Face> {

    private final Mat4 viewMatrix;
    private IPushFilter<Face> successor;


    public ViewingTransformation() {
        this.viewMatrix = Matrices.lookAt(
                new Vec3(0, 0, 600),   // Kamera weiter zurück
                new Vec3(0, 0, 0),     // Blickrichtung auf Ursprung (Teekanne)
                new Vec3(0, 1, 0)      // Up-Vektor: Y-Achse
        );    }

    @Override
    public void setSuccessor(IPushFilter<?> successor) {
        this.successor = (IPushFilter<Face>) successor;
    }

    @Override
    public void push(Face f) {
        Vec4 v1 = viewMatrix.multiply(f.getV1());
        Vec4 v2 = viewMatrix.multiply(f.getV2());
        Vec4 v3 = viewMatrix.multiply(f.getV3());

        Vec4 n1 = viewMatrix.multiply(f.getN1());
        Vec4 n2 = viewMatrix.multiply(f.getN2());
        Vec4 n3 = viewMatrix.multiply(f.getN3());

        Face transformed = new Face(v1, v2, v3, n1, n2, n3);

        successor.push(transformed);
    }
}
