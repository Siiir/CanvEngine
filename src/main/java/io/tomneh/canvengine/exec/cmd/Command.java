package io.tomneh.canvengine.exec.cmd;

import io.tomneh.canvengine.util.SceneManager;

public interface Command {
    void exec(SceneManager ctx);
}
