package net.net63.codearcade.LSD.screens.overlays;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import net.net63.codearcade.LSD.LSD;
import net.net63.codearcade.LSD.screens.AbstractScreen;
import net.net63.codearcade.LSD.utils.Constants;

/**
 * Abstract overlay screen to handle the most common
 * methods and variables in the overlay-screens to be
 * displayed on top of another screen
 *
 * Created by Basim on 10/10/15.
 */
public abstract class AbstractOverlay extends AbstractScreen {

    //Whether to dispose the previous screen on hide
    protected boolean disposeScreen = true;

    //Pointers to the game and the last re-run
    protected AbstractScreen previousScreen;
    protected LSD game;

    //GUI elements
    private Stage stage;
    private Image overlay;

    public AbstractOverlay(LSD game, AbstractScreen previousScreen) {
        super(game);

        this.game = game;
        this.previousScreen = previousScreen;
        previousScreen.pauseLogic();

        //Setup all the elements
        setup();
    }

    private void setup() {
        //Create the stage and set to input
        stage = new Stage(new ExtendViewport(Constants.DEFAULT_SCREEN_WIDTH, Constants.DEFAULT_SCREEN_HEIGHT));
        Gdx.input.setInputProcessor(stage);

        //Create the translucent gray pixmap
        Pixmap overlayPix = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        overlayPix.setColor(0, 0, 0, 0.5f);
        overlayPix.fill();

        //Create the texture from the pixmap
        overlay = new Image(new Texture(overlayPix));
        overlay.setScaling(Scaling.stretch);
        stage.addActor(overlay);
        overlayPix.dispose();

        //Call the subclass to add all the other GUI elements
        setupUI(stage);
    }

    public abstract void setupUI(Stage stage);
    public abstract void checkChange();

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);

        //Update the viewport
        Viewport viewport = stage.getViewport();
        viewport.update(width, height);

        //Centre the camera
        Camera stageCam = stage.getViewport().getCamera();
        stageCam.position.x = Constants.DEFAULT_SCREEN_WIDTH / 2;
        stageCam.position.y = Constants.DEFAULT_SCREEN_HEIGHT / 2;
        stageCam.update();

        //Set the overlay to the bottom left
        Vector2 zero = new Vector2(0, height - 1);
        viewport.unproject(zero);

        overlay.setPosition(zero.x, zero.y);
        overlay.setSize(viewport.getWorldWidth(), viewport.getWorldHeight());

        //Resize the previous game
        previousScreen.resize(width, height);
    }

    @Override
    public void render(float deltaTime) {
        super.render(deltaTime);

        //Render the previous game
        previousScreen.render(deltaTime);

        //Render the GUI
        stage.getViewport().apply();
        stage.act(deltaTime);
        stage.draw();

        //Call the abstract method for any subclass changes
        checkChange();
    }

    @Override
    public void hide() {
        super.hide();
        dispose();
    }

    @Override
    public void dispose() {
        super.dispose();

        if (disposeScreen) previousScreen.dispose();
        ((TextureRegionDrawable) overlay.getDrawable()).getRegion().getTexture().dispose();
        stage.dispose();
    }


}
