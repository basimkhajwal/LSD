package net.net63.codearcade.LSD.components;

import com.badlogic.ashley.core.Component;

/**
 * Created by Basim on 15/08/15.
 */
public class StateComponent extends Component{

    private int state = 0;
    public float time = 0f;

    public int get() { return state; }

    public void set(int state) {
        this.state = state;
        time = 0f;
    }
}