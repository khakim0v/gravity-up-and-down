package cz.cvut.fel.khakikir.gravityupdown.engine.util;

import java.util.function.Consumer;

public class EngineTimer {
    /**
     * The global timer manager that handles global timers
     */
    public static EngineTimerManager globalManager = new EngineTimerManager();

    /**
     * The manager to which this timer belongs
     */
    public EngineTimerManager manager;

    /**
     * How much time the timer was set for.
     */
    public double time;

    /**
     * How many loops the timer was set for. 0 means "looping forever".
     */
    public int loops;

    /**
     * Pauses or checks the pause state of the timer.
     */
    public boolean active = false;

    /**
     * Check to see if the timer is finished.
     */
    public boolean finished = false;

    /**
     * Function that gets called when timer completes.
     */
    public Consumer<EngineTimer> onComplete;

    /**
     * Internal tracker for the actual timer counting up.
     */
    private double timeCounter = 0;

    /**
     * Internal tracker for the loops counting up.
     */
    private int loopsCounter = 0;

    private boolean _inManager = false;

    /**
     * Creates a new timer.
     */
    public EngineTimer(EngineTimerManager manager) {
        this.manager = manager != null ? manager : globalManager;
    }

    /**
     * Starts the timer and adds the timer to the timer manager.
     *
     * @param time       How many seconds it takes for the timer to go off.
     *                   If 0 then timer will fire OnComplete callback only once at the first call
     *                   of update method (which means that loops argument will be ignored).
     * @param onComplete Optional, triggered whenever the time runs out, once for each loop.
     * @param loops      How many times the timer should go off. 0 means "looping forever".
     * @return A reference to itself (handy for chaining or whatever).
     */
    public EngineTimer start(double time, Consumer<EngineTimer> onComplete, int loops) {
        if (manager != null && !_inManager) {
            manager.add(this);
            _inManager = true;
        }

        active = true;
        finished = false;
        this.time = Math.abs(time);

        if (loops < 0)
            loops *= -1;

        this.loops = loops;
        this.onComplete = onComplete;
        timeCounter = 0;
        loopsCounter = 0;

        return this;
    }

    /**
     * Restart the timer using the new duration
     *
     * @param newTime The duration of this timer in seconds.
     */
    public EngineTimer reset(double newTime) {
        if (newTime < 0)
            newTime = time;

        start(newTime, onComplete, loops);
        return this;
    }

    /**
     * Stops the timer and removes it from the timer manager.
     */
    public void cancel() {
        finished = true;
        active = false;

        if (manager != null && _inManager) {
            manager.remove(this);
            _inManager = false;
        }
    }

    /**
     * Called by the timer manager plugin to update the timer.
     * If time runs out, the loop counter is advanced, the timer reset, and the callback called if it exists.
     * If the timer runs out of loops, then the timer calls cancel().
     * However, callbacks are called AFTER cancel() is called.
     */
    public void update(double elapsed) {
        timeCounter += elapsed;

        while ((timeCounter >= time) && active && !finished) {
            timeCounter -= time;
            loopsCounter++;

            if (loops > 0 && (loopsCounter >= loops)) {
                finished = true;
            }
        }
    }

    public void onLoopFinished() {
        if (finished)
            cancel();

        if (onComplete != null)
            onComplete.accept(this);
    }

    public double getTimeLeft() {
        return time - timeCounter;
    }

    public double getElapsedTime() {
        return timeCounter;
    }

    public int getLoopsLeft() {
        return loops - loopsCounter;
    }

    public int getElapsedLoops() {
        return loopsCounter;
    }

    public double getProgress() {
        return (time > 0) ? (timeCounter / time) : 0;
    }
}
