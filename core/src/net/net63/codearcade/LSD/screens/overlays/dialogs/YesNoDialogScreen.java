package net.net63.codearcade.LSD.screens.overlays.dialogs;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import net.net63.codearcade.LSD.LSD;
import net.net63.codearcade.LSD.managers.Assets;
import net.net63.codearcade.LSD.screens.AbstractScreen;
import net.net63.codearcade.LSD.utils.GUIBuilder;

/**
 * Created by Basim on 31/01/16.
 */
public class YesNoDialogScreen extends AbstractDialog {

    private TextButton yesButton;
    private TextButton noButton;

    private boolean reverseColours = false;

    private boolean yesClicked = false;
    private boolean noClicked = false;

    public YesNoDialogScreen(LSD game, AbstractScreen previousScreen, String title, String message) {
        super(game, previousScreen, title, message);
    }

    public YesNoDialogScreen(LSD game, AbstractScreen previousScreen, String title, String message, boolean reverseColours) {
        this(game, previousScreen, title, message);

        this.reverseColours = reverseColours;
    }

    @Override
    public void setupTable(Table contentTable) {

        String yesString = reverseColours ? Assets.Buttons.RED : Assets.Buttons.GREEN;
        String noString = reverseColours ? Assets.Buttons.GREEN : Assets.Buttons.RED;

        yesButton = GUIBuilder.createTextButton(yesString, "Yes", Assets.FontSizes.TWENTY, Color.WHITE);
        noButton = GUIBuilder.createTextButton(noString, "No", Assets.FontSizes.TWENTY, Color.WHITE);

        yesButton.addListener(new ClickListener() {

            @Override
            public void clicked(InputEvent event, float x, float y) {
                yesClicked = true;
            }

        });

        noButton.addListener(new ClickListener() {

            @Override
            public void clicked(InputEvent event, float x, float y) {
                noClicked = true;
            }

        });

        float btnwidth = 70f;
        float btnheight = 35f;

        contentTable.add(noButton).size(btnwidth, btnheight).padLeft(5).padBottom(20).spaceRight(10);
        contentTable.add(yesButton).size(btnwidth, btnheight).padRight(5).padBottom(20);
    }

    @Override
    public void update() {
        yesButton.setChecked(yesButton.isOver());
        noButton.setChecked(noButton.isOver());

        if (yesClicked || noClicked) {
            previousScreen.resumeLogic();
            listener.handleResult(yesClicked ? DialogResult.YES : DialogResult.NO);

            game.setScreen(previousScreen);
        }
    }
}
