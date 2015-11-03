package net.net63.codearcade.LSD.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import net.net63.codearcade.LSD.LSD;
import net.net63.codearcade.LSD.utils.Assets;
import net.net63.codearcade.LSD.utils.Constants;
import net.net63.codearcade.LSD.utils.GUIBuilder;
import net.net63.codearcade.LSD.utils.ShaderManager;


/**
 * The Menu Screen state where the user selects the options they want to
 * do
 * 
 * @author Basim
 *
 */
public class MenuScreen extends AbstractScreen{

    // Constants
    private static final Color TOP_TITLE = new Color(220 / 255.0f, 80 / 255.0f, 0f, 1f);
    private static final Color BOTTOM_TITLE = new Color(150 / 255.0f, 30 / 255.0f, 0f, 1f);

    //GUI
	private Stage stage;
    private ImageButton playButton;

    //Shader rendering
    private SpriteBatch batch;
    private OrthographicCamera camera;

    //Background and shader
    private Texture backgroundTexture;
    private Vector2 backgroundSize = new Vector2();
    private ShaderProgram shaderProgram;

    //Background position
    private float backgroundX = 0;
    private float time = 0;

    private boolean changingScreen = false;

    public MenuScreen(LSD game) {
        super(game);

        //Instantiate the batch and camera for drawing the background
        batch = new SpriteBatch();
        camera = new OrthographicCamera();

        //Load the shader and set pedantic to stop un-used uniform errors
        shaderProgram = ShaderManager.getShader(ShaderManager.Shaders.MENU);
        shaderProgram.pedantic = false;

        //Create the stage for the GUI and the input handler
        stage = new Stage(new ExtendViewport(Constants.DEFAULT_SCREEN_WIDTH, Constants.DEFAULT_SCREEN_HEIGHT));
        Gdx.input.setInputProcessor(stage);


        setupUI();
    }

    private void setupUI() {
        //Top title text
        Label topTitle = GUIBuilder.createLabel("Little Sticky", 100, TOP_TITLE);
        topTitle.setAlignment(Align.center);
        topTitle.setPosition((800 - topTitle.getWidth()) / 2, 450);

        //Bottom title text
        Label bottomTitle = GUIBuilder.createLabel("Destroyer", 100, BOTTOM_TITLE);
        bottomTitle.setAlignment(Align.center);
        bottomTitle.setPosition((800 - bottomTitle.getWidth()) / 2, topTitle.getY() - bottomTitle.getHeight());

        //The play button to handle play-game
        playButton = GUIBuilder.createButton(Assets.Buttons.MENU_PLAY);
        playButton.setSize(140f, 140f);
        playButton.setPosition((800 - playButton.getWidth()) / 2.0f, 100);
        playButton.addListener(new ClickListener() {

            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);

                //If the mouse is released on the button apply the change
                if (this.inTapSquare()) changingScreen = true;
            }
        });

        //Get the background and set the shader uniform
        backgroundTexture = Assets.getAsset(Assets.Images.BACKGROUND, Texture.class);
        backgroundTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        shaderProgram.setUniformf("invScreenSize", 1.0f/backgroundTexture.getWidth(), 1.0f/backgroundTexture.getHeight());

        //Add all the GUI elements
        stage.addActor(topTitle);
        stage.addActor(bottomTitle);
        stage.addActor(playButton);
    }
	
	@Override
	public void resize(int width, int height) {
		super.resize(width, height);

        //Update the viewport size
        Viewport viewport = stage.getViewport();
		viewport.update(width, height);

        //Reposition the viewport to the centre
        Camera stageCam = stage.getViewport().getCamera();
        stageCam.position.x = Constants.DEFAULT_SCREEN_WIDTH / 2;
        stageCam.position.y = Constants.DEFAULT_SCREEN_HEIGHT / 2;
        stageCam.update();

        //Update the shader camera and resend the uniform for the screenSize
        camera.setToOrtho(false, width, height);
        shaderProgram.begin();
        shaderProgram.setUniformf("screenSize", width, height);
        shaderProgram.end();

        //Reset the size and position of the background
        backgroundX = 0;
        backgroundSize.set(width, height);
	}
	
	@Override
	public void render(float delta) {
		super.render(delta);

        //Update the time
        time += delta;

        //Move the background, reset if it passes the screen
        backgroundX += 25 * delta;
        if (backgroundX >= backgroundSize.x) {
            backgroundX = 0;
        }

        //Set the shader and pass the time uniform
        batch.setShader(shaderProgram);
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        shaderProgram.setUniformf("time", time);

        //Draw the background twice, side by side
        batch.draw(backgroundTexture, backgroundX, 0, backgroundSize.x, backgroundSize.y);
        batch.draw(backgroundTexture, backgroundX - backgroundSize.x, 0, backgroundSize.x, backgroundSize.y);
        batch.end();

        //Update and render the UI
		stage.act(delta);
        playButton.setChecked(playButton.isOver());
		stage.draw();

        //If the button has been clicked start the game
		if (changingScreen) {
            dispose();
            game.setScreen(new GameScreen(game, 0));
        }
	}
	
	@Override
	public void dispose() {
		super.dispose();

        batch.dispose();
		stage.dispose();
        shaderProgram.dispose();
	}
	
}
