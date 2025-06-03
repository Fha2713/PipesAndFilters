package at.fhv.sysarch.lab3.pipeline.filters;

import at.fhv.sysarch.lab3.obj.ColorFace;
import at.fhv.sysarch.lab3.obj.Face;
import com.hackoeur.jglm.Vec3;
import javafx.scene.paint.Color;

public class Lighting implements IPushFilter<ColorFace> {

    private final Vec3 lightPos;
    private IPushFilter<ColorFace> successor;

    public Lighting(Vec3 lightPos) {
        this.lightPos = lightPos;
    }

    @Override
    public void setSuccessor(IPushFilter<?> successor) {
        this.successor = (IPushFilter<ColorFace>) successor;
    }

    @Override
    public void push(ColorFace cf) {
        Face f = cf.getFace();
        Vec3 normal = f.getN1().toVec3().getUnitVector();
        Vec3 surface = f.getV1().toVec3();
        Vec3 lightDir = lightPos.subtract(surface).getUnitVector();

        float intensity = Math.max(0f, normal.dot(lightDir));

        ColorFace lit = new ColorFace(f, cf.getColor(), intensity);
        successor.push(lit);
    }
}
