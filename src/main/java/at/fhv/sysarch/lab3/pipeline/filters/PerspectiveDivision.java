package at.fhv.sysarch.lab3.pipeline.filters;

import at.fhv.sysarch.lab3.obj.ColorFace;
import at.fhv.sysarch.lab3.obj.Face;
import com.hackoeur.jglm.Vec4;

public class PerspectiveDivision implements IPushFilter<ColorFace>{

    private IPushFilter<ColorFace> successor;

    @Override
    public void setSuccessor(IPushFilter<?> successor) {
        this.successor = (IPushFilter<ColorFace>) successor;
    }

    @Override
    public void push(ColorFace cf) {
        Face f = cf.getFace();

        Vec4 v1 = divideByW(f.getV1());
        Vec4 v2 = divideByW(f.getV2());
        Vec4 v3 = divideByW(f.getV3());

        Face divided = new Face(v1, v2, v3, f.getN1(), f.getN2(), f.getN3());

        ColorFace result = new ColorFace(divided, cf.getColor(), cf.getIntensity());
        successor.push(result);
    }

    private Vec4 divideByW(Vec4 v) {
        float w = v.getW();
        return new Vec4(
                v.getX() / w,
                v.getY() / w,
                v.getZ() / w,
                1f
        );
    }
}
