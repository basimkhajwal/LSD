package net.net63.codearcade.LSD.screens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import net.net63.codearcade.LSD.LSD;
import net.net63.codearcade.LSD.managers.Assets;
import net.net63.codearcade.LSD.managers.LevelManager;
import net.net63.codearcade.LSD.managers.ShaderManager;
import net.net63.codearcade.LSD.ui.PagedScrollPane;
import net.net63.codearcade.LSD.utils.BackgroundRenderer;
import net.net63.codearcade.LSD.utils.CentreGUI;
import net.net63.codearcade.LSD.utils.GUIBuilder;

/**
 * Created by Basim on 31/12/15.
 */
public class PackSelectScreen extends AbstractScreen {

    private BackgroundRenderer backgroundRenderer;
    private CentreGUI centreGUI;
    private PagedScrollPane pagedScrollPane;

    public PackSelectScreen(LSD game) {
        super(game);

        centreGUI = new CentreGUI();
        backgroundRenderer = new BackgroundRenderer(ShaderManager.Shaders.MENU, BackgroundRenderer.DEFAULT);

        setupUI(centreGUI.getStage());
    }

    private void setupUI(Stage stage) {

        pagedScrollPane = new PagedScrollPane();

        for (LevelManager.LevelPack levelPack: LevelManager.levelPacks) {
            Table page = createPage(levelPack);
            pagedScrollPane.addPage(page);
        }

        centreGUI.getStage().addActor(pagedScrollPane);
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
    }

    @Override
    public void render(float deltaTime) {
        super.render(deltaTime);

        backgroundRenderer.render(deltaTime);
        centreGUI.render(deltaTime);
    }

}
