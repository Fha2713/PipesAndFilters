package at.fhv.sysarch.lab3.pipeline.filters;


import at.fhv.sysarch.lab3.obj.Face;
import javafx.scene.paint.Color;
import com.hackoeur.jglm.Vec3;
import at.fhv.sysarch.lab3.pipeline.data.Pair;

public class FlatShading implements IPushFilter<Face> {

    private IPushFilter<Pair<Face, Color>> successor;
    private final Vec3 lightPos;
    private final Color modelColor;

    public FlatShading(Vec3 lightPos, Color modelColor) {
        this.lightPos = lightPos;
        this.modelColor = modelColor;
    }

    @Override
    public void setSuccessor(IPushFilter<?> successor) {
        this.successor = (IPushFilter<Pair<Face, Color>>) successor;
    }

    @Override
    public void push(Face face) {
        Color shadedColor = applyFlatShading(face, lightPos, modelColor);
        Pair<Face, Color> coloredFace = new Pair<>(face, shadedColor);

        if (successor != null) {
            successor.push(coloredFace);
        }
    }

    private Color applyFlatShading(Face face, Vec3 lightPos, Color baseColor) {
        Vec3 faceNormal = calculateFaceNormal(face);
        Vec3 lightDirection = lightPos.subtract(face.getV1().toVec3()).getUnitVector();
        double dotProduct = faceNormal.dot(lightDirection);
        double intensity = Math.max(0, dotProduct);
        return baseColor.deriveColor(0, 1, intensity, 1);
    }

    private Vec3 calculateFaceNormal(Face face) {
        Vec3 v1 = face.getV1().toVec3();
        Vec3 v2 = face.getV2().toVec3();
        Vec3 v3 = face.getV3().toVec3();
        return v2.subtract(v1).cross(v3.subtract(v1)).getUnitVector();
    }
}
