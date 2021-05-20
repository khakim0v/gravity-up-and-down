package cz.cvut.fel.khakikir.gravityupdown.engine.util;

import cz.cvut.fel.khakikir.gravityupdown.engine.Engine;
import cz.cvut.fel.khakikir.gravityupdown.engine.entity.MapBasic;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class EngineTimerManager extends MapBasic {
    // TODO: Remove all timers when switching state (preSwitch)
    private final List<EngineTimer> timers = new ArrayList<>();

    public EngineTimerManager() {
        // Don't call draw
        visible = false;
    }

    /**
     * Called before the game state has been updated.
     * Cycles through timers and calls update() on each one.
     */
    @Override
    public void update() {
        List<EngineTimer> loopedTimers = null;

        for (EngineTimer timer : timers) {
            if (timer.active && !timer.finished && timer.time >= 0) {
                int timerLoops = timer.getElapsedLoops();
                timer.update(Engine.elapsed);

                if (timerLoops != timer.getElapsedLoops()) {
                    if (loopedTimers == null)
                        loopedTimers = new LinkedList<>();

                    loopedTimers.add(timer);
                }
            }
        }

        if (loopedTimers != null) {
            for (EngineTimer timer : loopedTimers) {
                timer.onLoopFinished();
            }
        }
    }

    /**
     * Add a new timer to the timer manager.
     * Called when EngineTimer is started.
     *
     * @param timer The EngineTimer you want to add to the manager.
     */
    public void add(EngineTimer timer) {
        timers.add(timer);
    }

    /**
     * Remove a timer from the timer manager.
     * Called automatically by EngineTimer's cancel() function.
     *
     * @param timer The EngineTimer you want to remove from the manager.
     */
    public void remove(EngineTimer timer) {
        timers.remove(timer);
    }

    /**
     * Removes all the timers from the timer manager.
     */
    public void clear() {
        timers.clear();
    }
}
