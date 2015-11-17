package net.net63.codearcade.LSD.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * A utility class to avoid re-using code over and over again
 * which sets the same viewport and resizing methods
 *
 * Created by Basim on 17/11/15.
 */
public class CentreGUI implements Disposable{

    //The main stage
    private Stage stage;

    /**
     * Create a new gui manager
     */
    public CentreGUI() {
        stage = new Stage(new ExtendViewport(Constants.DEFAULT_SCREEN_WIDTH, Constants.DEFAULT_SCREEN_HEIGHT));
        Gdx.input.setInputProcessor(stage);
    }

    /**
     * Resize the GUI
     *
     * @param width The screen width
     * @param height The screen height
     */
    public void resize(int width, int height) {
        //Update the viewport size
        Viewport viewport = stage.getViewport();
        viewport.update(width, height);

        //Reposition the viewport to the centre
        Camera stageCam = stage.getViewport().getCamera();
        stageCam.position.x = Constants.DEFAULT_SCREEN_WIDTH / 2;
        stageCam.position.y = Constants.DEFAULT_SCREEN_HEIGHT / 2;
        stageCam.update();
    }

    /**
     * Update and render the GUI
     *
     * @param deltaTime The amount to update by (seconds)
     */
    public void render(float deltaTime) {
        stage.getViewport().apply();
        stage.act(deltaTime);
        stage.draw();
    }

    /**
     * Get the stage
     *
     * @return The stage
     */
    public Stage getStage() {
        return stage;
    }

    @Override
    public void dispose() {
        stage.dispose();
    }


}
