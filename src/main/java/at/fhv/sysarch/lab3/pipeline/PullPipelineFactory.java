package at.fhv.sysarch.lab3.pipeline;

import at.fhv.sysarch.lab3.animation.AnimationRenderer;
import at.fhv.sysarch.lab3.obj.Model;
import at.fhv.sysarch.lab3.pipeline.filters.*;
import com.hackoeur.jglm.Mat4;
import com.hackoeur.jglm.Vec4;
import javafx.animation.AnimationTimer;

public class PullPipelineFactory {
    public static AnimationTimer createPipeline(PipelineData pd) {
        ModelData modelData = new ModelData();
        ModelViewTransformation modelViewTransform = new ModelViewTransformation();
        BackfaceCulling backfaceCulling = new BackfaceCulling();
        DepthSorting depthSorting = new DepthSorting();
        FlatShading flatShading = new FlatShading(pd.getLightPos(), pd.getModelColor());
        ProjectionTransformation projectionTransformation = new ProjectionTransformation(pd.getProjTransform());
        PerspectiveDivision perspectiveDivision = new PerspectiveDivision();
        ViewportTransformation viewportTransformation = new ViewportTransformation(pd.getViewportTransform(), pd.getViewHeight());

        Renderer renderer = new Renderer(pd.getGraphicsContext(), pd.getRenderingMode());

        renderer.setPredecessor(viewportTransformation);
        viewportTransformation.setPredecessor(perspectiveDivision);
        perspectiveDivision.setPredecessor(projectionTransformation);
        projectionTransformation.setPredecessor(flatShading);
        flatShading.setPredecessor(depthSorting);
        depthSorting.setPredecessor(backfaceCulling);
        backfaceCulling.setPredecessor(modelViewTransform);
        modelViewTransform.setPredecessor(modelData);

        // TODO: pull from the source (model)
        // TODO 1. perform model-view transformation from model to VIEW SPACE coordinates
        // TODO 2. perform backface culling in VIEW SPACE
        // TODO 3. perform depth sorting in VIEW SPACE
        // TODO 4. add coloring (space unimportant)
        // lighting can be switched on/off
        if (pd.isPerformLighting()) {
            // 4a. TODO perform lighting in VIEW SPACE
            // 5. TODO perform projection transformation on VIEW SPACE coordinates
        } else {
            // 5. TODO perform projection transformation
        }
        // TODO 6. perform perspective division to screen coordinates
        // TODO 7. feed into the sink (renderer)

        // returning an animation renderer which handles clearing of the
        // viewport and computation of the praction
        return new AnimationRenderer(pd) {

            private float rotation = 0.0f;

            // TODO rotation variable goes in here
            /** This method is called for every frame from the JavaFX Animation
             * system (using an AnimationTimer, see AnimationRenderer). 
             * @param fraction the time which has passed since the last render call in a fraction of a second
             * @param model    the model to render 
             */
            @Override
            protected void render(float fraction, Model model) {
                rotation += (float) (fraction * 2 * Math.PI / 10);

                Mat4 scaleMatrix = new Mat4(
                        new Vec4(1f, 0f, 0f, 0f),
                        new Vec4(0f, 1f, 0f, 0f),
                        new Vec4(0f, 0f, 1f, 0f),
                        new Vec4(0f, 0f, 0f, 1f)
                );

                Mat4 rotated = modelViewTransform.rotate(rotation, pd.getModelRotAxis());
                Mat4 modelMatrix = pd.getModelTranslation().multiply(rotated).multiply(scaleMatrix);
                Mat4 modelViewMatrix = pd.getViewTransform().multiply(modelMatrix);

                modelViewTransform.setModelViewMatrix(modelViewMatrix);
                modelData.setModel(model);

                // Pipeline starten (Pull-Variante)
                renderer.renderAll();

                // TODO compute rotation in radians
                // TODO create new model rotation matrix using pd.getModelRotAxis and Matrices.rotate
                // TODO compute updated model-view tranformation
                // TODO update model-view filter
                // TODO trigger rendering of the pipeline
            }
        };
    }
}
