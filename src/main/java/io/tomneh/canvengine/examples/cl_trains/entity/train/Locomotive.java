package io.tomneh.canvengine.examples.cl_trains.entity.train;

import io.tomneh.canvengine.examples.cl_trains.entity.RailroadEntity;

public class Locomotive extends RailroadEntity {
    @Override
    public String entityTypeName() {
        return "Locomotive";
    }

    public static Locomotive constructRand() {
        return new Locomotive();
    }
}
