package net.net63.codearcade.LSD.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.ArrayMap;

/**
 * Manager class to handle loading and acessing
 * shaders and shader programs globally
 *
 * Created by Basim on 31/10/15.
 */
public class ShaderManager {


    public static class Shaders {

        public static final String MENU = "shaders/menu";

    }
    private static final String[] _Shaders = { Shaders.MENU };
    private static final ArrayMap<String, String> shaderCache = new ArrayMap<String, String>();

    /**
     * Loads all the shader strings
     */
    public static void loadAll() {

        for (String shaderName: _Shaders) {

            String vertex = shaderName + "_vertex.glsl";
            String fragment = shaderName + "_fragment.glsl";

            shaderCache.put(vertex, Gdx.files.internal(vertex).readString());
            shaderCache.put(fragment, Gdx.files.internal(fragment).readString());
        }

    }

    /**
     * Get the cached vertex shader
     *
     * @param shaderName The shader name
     * @return The vertex shader string
     */
    public static String getVertexShader(String shaderName) {
        return shaderCache.get(shaderName + "_vertex.glsl");
    }

    /**
     * Get the cached fragment shader
     *
     * @param shaderName The shader name
     * @return The fragment shader string
     */
    public static String getFragmentShader(String shaderName) {
        return shaderCache.get(shaderName + "_fragment.glsl");
    }

    /**
     * Utility function to merge the vertex and the fragment
     * shaders into one shader program
     *
     * @param shaderName The specified shader to get
     * @return The shader program with the vertex and fragment shader
     */
    public static ShaderProgram getShader(String shaderName) {
        return new ShaderProgram(getVertexShader(shaderName), getFragmentShader(shaderName));
    }
}
