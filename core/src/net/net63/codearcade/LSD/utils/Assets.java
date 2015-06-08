package net.net63.codearcade.LSD.utils;

import java.lang.reflect.Field;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

/**
 * Class to load and handle all the assets 
 * 
 * @author Basim
 *
 */
public class Assets {
	
	public static class Images {
		public static final String BACKGROUND = "images/bg.jpg";
	}
	
	public static class Fonts {
		public static final String DEFAULT = "fonts/DisplayOTF";
	}
	
	public static class FontSizes {
		public static final int TEN = 10;
		public static final int TWENTY = 20;
		public static final int FIFTY = 50;
		public static final int HUNDRED = 100;
	}
	
	private static AssetManager assetManager = new AssetManager();
	
	//Private constructor to prevent instantiation
	private Assets () { }
	
	/**
	 *  Static method that loads all the assets in the asset classes
	 */
	public static void loadAll() {
		
		for (Field image: Images.class.getDeclaredFields()) {
			try {
				assetManager.load((String) image.get(null), Texture.class);
			} catch (Exception e) {
				System.out.println("Invalid file name!!");
				e.printStackTrace();
			}
		}
		
		for (Field font: Fonts.class.getDeclaredFields()) {
			try {
				String fontName = (String) font.get(null);
				for (Field size: FontSizes.class.getDeclaredFields()) {
					int fontSize = size.getInt(null);
					assetManager.load(fontName + fontSize + ".fnt", BitmapFont.class);
				}
			} catch (Exception e) {
				System.out.println("Invalid file name!!");
				e.printStackTrace();
			}
		}
		
		assetManager.finishLoading();
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
	 * Returns a BitmapFont of the specified font and size
	 * 
	 * @param fontName	The font name
	 * @param size	The size of the font 
	 * @return The associated BitmapFont
	 */
	public static BitmapFont getFont(String fontName, int size) {
		return assetManager.get(fontName + size + ".fnt", BitmapFont.class);
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
