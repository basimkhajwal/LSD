package net.net63.codearcade.LSD.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Created by Basim on 05/07/15.
 */
public class RenderComponent implements Component {

    public TextureRegion texture;
    public boolean render = true;

    public float tileWidth;
    public float tileHeight;
    public boolean tileToSize = false;

}
