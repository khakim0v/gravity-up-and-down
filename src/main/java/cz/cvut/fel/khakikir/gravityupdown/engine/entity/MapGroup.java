package cz.cvut.fel.khakikir.gravityupdown.engine.entity;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class MapGroup extends MapBasic {
    private final List<MapBasic> members;

    public MapGroup() {
        this.members = new ArrayList<>();
    }

    /**
     * Adds a new `MapBasic` subclass (`MapBasic`, `MapSprite`, `Object`, etc) to the group.
     * `MapGroup` will try to replace a `null` member of the list first.
     * Failing that, `FlxGroup` will add it to the end of the member array.
     *
     * @param object The object you want to add to the group.
     */
    public void add(MapBasic object) {
        if (object == null) return;

        // Don't bother adding an object twice.
        if (members.contains(object)) return;

        // First, look for a null entry where we can add the object.
        int index = members.indexOf(null);
        if (index != -1) {
            members.set(index, object);
            return;
        }

        members.add(object);
    }

    /**
     * Removes an object from the group.
     *
     * @param object The `MapBasic` you want to remove.
     * @param splice Whether the object should be cut from the array entirely or not.
     */
    public void remove(MapBasic object, boolean splice) {
        int index = members.indexOf(object);

        if (index < 0) return;

        if (splice) {
            members.remove(index);
        } else {
            members.set(index, null);
        }
    }


    /**
     * Automatically goes through and calls update on everything you added.
     */
    @Override
    public void update() {
        for (MapBasic member : members) {
            if (member != null && member.active) {
                member.update();
            }
        }
    }

    /**
     * Automatically goes through and calls render on everything you added.
     */
    @Override
    public void draw(Graphics2D g) {
        for (MapBasic member : members) {
            if (member != null && member.visible) {
                member.draw(g);
            }
        }
    }
}
