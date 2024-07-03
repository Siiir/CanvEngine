package io.tomneh.canvengine.components.logic;

import io.tomneh.canvengine.components.Compo;
import io.tomneh.canvengine.util.SceneManager;

public interface Updating extends Compo {
    void update(SceneManager ctx);
}
