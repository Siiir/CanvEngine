package io.tomneh.canvengine.examples.cl_trains.entity;

import io.tomneh.canvengine.entity.Entity;

public abstract class RailroadEntity extends Entity {
    public abstract String entityTypeName();

    @Override
    public String toString(){
        return entityTypeName();
    }
}
