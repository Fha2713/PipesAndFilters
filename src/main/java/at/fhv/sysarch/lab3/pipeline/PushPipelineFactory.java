package at.fhv.sysarch.lab3.pipeline;

import at.fhv.sysarch.lab3.animation.AnimationRenderer;
import at.fhv.sysarch.lab3.obj.Model;
import at.fhv.sysarch.lab3.pipeline.filters.*;
import com.hackoeur.jglm.Mat4;
import javafx.animation.AnimationTimer;

public class PushPipelineFactory {
    public static AnimationTimer createPipeline(PipelineData pd) {
        ModelData modelData = new ModelData();
        ModelViewTransformation modelViewTransform = new ModelViewTransformation();
        BackfaceCulling backfaceCulling = new BackfaceCulling();
        DepthSorting depthSorting = new DepthSorting();
        Coloring coloring = new Coloring(pd.getModelColor());
        Lighting lighting = new Lighting(pd.getLightPos());
        ProjectionTransformation projectionTransformation = new ProjectionTransformation(pd.getProjTransform());
        PerspectiveDivision perspectiveDivision = new PerspectiveDivision();
        ViewportTransformation viewportTransformation = new ViewportTransformation(pd.getViewportTransform());
        Renderer renderer = new Renderer(pd.getGraphicsContext(), pd.getRenderingMode());

        modelData.setSuccessor(modelViewTransform);
        modelViewTransform.setSuccessor(backfaceCulling);
        backfaceCulling.setSuccessor(depthSorting);
        depthSorting.setSuccessor(coloring);

        if (pd.isPerformLighting()) {
            coloring.setSuccessor(lighting);
            lighting.setSuccessor(projectionTransformation);
        } else {
            coloring.setSuccessor(projectionTransformation);
        }

        projectionTransformation.setSuccessor(perspectiveDivision);
        perspectiveDivision.setSuccessor(viewportTransformation);
        viewportTransformation.setSuccessor(renderer);

        return new AnimationRenderer(pd) {

            private float rotation = 0.0f;

            /** This method is called for every frame from the JavaFX Animation
             * system (using an AnimationTimer, see AnimationRenderer).
             * @param fraction the time which has passed since the last render call in a fraction of a second
             * @param model    the model to render
             */
            @Override
            protected void render(float fraction, Model model) {
                rotation += (float) (fraction * 2 * Math.PI / 10);

                Mat4 rotated = modelViewTransform.rotate(rotation, pd.getModelRotAxis());
                Mat4 modelMatrix = pd.getModelTranslation().multiply(rotated);
                Mat4 modelViewMatrix = pd.getViewTransform().multiply(modelMatrix);

                modelViewTransform.setModelViewMatrix(modelViewMatrix);

                modelData.run(model);
                depthSorting.flush();
            }
        };
    }
}
