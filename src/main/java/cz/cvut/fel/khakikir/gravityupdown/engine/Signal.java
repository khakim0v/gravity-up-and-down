package cz.cvut.fel.khakikir.gravityupdown.engine;

import cz.cvut.fel.khakikir.gravityupdown.engine.util.Procedure;

import java.util.ArrayList;
import java.util.List;

public class Signal {
    private final List<Procedure> callbacks;

    public Signal() {
        this.callbacks = new ArrayList<>();
    }

    public void add(Procedure procedure) {
        callbacks.add(procedure);
    }

    public void remove(Procedure procedure) {
        callbacks.remove(procedure);
    }

    public void handle() {
        for (Procedure callback : callbacks) {
            callback.run();
        }
    }
}
