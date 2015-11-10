package net.net63.codearcade.LSD.utils;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;

/**
 * A level manager class to handle the map files
 * and to hold data about which maps are available
 * and how many there are
 *
 * Created by Basim on 08/11/15.
 */
public class LevelManager {

    //The level packs available
    public static final LevelPack[] LEVEL_PACKS = new LevelPack[]{
        new LevelPack("original", "maps/level", 16)
    };

    //Level data caches
    private static final TiledMap[][] levelPackCache = new TiledMap[LEVEL_PACKS.length][];

    /**
     * Load all the maps into a cache
     */
    public static void loadAll() {

        TmxMapLoader mapLoader = new TmxMapLoader();

        //Iterate over each level pack
        for (int i = 0; i < LEVEL_PACKS.length; i++) {
            LevelPack pack = LEVEL_PACKS[i];

            //Load each level into the cache
            levelPackCache[i] = new TiledMap[pack.numLevels];
            for (int j = 0; j < pack.numLevels; j++) {
                levelPackCache[i][j] = mapLoader.load(pack.getLevel(j));
            }
        }

    }

    /**
     * Disposes all the maps stored in the cache
     */
    public static void dispose() {

        //Double iterate and call dispose on every level
        for (TiledMap[] levels : levelPackCache) {
            //Ignore empty arrays
            if (levels == null) continue;

            for (TiledMap level: levels) level.dispose();
        }

    }

    /**
     * Level pack class for each pack that
     * contains information about how many
     * levels are stored and the files
     * locations
     */
    public static class LevelPack {
        public final String name;
        public final String fileStub;
        public final int numLevels;

        public LevelPack(String name, String fileStub, int numLevels) {
            this.name = name;
            this.fileStub = fileStub;
            this.numLevels = numLevels;
        }

        /**
         *
         * @param num The index of the level to retrieve, must be less than the number of levels
         * @return The path of the given indexed level
         */
        public String getLevel(int num) {
            return fileStub + (num + 1) + ".tmx";
        }
    }

}
