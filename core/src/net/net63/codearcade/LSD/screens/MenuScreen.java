package net.net63.codearcade.LSD.screens;

import net.net63.codearcade.LSD.LSD;
import net.net63.codearcade.LSD.utils.Assets;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.utils.viewport.FitViewport;


/**
 * The Menu Screen state where the user selects the options they want to
 * do
 * 
 * @author Basim
 *
 */
public class MenuScreen extends AbstractScreen{
	
	private Stage stage;
	
	private Image backgroundImage;
	private Label title;
	
	public MenuScreen(LSD game) {
		super(game);
		this.clear = new Color(1, 0, 0, 1);
		
		stage = new Stage(new FitViewport(800, 400));
		title = new Label("Little Sticky Destroyer", 
				new LabelStyle(Assets.getFont(Assets.Fonts.DEFAULT, Assets.FontSizes.FIFTY), Color.ORANGE));
		title.setX(100);
		title.setY(20);
		
		setupBackground();
	}
	
	private void setupBackground() {
		Texture backgroundTexture = Assets.getAsset(Assets.Images.BACKGROUND, Texture.class);
		backgroundTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		backgroundImage = new Image(backgroundTexture);
	}
	
	@Override
	public void show() {
		super.show();
		
		stage.addActor(backgroundImage);
		stage.addActor(title);
	}
	
	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
		
		stage.getViewport().update(width, height);
		backgroundImage.setBounds(0, 0, width, height);
		
	}
	
	@Override
	public void render(float delta) {
		super.render(delta);
		
		stage.act(delta);
		stage.draw();
	}
	
	@Override
	public void dispose() {
		super.dispose();
		
		stage.dispose();
	}
	
}
