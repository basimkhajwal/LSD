package net.net63.codearcade.LSD.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
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
        public static final String BLOCKED = "images/blocked.png";
        public static final String SETTINGS_BACKGROUND = "images/settings_bg.png";
        public static final String NEXT_LEVEL_DISABLED = "buttons/next_level_disabled.png";
        public static final String LEVEL_SELECT_DISABLED = "buttons/level_select_disabled.png";
        public static final String LASER_BASE = "images/laser-base.png";
        public static final String LASER_HEAD = "images/laser-head.png";
        public static final String STAR = "images/star.png";
        public static final String EMPTY_STAR = "images/empty_star.png";
	}
    private static final String[] _Images = {
            Images.BACKGROUND, Images.SENSOR_TILE,
            Images.WALL_TILE, Images.TRANSITION_BACKGROUND,
            Images.TRAJECTORY, Images.NEXT_LEVEL_DISABLED,
            Images.BLOCKED, Images.SETTINGS_BACKGROUND,
            Images.LASER_BASE, Images.LASER_HEAD,
            Images.LEVEL_SELECT_DISABLED, Images.STAR,
            Images.EMPTY_STAR
        };

    public static class Buttons {
        public static final String MENU_PLAY = "buttons/menu_play";
        public static final String NEXT_LEVEL = "buttons/next_level";
        public static final String PREVIOUS_LEVEL = "buttons/previous_level";
        public static final String REPLAY_LEVEL = "buttons/replay_level";
        public static final String BACK_MENU = "buttons/back_menu";
        public static final String PLAIN = "buttons/level_select";
        public static final String PAUSE = "buttons/pause";
        public static final String SETTINGS = "buttons/settings";
        public static final String CROSS = "buttons/cross";
        public static final String BACK = "buttons/back";
        public static final String RED = "buttons/red";
        public static final String GREEN = "buttons/green";
    }
    private static final String[] _Buttons = {
            Buttons.MENU_PLAY, Buttons.NEXT_LEVEL,
            Buttons.REPLAY_LEVEL, Buttons.BACK_MENU,
            Buttons.PLAIN, Buttons.PAUSE,
            Buttons.SETTINGS, Buttons.CROSS,
            Buttons.BACK, Buttons.PREVIOUS_LEVEL,
            Buttons.RED, Buttons.GREEN
    };

    public static class FontSizes {
        public static final int TWENTY = 20;
        public static final int TWENTY_FIVE = 25;
        public static final int THIRTY = 30;
        public static final int FORTY = 40;
        public static final int FIFTY = 50;
        public static final int HUNDRED = 100;
        public static final int TWO_HUNDRED = 200;
    }
    private static final int[] _FontSizes = { FontSizes.TWENTY, FontSizes.TWENTY_FIVE, FontSizes.THIRTY, FontSizes.FORTY, FontSizes.FIFTY, FontSizes.HUNDRED, FontSizes.TWO_HUNDRED };

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

    public static final String UI_SKIN = "skin/uiskin.json";

	private static AssetManager assetManager;
	
	//Private constructor to prevent instantiation
	private Assets () { }
	
	/**
	 *  Static method that loads all the assets in the asset classes
	 */
	public static void loadAll(AssetManager manager) {

        assetManager = manager;

		for (String image: _Images) assetManager.load(image, Texture.class);
        for (String animation: _Animations) assetManager.load(animation, Texture.class);

        for (String button: _Buttons) {
            assetManager.load(button + ".png", Texture.class);
            assetManager.load(button + "_hover.png", Texture.class);
            assetManager.load(button + "_down.png", Texture.class);
        }

        assetManager.load(UI_SKIN, Skin.class);
	}

    /**
     * Separate function to load fonts synchronously
     */
    public static void loadFonts() {
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
    }

    /**
     * Handler to be called when the AssetManager finishes
     * loading all the required assets
     */
    public static void finishLoading() {
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
	 * Dispose all the assets as well as the Asset Manager (not resettable)
	 */
	public static void dispose() {
        for (ArrayMap<Integer, BitmapFont> caches: fontCache.values()) for (BitmapFont font: caches.values()) font.dispose();
	}
	
}
