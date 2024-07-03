package io.tomneh.canvengine.exec.cmd.key;

import io.tomneh.canvengine.exec.cmd.Command;
import io.tomneh.canvengine.util.SceneManager;

public record KeyboardPress(int keycode) implements Command {
    @Override
    public void exec(SceneManager ctx) {
        ctx.currentlyPressedKeys.add(this.keycode);
        System.err.printf("Received command: `KeyboardPress(%d)`.", keycode);
    }
}
