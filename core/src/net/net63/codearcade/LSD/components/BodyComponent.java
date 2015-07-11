package net.net63.codearcade.LSD.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.physics.box2d.Body;

/**
 * Created by Basim on 24/06/15.
 */
public class BodyComponent extends Component{

    public Body body;
    public boolean removeBody = false;
}