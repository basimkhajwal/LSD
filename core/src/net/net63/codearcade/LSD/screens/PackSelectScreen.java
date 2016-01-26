package net.net63.codearcade.LSD.screens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.Viewport;
import net.net63.codearcade.LSD.LSD;
import net.net63.codearcade.LSD.managers.Assets;
import net.net63.codearcade.LSD.managers.LevelManager;
import net.net63.codearcade.LSD.managers.ShaderManager;
import net.net63.codearcade.LSD.managers.SoundManager;
import net.net63.codearcade.LSD.ui.PagedScrollPane;
import net.net63.codearcade.LSD.utils.BackgroundRenderer;
import net.net63.codearcade.LSD.utils.CentreGUI;
import net.net63.codearcade.LSD.utils.GUIBuilder;
import net.net63.codearcade.LSD.utils.Settings;

import java.util.ArrayList;

/**
 * Created by Basim on 31/12/15.
 */
public class PackSelectScreen extends AbstractScreen {

    private static final Vector3 tmp = new Vector3();

    private BackgroundRenderer backgroundRenderer;
    private CentreGUI centreGUI;

    private Table container;
    private Table starCount;
    private PagedScrollPane pagedScrollPane;
    private ArrayList<ImageButton> buttons = new ArrayList<ImageButton>();
    private ImageButton backButton;

    private int numLevels;
    private int levelPackNum = -1;
    private boolean backClicked = false;

    public PackSelectScreen(LSD game) {
        super(game);

        centreGUI = new CentreGUI();
        backgroundRenderer = new BackgroundRenderer(ShaderManager.Shaders.MENU, BackgroundRenderer.DEFAULT);

        setupUI(centreGUI.getStage());
    }

    private void setupUI(Stage stage) {

        pagedScrollPane = new PagedScrollPane();
        pagedScrollPane.preventFirstAndLast = true;
        numLevels = 0;

        pagedScrollPane.addPage(new Actor());
        for (LevelManager.LevelPack levelPack: LevelManager.LevelPacks) {
            pagedScrollPane.addPage(createPage(levelPack));
            numLevels++;
        }
        pagedScrollPane.addPage(new Actor());

        container = new Table();
        container.setFillParent(true);
        container.add(pagedScrollPane).expand().fill();

        ImageButton prevButton = GUIBuilder.createButton(Assets.Buttons.PREVIOUS_LEVEL);
        prevButton.addListener(new ClickListener() {

            @Override
            public void clicked(InputEvent event, float x, float y) {
                int page = pagedScrollPane.getCurrentPage() - 1;
                if (page >= 1) pagedScrollPane.scrollToPage(page);

                SoundManager.playSound(SoundManager.getClick());
            }

        });

        ImageButton startButton = GUIBuilder.createButton(Assets.Buttons.MENU_PLAY);
        startButton.addListener(new ClickListener() {

            @Override
            public void clicked(InputEvent event, float x, float y) {
                levelPackNum = pagedScrollPane.getCurrentPage() - 1;
                SoundManager.playSound(SoundManager.getClick());
            }

        });

        ImageButton nextButton = GUIBuilder.createButton(Assets.Buttons.NEXT_LEVEL);
        nextButton.addListener(new ClickListener() {

            @Override
            public void clicked(InputEvent event, float x, float y) {
                int page = pagedScrollPane.getCurrentPage() + 1;
                if (page <= numLevels) pagedScrollPane.scrollToPage(page);

                SoundManager.playSound(SoundManager.getClick());
            }

        });

        buttons.add(prevButton);
        buttons.add(startButton);
        buttons.add(nextButton);

        Table buttonTable = new Table();
        buttonTable.setPosition((800 - buttonTable.getWidth()) / 2, 150);
        for (ImageButton button: buttons) buttonTable.add(button).size(100, 100).spaceRight(30);

        //Back button (positioned in the top left corner)
        backButton = GUIBuilder.createButton(Assets.Buttons.BACK);
        backButton.setSize(100, 60);
        backButton.addListener(new ClickListener() {

            @Override
            public void clicked(InputEvent event, float x, float y) {
                backClicked = true;
                SoundManager.playSound(SoundManager.getClick());
                event.handle();
            }


        });

        Label titleLabel = GUIBuilder.createLabel("Pack Select", Assets.FontSizes.FIFTY, Color.YELLOW);
        titleLabel.setPosition((800 - titleLabel.getWidth()) / 2, 600 - titleLabel.getHeight() - 10);

        starCount = new Table();
        starCount.add(new Image(Assets.getAsset(Assets.Images.STAR, Texture.class))).size(50).spaceRight(5);
        starCount.add(GUIBuilder.createLabel(Settings.getStarCount() + "", Assets.FontSizes.FIFTY, Color.WHITE));

        stage.addActor(container);
        stage.addActor(buttonTable);
        stage.addActor(titleLabel);
        stage.addActor(backButton);
        stage.addActor(starCount);
    }

    private Table createPage(LevelManager.LevelPack levelPack) {
        Table table = new Table();

        int numStars = 0;
        for (int level = 0; level < 16; level++) {
            numStars += Settings.getStarsCollected(levelPack.name, level);
        }

        Label title = GUIBuilder.createLabel(levelPack.name, Assets.FontSizes.FIFTY, Color.WHITE);
        Label starText = GUIBuilder.createLabel(numStars + " / 48", Assets.FontSizes.FORTY, Color.WHITE);

        table.add(title).center().colspan(2);
        table.row();
        table.add(new Image(Assets.getAsset(Assets.Images.STAR, Texture.class))).size(35).spaceRight(10);
        table.add(starText);

        return table;
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);

        backgroundRenderer.resize(width, height);
        centreGUI.resize(width, height);

        //Set the container at the bottom left corner
        Viewport viewport = centreGUI.getStage().getViewport();
        tmp.set(0, height - 1, 0);
        viewport.getCamera().unproject(tmp);
        container.setPosition(tmp.x, tmp.y);

        //Set the back button to the top left
        tmp.set(0, 0, 0);
        viewport.getCamera().unproject(tmp);
        backButton.setPosition(tmp.x + 10, tmp.y - 10 - backButton.getHeight());

        //Space pages so only one can be seen at a time
        pagedScrollPane.setPageSpacing(width / 2);
    }

    @Override
    public void render(float deltaTime) {
        super.render(deltaTime);

        for (ImageButton button: buttons) button.setChecked(button.isOver());
        backButton.setChecked(backButton.isOver());

        backgroundRenderer.render(deltaTime);
        centreGUI.render(deltaTime);

        if (levelPackNum != -1) game.setScreen(new LevelSelectScreen(game, levelPackNum));
        if (backClicked) game.setScreen(new MenuScreen(game));
    }

}
