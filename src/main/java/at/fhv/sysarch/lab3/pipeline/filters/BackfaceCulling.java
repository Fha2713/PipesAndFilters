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
    public void push(Face f) {
        // Normale des Dreiecks in View-Space (vorausgesetzt vom Vorgänger-Filter)
        Vec3 normal = f.getN1().toVec3();  // alle 3 Normalen sind bei flacher Shading identisch

        // Blickrichtung ist (0, 0, -1) in View Space
        Vec3 viewDir = new Vec3(0, 0, -1);

        // Prüfe Dot-Produkt
        float dot = normal.dot(viewDir);

        if (dot < 0) {
            // Fläche zeigt zur Kamera → weiterverarbeiten
            successor.push(f);
        }
        // sonst: wird ignoriert (gecullt)
    }
}
