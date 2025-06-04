package at.fhv.sysarch.lab3.pipeline;

import at.fhv.sysarch.lab3.animation.AnimationRenderer;
import at.fhv.sysarch.lab3.obj.Model;
import at.fhv.sysarch.lab3.pipeline.filters.*;
import com.hackoeur.jglm.Mat4;
import javafx.animation.AnimationTimer;

public class PushPipelineFactory {
    public static AnimationTimer createPipeline(PipelineData pd) {

        // TODO: push from the source (model)
        // TODO 1. perform model-view transformation from model to VIEW SPACE coordinates
        // TODO 2. perform backface culling in VIEW SPACE
        // TODO 3. perform depth sorting in VIEW SPACE
        ModelData modelData = new ModelData();
        ModelTransformation modelTransform = new ModelTransformation();
        ViewingTransformation viewTransform = new ViewingTransformation();
        BackfaceCulling backfaceCulling = new BackfaceCulling();
        DepthSorting depthSorting = new DepthSorting();
        Coloring coloring = new Coloring(pd.getModelColor());
        Lighting lighting = new Lighting(pd.getLightPos());
        ProjectionTransformation projectionTransformation = new ProjectionTransformation(pd.getProjTransform());
        PerspectiveDivision perspectiveDivision = new PerspectiveDivision();
        ViewportTransformation viewportTransformation = new ViewportTransformation(pd.getViewTransform(), pd.getViewHeight());

        Renderer renderer = new Renderer(pd.getGraphicsContext(), pd.getRenderingMode());

        // TODO 4. add coloring (space unimportant)

        modelData.setSuccessor(modelTransform);
        modelTransform.setSuccessor(viewTransform);
        viewTransform.setSuccessor(backfaceCulling);
        backfaceCulling.setSuccessor(depthSorting);
        // lighting can be switched on/off
        if (pd.isPerformLighting()) {
            depthSorting.setSuccessor(coloring);
            coloring.setSuccessor(lighting);
            lighting.setSuccessor(projectionTransformation);
        } else {
            depthSorting.setSuccessor(coloring);
            coloring.setSuccessor(projectionTransformation);
        }
        projectionTransformation.setSuccessor(perspectiveDivision);
        perspectiveDivision.setSuccessor(viewportTransformation);
        viewportTransformation.setSuccessor(renderer);

        // 4a. TODO perform lighting in VIEW SPACE
        // 5. TODO perform projection transformation on VIEW SPACE coordinates
        // 5. TODO perform projection transformation
        // TODO 6. perform perspective division to screen coordinates
        // TODO 7. feed into the sink (renderer)

        // returning an animation renderer which handles clearing of the
        // viewport and computation of the praction
        return new AnimationRenderer(pd) {
            // TODO rotation variable goes in here

            private float rotation = 0.0f;


            /** This method is called for every frame from the JavaFX Animation
             * system (using an AnimationTimer, see AnimationRenderer).
             * @param fraction the time which has passed since the last render call in a fraction of a second
             * @param model    the model to render
             */
            @Override
            protected void render(float fraction, Model model) {
                rotation += (float) (fraction * 2 * Math.PI / 10) % 360;
                Mat4 rotated = modelTransform.rotate(rotation, pd.getModelRotAxis());
                Mat4 translated = modelTransform.translate(rotated, pd.getModelTranslation());
                modelTransform.viewTranslate(translated, pd.getViewTransform());
                modelData.run(model);
                depthSorting.flush();

                // TODO compute rotation in radians
                // TODO create new model rotation matrix using pd.modelRotAxis
                // TODO compute updated model-view tranformation
                // TODO update model-view filter
                // TODO trigger rendering of the pipeline
            }
        };
    }
}