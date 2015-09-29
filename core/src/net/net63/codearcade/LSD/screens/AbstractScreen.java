package net.net63.codearcade.LSD.screens;

import net.net63.codearcade.LSD.LSD;
import net.net63.codearcade.LSD.utils.Constants;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;


/**
 * An abstract class that adds on top of the game state screen implemented in libGDX
 * by adding more debug logging 
 * 
 * @author Basim
 *
 */
public abstract class AbstractScreen implements Screen{
	
	//The top level game holder
	protected final LSD game;
	
	//Background colour
    protected Color clear = new Color(0f,0f,0f,1f);

	public AbstractScreen(LSD game ){
		this.game = game;
	}

	@Override
	public void render(float delta){
		Gdx.gl.glClearColor( clear.r, clear.g, clear.b, clear.a);
		Gdx.gl.glClear( GL20.GL_COLOR_BUFFER_BIT );
	}

	@Override
	public void hide(){ Gdx.app.log( Constants.LOG, "Hiding screen: " + getName() ); }
	
	@Override
	public void show(){ Gdx.app.log( Constants.LOG, "Showing screen: " + getName() ); }
	
	@Override
	public void resize(int width,int height ){ Gdx.app.log( Constants.LOG, "Resizing screen: " + getName() + " to: " + width + " x " + height ); }

	@Override
	public void pause(){ Gdx.app.log( Constants.LOG, "Pausing screen: " + getName() ); }

	@Override
	public void resume(){ Gdx.app.log( Constants.LOG, "Resuming screen: " + getName() ); }

	@Override
	public void dispose(){ Gdx.app.log( Constants.LOG, "Disposing screen: " + getName() ); }
	
	protected String getName() {
		return getClass().getSimpleName();
	}
}
