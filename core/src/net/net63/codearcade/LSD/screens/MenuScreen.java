package net.net63.codearcade.LSD.screens;

import net.net63.codearcade.LSD.LSD;
import net.net63.codearcade.LSD.utils.Assets;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;


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
	
	
	public MenuScreen(LSD game) {
		super(game);
		this.clear = new Color(1, 0, 0, 1);
		
		stage = new Stage();
		
		backgroundImage = new Image(Assets.getAsset(Assets.Images.BACKGROUND, Texture.class));
	}
	
	@Override
	public void show() {
		super.show();
		
		stage.addActor(backgroundImage);
	}
	
	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
		
		stage.getViewport().setScreenSize(width, height);
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
