package net.net63.codearcade.LSD.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
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
import net.net63.codearcade.LSD.utils.*;


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

    //Background rendering
    private BackgroundRenderer backgroundRenderer;

    private boolean changingScreen = false;

    public MenuScreen(LSD game) {
        super(game);

        //Create the background renderer
        backgroundRenderer = new BackgroundRenderer(ShaderManager.Shaders.MENU, BACKGROUND_METHOD);

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

        //Add all the GUI elements
        stage.addActor(topTitle);
        stage.addActor(bottomTitle);
        stage.addActor(playButton);
    }
	
	@Override
	public void resize(int width, int height) {
		super.resize(width, height);

        //Resize the background
        backgroundRenderer.resize(width, height);

        //Update the viewport size
        Viewport viewport = stage.getViewport();
		viewport.update(width, height);

        //Reposition the viewport to the centre
        Camera stageCam = stage.getViewport().getCamera();
        stageCam.position.x = Constants.DEFAULT_SCREEN_WIDTH / 2;
        stageCam.position.y = Constants.DEFAULT_SCREEN_HEIGHT / 2;
        stageCam.update();
	}
	
	@Override
	public void render(float delta) {
		super.render(delta);

        //Render the background
        backgroundRenderer.render(delta);

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

    // Render method to render the background
    private static final BackgroundRenderer.BackgroundRenderable BACKGROUND_METHOD = new BackgroundRenderer.BackgroundRenderable() {

        private float xPos;
        private Texture texture;
        private Vector2 size;

        @Override
        public void setup(BackgroundRenderer renderer) {
            texture = renderer.getTexture();
            size = renderer.getScreenSize();
            xPos = 0;
        }

        @Override
        public void renderBackground(SpriteBatch batch, float delta) {
            //Move the background, reset if it passes the screen
            xPos += 25 * delta;
            if (xPos >= size.x) xPos = 0;

            //Draw the background twice, side by side
            batch.draw(texture, xPos, 0, size.x, size.y);
            batch.draw(texture, xPos - size.x, 0, size.x, size.y);
        }
    };

	@Override
	public void dispose() {
		super.dispose();

        backgroundRenderer.dispose();
		stage.dispose();
	}
	
}
