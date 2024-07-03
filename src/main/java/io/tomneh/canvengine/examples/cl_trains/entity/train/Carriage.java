package io.tomneh.canvengine.examples.cl_trains.entity.train;

import io.tomneh.canvengine.examples.cl_trains.entity.RailroadEntity;
import io.tomneh.canvengine.examples.cl_trains.res.AssetServ;

public class Carriage extends RailroadEntity {
    public boolean isLoaded= false;
    final public String sender;

    public Carriage() {
        this.sender = AssetServ.getArbFullName();
    }

    @Override
    public String entityTypeName(){
        return "Carriage";
    }

    public static Carriage constructRand() {
        return new Carriage();
    }

    @Override
    public String toString(){
        return this.entityTypeName()
                +"{"
                +"Sender = \"" + this.sender + "\""
                +"}"
                +"["
                + (this.isLoaded? "Loaded": "Empty")
                + "]";
    }
}
