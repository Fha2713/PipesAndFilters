package at.fhv.sysarch.lab3.obj;

import com.hackoeur.jglm.Vec4;

public class FaceFinished extends Face {

    public FaceFinished(Vec4 v1, Vec4 v2, Vec4 v3, Vec4 n1, Vec4 n2, Vec4 n3) {
        super(v1, v2, v3, n1, n2, n3);
    }

    public FaceFinished(Vec4 v1, Vec4 v2, Vec4 v3, Face otherNormals) {
        super(v1, v2, v3, otherNormals);
    }
}
