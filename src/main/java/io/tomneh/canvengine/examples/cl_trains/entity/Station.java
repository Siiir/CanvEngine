package io.tomneh.canvengine.examples.cl_trains.entity;

import io.tomneh.canvengine.examples.cl_trains.entity.train.Carriage;

public class Station extends RailroadEntity{
    public static Station constructRand() {
        return new Station();
    }

    @Override
    public String entityTypeName() {
        return "Station";
    }
}
