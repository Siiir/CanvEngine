package io.tomneh.canvengine.exec;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Executes assigned tasks in thread pool.
 */
public class ParallelExecutor {
    protected static final ExecutorService threadPool = Executors.newCachedThreadPool();

    /**
     * Executes the given task in the thread pool.
     *
     * @param task The task to be executed.
     */
    public static void exec(Runnable task) {
        threadPool.execute(task);
    }

}
