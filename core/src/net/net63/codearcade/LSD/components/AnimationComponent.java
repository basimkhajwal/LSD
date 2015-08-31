package net.net63.codearcade.LSD.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.utils.ArrayMap;

/**
 * Created by Basim on 31/08/15.
 */
public class AnimationComponent extends Component {

    public ArrayMap<Integer, Animation> animations = new ArrayMap<Integer, Animation>();

}
