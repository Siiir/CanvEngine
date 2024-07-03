package io.tomneh.canvengine.examples.cl_trains.entity.train;

import io.tomneh.canvengine.examples.cl_trains.entity.RailroadEntity;

public class Train extends RailroadEntity {
    public static Train constructRand() {
        return new Train();
    }

    @Override
    public String entityTypeName() {
        return "Train";
    }
}
