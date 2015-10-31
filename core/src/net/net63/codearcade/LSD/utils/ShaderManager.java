package net.net63.codearcade.LSD.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.ArrayMap;

/**
 * Created by Basim on 31/10/15.
 */
public class ShaderManager {

    public static class Shaders {

        public static final String MENU = "shaders/menu";

    }
    private static final String[] _Shaders = { Shaders.MENU };
    private static final ArrayMap<String, String> shaderCache = new ArrayMap<String, String>();

    public static void loadAll() {

        for (String shaderName: _Shaders) {

            String vertex = shaderName + "_vertex.glsl";
            String fragment = shaderName + "_fragment.glsl";

            shaderCache.put(vertex, Gdx.files.internal(vertex).readString());
            shaderCache.put(fragment, Gdx.files.internal(fragment).readString());
        }

    }

    public static String getVertexShader(String shaderName) {
        return shaderCache.get(shaderName + "_vertex.glsl");
    }

    public static String getFragmentShader(String shaderName) {
        return shaderCache.get(shaderName + "_fragment.glsl");
    }

    public static ShaderProgram getShader(String shaderName) {
        return new ShaderProgram(getVertexShader(shaderName), getFragmentShader(shaderName));
    }
}
