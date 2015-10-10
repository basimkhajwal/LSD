package net.net63.codearcade.LSD.utils;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.ArrayMap;

/**
 * Class to load and handle all the assets 
 * 
 * @author Basim
 *
 */
public class Assets {
	
	public static class Images {
		public static final String BACKGROUND = "images/bg.jpg";
        public static final String SENSOR_TILE = "images/quad_grey.png";
        public static final String WALL_TILE = "images/quad.png";
        public static final String TRANSITION_BACKGROUND = "images/transition-bg.png";
	}
    private static final String[] _Images = { Images.BACKGROUND, Images.SENSOR_TILE, Images.WALL_TILE, Images.TRANSITION_BACKGROUND };

    public static class Buttons {
        public static final String MENU_PLAY = "buttons/menu_play";
        public static final String NEXT_LEVEL = "buttons/next_level";
        public static final String REPLAY_LEVEL = "buttons/replay_level";
        public static final String BACK_MENU = "buttons/back_menu";
    }
    private static final String[] _Buttons = { Buttons.MENU_PLAY, Buttons.NEXT_LEVEL, Buttons.REPLAY_LEVEL, Buttons.BACK_MENU };


	public static class Fonts {
        public static final String DIN_ALT = "fonts/DINAlternate";
        public static final String DISPLAY_OTF = "fonts/DisplayOTF";

		public static final String DEFAULT = DIN_ALT;
	}
    private static final String[] _Fonts = { Fonts.DIN_ALT, Fonts.DISPLAY_OTF };
	
	public static class FontSizes {
        public static final int TEN = 10;
        public static final int TWENTY = 20;
        public static final int FIFTY = 50;
        public static final int HUNDRED = 100;
    }
    private static final int[] _FontSizes = { FontSizes.TEN, FontSizes.TWENTY, FontSizes.FIFTY, FontSizes.HUNDRED };

    public static class Animations {
        public static final String PLAYER_STILL = "images/ball_anim.png";
        public static final String PLAYER_JUMPING = "images/ball_anim_jump.png";
        public static final String PLAYER_FALLING = "images/ball_anim_fall.png";
    }
    public static final ArrayMap<String, Animation> animationList = new ArrayMap<String, Animation>();
    private static final String[] _Animations = { Animations.PLAYER_STILL, Animations.PLAYER_JUMPING, Animations.PLAYER_FALLING };

    public static class LevelMaps {
        public static final String TEST = "maps/test.tmx";
        public static final String TEST2 = "maps/test2.tmx";
    }
    private static final String[] _LevelMaps = { LevelMaps.TEST, LevelMaps.TEST2 };

	private static AssetManager assetManager = new AssetManager();
	
	//Private constructor to prevent instantiation
	private Assets () { }
	
	/**
	 *  Static method that loads all the assets in the asset classes
	 */
	public static void loadAll() {

        //Add loaders
        assetManager.setLoader(TiledMap.class, new TmxMapLoader(new InternalFileHandleResolver()));

		for (String image: _Images) assetManager.load(image, Texture.class);
        for (String animation: _Animations) assetManager.load(animation, Texture.class);
        for (String levelMap: _LevelMaps) assetManager.load(levelMap, TiledMap.class);

		for (String font: _Fonts) {
            for (int size: _FontSizes) {
                assetManager.load(font + size + ".fnt", BitmapFont.class);
            }
		}

        for (String button: _Buttons) {
            assetManager.load(button + ".png", Texture.class);
            assetManager.load(button + "_hover.png", Texture.class);
            assetManager.load(button + "_down.png", Texture.class);
        }

		assetManager.finishLoading();

        //Setup the animations
        for (String animation: _Animations) {
            TextureRegion texture = new TextureRegion(assetManager.get(animation, Texture.class));
            TextureRegion[] regions = texture.split(24, 24)[0];

            animationList.put(animation, new Animation(0.1f, regions));
        }
	}
	
	/**
	 * Get a loaded asset of type T from the loaded cache
	 * 
	 * @param fileName The identifier for the asset 
	 * @param type The type to return
	 * @return	The asset with the type as specified
	 */
	public static <T> T getAsset(String fileName, Class<T> type) {
		return assetManager.get(fileName, type);
	}


    /**
     *
     * Returns a loaded tiled map based on the map's filename
     *
     * @param fileName The tiled map file name
     * @return The respective tiled map
     */
    public static TiledMap getTiledMap(String fileName) { return getAsset(fileName, TiledMap.class); }

	/**
	 * Returns a BitmapFont of the specified font and size
	 * 
	 * @param fontName	The font name
	 * @param size	The size of the font 
	 * @return The associated BitmapFont
	 */
	public static BitmapFont getFont(String fontName, int size) { return getAsset(fontName + size + ".fnt", BitmapFont.class); }


    public static Animation getAnimation(String animation) {
        return animationList.get(animation);
    }


    public static ImageButton createButton(String button) {
        Texture txt = getAsset(button + ".png", Texture.class);
        Texture txtDwn = getAsset(button + "_down.png", Texture.class);
        Texture txtHover = getAsset(button + "_hover.png", Texture.class);

        TextureRegionDrawable btn = new TextureRegionDrawable(new TextureRegion(txt));
        TextureRegionDrawable btnDown = new TextureRegionDrawable(new TextureRegion(txtDwn));
        TextureRegionDrawable btnChecked = new TextureRegionDrawable(new TextureRegion(txtHover));

        return new ImageButton(btn, btnDown, btnChecked);
    }


	/**
	 * Dispose all the assets but the Asset Manager still remains (re-usable)
	 */
	public static void clear() {
		assetManager.clear();
	}
	
	/**
	 * Dispose all the assets as well as the Asset Manager (not resettable)
	 */
	public static void dispose() {
		assetManager.dispose();
	}
	
}
