package net.net63.codearcade.LSD.client;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;
import net.net63.codearcade.LSD.LSD;
import net.net63.codearcade.LSD.utils.Constants;

public class HtmlLauncher extends GwtApplication {

        @Override
        public GwtApplicationConfiguration getConfig () {
                return new GwtApplicationConfiguration(
                        Constants.DEFAULT_SCREEN_WIDTH,
                        Constants.DEFAULT_SCREEN_HEIGHT);
        }

        @Override
        public ApplicationListener getApplicationListener () {
                return new LSD();
        }
}