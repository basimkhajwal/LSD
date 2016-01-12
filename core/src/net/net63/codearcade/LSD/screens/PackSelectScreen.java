package net.net63.codearcade.LSD.screens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.Viewport;
import net.net63.codearcade.LSD.LSD;
import net.net63.codearcade.LSD.managers.Assets;
import net.net63.codearcade.LSD.managers.LevelManager;
import net.net63.codearcade.LSD.managers.ShaderManager;
import net.net63.codearcade.LSD.ui.PagedScrollPane;
import net.net63.codearcade.LSD.utils.BackgroundRenderer;
import net.net63.codearcade.LSD.utils.CentreGUI;
import net.net63.codearcade.LSD.utils.GUIBuilder;

import java.util.ArrayList;

/**
 * Created by Basim on 31/12/15.
 */
public class PackSelectScreen extends AbstractScreen {

    private BackgroundRenderer backgroundRenderer;
    private CentreGUI centreGUI;

    private Table container;
    private PagedScrollPane pagedScrollPane;
    private Table first, last;
    private ArrayList<ImageButton> buttons = new ArrayList<ImageButton>();

    private static final Vector3 tmp = new Vector3();

    public PackSelectScreen(LSD game) {
        super(game);

        centreGUI = new CentreGUI();
        backgroundRenderer = new BackgroundRenderer(ShaderManager.Shaders.MENU, BackgroundRenderer.DEFAULT);

        setupUI(centreGUI.getStage());
    }

    private void setupUI(Stage stage) {

        pagedScrollPane = new PagedScrollPane();
        first = null;

        for (LevelManager.LevelPack levelPack: LevelManager.levelPacks) {
            Table page = createPage(levelPack);
            pagedScrollPane.addPage(page);

            if (first == null) first = page;
            last = page;
        }

        container = new Table();
        container.setFillParent(true);
        container.add(pagedScrollPane).expand().fill();

        ImageButton prevButton = GUIBuilder.createButton(Assets.Buttons.PREVIOUS_LEVEL);
        prevButton.addListener(new ClickListener() {

            @Override
            public void clicked(InputEvent event, float x, float y) {
                int page = pagedScrollPane.getCurrentPage() - 1;

                if (page >= 0) pagedScrollPane.scrollToPage(page);
            }

        });

        ImageButton startButton = GUIBuilder.createButton(Assets.Buttons.MENU_PLAY);
        startButton.addListener(new ClickListener() {

            @Override
            public void clicked(InputEvent event, float x, float y) {
                int pack = pagedScrollPane.getCurrentPage();


            }

        });

        ImageButton nextButton = GUIBuilder.createButton(Assets.Buttons.NEXT_LEVEL);
        nextButton.addListener(new ClickListener() {

            @Override
            public void clicked(InputEvent event, float x, float y) {
                int page = pagedScrollPane.getCurrentPage() + 1;

                if (page < LevelManager.levelPacks.length) pagedScrollPane.scrollToPage(page);
            }

        });

        buttons.add(prevButton);
        buttons.add(startButton);
        buttons.add(nextButton);

        Table buttonTable = new Table();
        buttonTable.setPosition((800 - buttonTable.getWidth()) / 2, 150);
        for (ImageButton button: buttons) buttonTable.add(button).size(100, 100).spaceRight(30);

        stage.addActor(container);
        stage.addActor(buttonTable);
    }

    private Table createPage(LevelManager.LevelPack levelPack) {
        Table table = new Table();

        Label title = GUIBuilder.createLabel(levelPack.name, Assets.FontSizes.FIFTY, Color.WHITE);

        table.add(title).center();

        return table;
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);

        backgroundRenderer.resize(width, height);
        centreGUI.resize(width, height);

        //Space pages so only one can be seen at a time
        first.padLeft((width - first.getMinWidth()) / 2);
        last.padRight((width - last.getMinWidth()) / 2);
        pagedScrollPane.setPageSpacing(width / 2);

        //Set the container at the bottom left corner
        Viewport viewport = centreGUI.getStage().getViewport();
        tmp.set(0, height - 1, 0);
        viewport.getCamera().unproject(tmp);
        container.setPosition(tmp.x, tmp.y);
    }

    @Override
    public void render(float deltaTime) {
        super.render(deltaTime);

        for (ImageButton button: buttons) button.setChecked(button.isOver());

        backgroundRenderer.render(deltaTime);
        centreGUI.render(deltaTime);
    }

}
