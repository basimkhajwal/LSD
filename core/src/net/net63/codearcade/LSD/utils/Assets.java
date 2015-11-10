package net.net63.codearcade.LSD.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.ArrayMap;

/**
 * Class to load and handle all the assets 
 * 
 * @author Basim
 *
 */
public class Assets {

    /* Global constants */

    public static final int FONT_FILE_SIZE = 200;

    /*-----------------*/

	public static class Images {
		public static final String BACKGROUND = "images/bg.jpg";
        public static final String SENSOR_TILE = "images/quad_grey.png";
        public static final String WALL_TILE = "images/quad.png";
        public static final String TRANSITION_BACKGROUND = "images/transition-bg.png";
        public static final String TRAJECTORY = "images/trajectory.png";

        public static final String NEXT_LEVEL_DISABLED = "buttons/next_level_disabled.png";
	}
    private static final String[] _Images = { Images.BACKGROUND, Images.SENSOR_TILE, Images.WALL_TILE, Images.TRANSITION_BACKGROUND, Images.TRAJECTORY, Images.NEXT_LEVEL_DISABLED };

    public static class Buttons {
        public static final String MENU_PLAY = "buttons/menu_play";
        public static final String NEXT_LEVEL = "buttons/next_level";
        public static final String REPLAY_LEVEL = "buttons/replay_level";
        public static final String BACK_MENU = "buttons/back_menu";
    }
    private static final String[] _Buttons = { Buttons.MENU_PLAY, Buttons.NEXT_LEVEL, Buttons.REPLAY_LEVEL, Buttons.BACK_MENU };

    public static class FontSizes {
        public static final int TWENTY = 20;
        public static final int FOURTY = 40;
        public static final int FIFTY = 50;
        public static final int HUNDRED = 100;
        public static final int TWO_HUNDRED = 200;
    }
    private static final int[] _FontSizes = { FontSizes.TWENTY, FontSizes.FOURTY, FontSizes.FIFTY, FontSizes.HUNDRED, FontSizes.TWO_HUNDRED };

	public static class Fonts {
        public static final String DIN_ALT = "fonts/DINAlternate";

		public static final String DEFAULT = DIN_ALT;
	}
    private static final String[] _Fonts = { Fonts.DIN_ALT };
    private static final ArrayMap<String, ArrayMap<Integer, BitmapFont>> fontCache = new ArrayMap<String, ArrayMap<Integer, BitmapFont>>();

    public static class Animations {
        public static final String PLAYER_STILL = "images/ball_anim.png";
        public static final String PLAYER_JUMPING = "images/ball_anim_jump.png";
        public static final String PLAYER_FALLING = "images/ball_anim_fall.png";
    }
    public static final ArrayMap<String, Animation> animationList = new ArrayMap<String, Animation>();
    private static final String[] _Animations = { Animations.PLAYER_STILL, Animations.PLAYER_JUMPING, Animations.PLAYER_FALLING };

	private static AssetManager assetManager = new AssetManager();
	
	//Private constructor to prevent instantiation
	private Assets () { }
	
	/**
	 *  Static method that loads all the assets in the asset classes
	 */
	public static void loadAll() {

		for (String image: _Images) assetManager.load(image, Texture.class);
        for (String animation: _Animations) assetManager.load(animation, Texture.class);

        for (String button: _Buttons) {
            assetManager.load(button + ".png", Texture.class);
            assetManager.load(button + "_hover.png", Texture.class);
            assetManager.load(button + "_down.png", Texture.class);
        }

		assetManager.finishLoading();

        //Load fonts separately
        for (String fontName: _Fonts) {
            ArrayMap<Integer, BitmapFont> localCache = new ArrayMap<Integer, BitmapFont>();

            for (int i = 0; i < _FontSizes.length; i++) {
                BitmapFont font = new BitmapFont(Gdx.files.internal(fontName + ".fnt"), false);
                font.getData().setScale((1.0f * _FontSizes[i]) / FONT_FILE_SIZE);

                localCache.put(_FontSizes[i], font);
            }

            fontCache.put(fontName, localCache);
        }

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
	 * Returns a BitmapFont of the specified font
	 * 
	 * @param fontName	The font name
	 * @return The associated BitmapFont
	 */
	public static BitmapFont getFont(String fontName, int size) { return fontCache.get(fontName).get(size); }


    /**
     * Get an animation that has already been loaded and cached
     *
     * @param animation
     * @return The given animation corresponding to the string
     */
    public static Animation getAnimation(String animation) {
        return animationList.get(animation);
    }

	/**
	 * Dispose all the assets but the Asset Manager still remains (re-usable)
	 */
	public static void clear() {
		assetManager.clear();
        for (ArrayMap<Integer, BitmapFont> caches: fontCache.values()) for (BitmapFont font: caches.values()) font.dispose();
	}
	
	/**
	 * Dispose all the assets as well as the Asset Manager (not resettable)
	 */
	public static void dispose() {
		assetManager.dispose();
        for (ArrayMap<Integer, BitmapFont> caches: fontCache.values()) for (BitmapFont font: caches.values()) font.dispose();
	}
	
}
