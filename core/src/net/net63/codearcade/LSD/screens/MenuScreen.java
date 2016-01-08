package net.net63.codearcade.LSD.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.Viewport;
import net.net63.codearcade.LSD.LSD;
import net.net63.codearcade.LSD.managers.Assets;
import net.net63.codearcade.LSD.managers.ShaderManager;
import net.net63.codearcade.LSD.screens.overlays.SettingsScreen;
import net.net63.codearcade.LSD.utils.BackgroundRenderer;
import net.net63.codearcade.LSD.utils.CentreGUI;
import net.net63.codearcade.LSD.utils.GUIBuilder;


/**
 * The Menu Screen state where the user selects the options they want to
 * do
 * 
 * @author Basim
 *
 */
public class MenuScreen extends AbstractScreen{

    //Temporary vectors
    private static final Vector3 tmp = new Vector3();

    // Constants
    private static final Color TOP_TITLE = new Color(220 / 255.0f, 80 / 255.0f, 0f, 1f);
    private static final Color BOTTOM_TITLE = new Color(150 / 255.0f, 30 / 255.0f, 0f, 1f);

    //GUI
    private CentreGUI centreGUI;
    private ImageButton settingsButton;
    private ImageButton playButton;

    //Background rendering
    private BackgroundRenderer backgroundRenderer;

    private boolean settingsClicked = false;
    private boolean playClicked = false;

    public MenuScreen(LSD game) {
        super(game);

        //Create the background renderer
        backgroundRenderer = new BackgroundRenderer(ShaderManager.Shaders.MENU, BACKGROUND_METHOD);
        centreGUI = new CentreGUI();

        setupUI();
    }

    @Override
    public void resumeLogic() {
        Gdx.input.setInputProcessor(centreGUI.getStage());
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
            public void clicked(InputEvent event, float x, float y) {
                //Apply the change
                 playClicked = true;
            }
        });

        //The settings button to view the settings (positioned in the top right corner)
        settingsButton = GUIBuilder.createButton(Assets.Buttons.SETTINGS);
        settingsButton.setSize(50, 50);
        settingsButton.addListener(new ClickListener() {

            @Override
            public void clicked(InputEvent event, float x, float y) {
                settingsClicked = true;
            }

        });


        //Add all the GUI elements
        Stage stage = centreGUI.getStage();
        stage.addActor(topTitle);
        stage.addActor(bottomTitle);
        stage.addActor(playButton);
        stage.addActor(settingsButton);
    }
	
	@Override
	public void resize(int width, int height) {
		super.resize(width, height);

        //Resize the background
        backgroundRenderer.resize(width, height);

        //Update the GUI
        centreGUI.resize(width, height);

        //Set the position of the settings button to the top-right corner
        Viewport viewport = centreGUI.getStage().getViewport();
        tmp.set(width, 0, 0);
        viewport.getCamera().unproject(tmp);
        settingsButton.setPosition(tmp.x - 20 - settingsButton.getWidth(), tmp.y - 20 - settingsButton.getHeight());
	}
	
	@Override
	public void render(float delta) {
		super.render(delta);

        //Render the background
        backgroundRenderer.render(delta);

        //Update and render the UI
        playButton.setChecked(playButton.isOver());
        settingsButton.setChecked(settingsButton.isOver());
        centreGUI.render(delta);

        //If the button has been clicked start the game
		if (playClicked) {
            dispose();
            game.setScreen(new PackSelectScreen(game));
        }

        //If the settings button was clicked go to the settings screen
        if (settingsClicked) {
            settingsClicked = false;
            game.setScreen(new SettingsScreen(game, this));
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
		centreGUI.dispose();
	}
	
}
