package net.net63.codearcade.LSD.systems;

import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import net.net63.codearcade.LSD.utils.Constants;
import sun.font.SunFontManager;

/**
 * Created by Basim on 03/09/15.
 */
public class BackgroundRenderSystem extends EntitySystem {

    public BackgroundRenderSystem() {
        super(Constants.SYSTEM_PRIORITIES.BACKGROUND_RENDER);
    }

    @Override
    public void update(float deltaTime) {

    }
}
