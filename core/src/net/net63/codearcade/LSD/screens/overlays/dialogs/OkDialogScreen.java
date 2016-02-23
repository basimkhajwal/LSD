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
 * Created by Basim on 23/02/16.
 */
public class OkDialogScreen extends AbstractDialog {

    private TextButton okButton;
    private boolean buttonClicked = false;

    public OkDialogScreen(LSD game, AbstractScreen previousScreen, String title, String message) {
        super(game, previousScreen, title, message);
    }

    @Override
    public void setupTable(Table contentTable) {

        okButton = GUIBuilder.createTextButton(Assets.Buttons.PLAIN, "Ok", Assets.FontSizes.TWENTY, Color.BLACK);
        okButton.addListener(new ClickListener() {

            @Override
            public void clicked(InputEvent event, float x, float y) {
                buttonClicked = true;
            }

        });

        contentTable.add(okButton).size(70, 35).padLeft(5).padBottom(20).colspan(2);
    }

    @Override
    public void update() {
        okButton.setChecked(okButton.isOver());

        if (buttonClicked) {
            previousScreen.resumeLogic();
            listener.handleResult(DialogResult.CLOSED);
            game.setScreen(previousScreen);
        }
    }

}
