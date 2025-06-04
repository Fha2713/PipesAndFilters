package at.fhv.sysarch.lab3.pipeline.filters;

import at.fhv.sysarch.lab3.obj.Face;
import at.fhv.sysarch.lab3.obj.FaceFinished;
import com.hackoeur.jglm.Vec3;

import java.util.Deque;
import java.util.LinkedList;

public class BackfaceCulling implements IPullFilter<Face>, IPushFilter<Face> {

    private IPullFilter<Face> predecessor;
    private IPushFilter<Face> successor;

    // interne Queue für Iterator-ähnliche Nutzung
    private final Deque<Face> buffer = new LinkedList<>();

    @Override
    public void setPredecessor(IPullFilter<?> predecessor) {
        this.predecessor = (IPullFilter<Face>) predecessor;
    }

    @Override
    public void setSuccessor(IPushFilter<?> successor) {
        this.successor = (IPushFilter<Face>) successor;
    }

    // Iterator-ähnliche Methode: gibt true, wenn noch sichtbare Faces im buffer oder im Nachfolger verfügbar sind
    public boolean hasNextFace() {
        // Wenn Puffer leer, versuche neue sichtbare Faces zu laden
        while (buffer.isEmpty()) {
            Face face = predecessor.pull();
            if (face == null) return false; // kein Face mehr
            if (face instanceof FaceFinished) {
                buffer.add(face);
                return true;
            }
            if (face.getV1() == null || face.getV2() == null || face.getV3() == null) {
                continue; // ungültiges Face ignorieren
            }
            if (isFaceVisible(face)) {
                buffer.add(face);
                return true;
            }
            // unsichtbares Face ignorieren
        }
        return true; // buffer ist nicht leer
    }

    // Iterator-ähnliche Methode: nächstes sichtbares Face holen (muss vorher hasNextFace true liefern)
    public Face nextFace() {
        return buffer.poll();
    }

    // IPullFilter Methode, die iteratorähnlich pull macht
    @Override
    public Face pull() {
        if (!hasNextFace()) return null;
        return nextFace();
    }

    // Push-Implementierung wie gehabt
    @Override
    public void push(Face face) {
        if (face == null) return;

        if (face instanceof FaceFinished) {
            successor.push(face);
            return;
        }
        if (face.getV1() == null || face.getV2() == null || face.getV3() == null) {
            return;
        }
        if (isFaceVisible(face)) {
            successor.push(face);
        }
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
