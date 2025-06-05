package at.fhv.sysarch.lab3.pipeline.filters;

import at.fhv.sysarch.lab3.obj.Face;
import at.fhv.sysarch.lab3.pipeline.data.Pair;
import com.hackoeur.jglm.Vec3;
import javafx.scene.paint.Color;

public class Lighting implements IPushFilter<Pair<Face, Color>>, IPullFilter<Pair<Face, Color>> {

    private final Vec3 lightPos;
    private IPushFilter<Pair<Face, Color>> successor;
    private IPullFilter<Pair<Face, Color>> predecessor;

    public Lighting(Vec3 lightPos) {
        this.lightPos = lightPos;
    }

    @Override
    public void setSuccessor(IPushFilter<?> successor) {
        this.successor = (IPushFilter<Pair<Face, Color>>) successor;
    }

    @Override
    public void push(Pair<Face, Color> input) {
        Pair<Face, Color> output = applyLighting(input);
        if (successor != null) successor.push(output);
    }

    @Override
    public void setPredecessor(IPullFilter<?> predecessor) {
        this.predecessor = (IPullFilter<Pair<Face, Color>>) predecessor;
    }

    @Override
    public Pair<Face, Color> pull() {
        Pair<Face, Color> input = predecessor.pull();
        if (input == null) return null;
        return applyLighting(input);
    }

    private Pair<Face, Color> applyLighting(Pair<Face, Color> input) {
        Face face = input.fst();
        Color baseColor = input.snd();

        Vec3 normal = calculateFaceNormal(face);
        Vec3 lightDir = lightPos.subtract(face.getV1().toVec3()).getUnitVector();
        double intensity = Math.max(0, normal.dot(lightDir));

        Color shadedColor = baseColor.deriveColor(0, 1, intensity, 1);
        return new Pair<>(face, shadedColor);
    }

    private Vec3 calculateFaceNormal(Face face) {
        Vec3 v1 = face.getV1().toVec3();
        Vec3 v2 = face.getV2().toVec3();
        Vec3 v3 = face.getV3().toVec3();
        return v2.subtract(v1).cross(v3.subtract(v1)).getUnitVector();
    }
}
