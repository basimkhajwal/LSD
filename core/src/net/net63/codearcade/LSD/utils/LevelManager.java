package net.net63.codearcade.LSD.utils;

/**
 * A level manager class to handle the map files
 * and to hold data about which maps are available
 * and how many there are
 *
 * Created by Basim on 08/11/15.
 */
public class LevelManager {

    public static final LevelPack[] levelPacks = new LevelPack[]{
        new LevelPack("original", "maps/level", 16)
    };


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
            assert num < numLevels;
            return fileStub + num + ".tmx";
        }
    }

}
