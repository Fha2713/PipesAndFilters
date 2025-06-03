package at.fhv.sysarch.lab3.obj;

import javafx.scene.paint.Color;

public class ColorFace {

    private final Face face;
    private final Color color;
    private final float intensity;


    public ColorFace(Face face, Color color, float intensity) {
        this.face = face;
        this.color = color;
        this.intensity = intensity;
    }

    public Face getFace() {
        return face;
    }

    public Color getColor() {
        return color;
    }

    public float getIntensity() {
        return intensity;
    }

    public Color getShadedColor() {
        return color.deriveColor(0, 1, intensity, 1);
    }


}
