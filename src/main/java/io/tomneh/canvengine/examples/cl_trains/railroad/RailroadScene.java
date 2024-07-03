package io.tomneh.canvengine.examples.cl_trains.railroad;

import io.tomneh.canvengine.entity.Entity;
import io.tomneh.canvengine.examples.cl_trains.entity.RailroadEntity;
import io.tomneh.canvengine.util.Scene;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class RailroadScene extends Scene {
    transient HashMap<Long, RailroadEntity> railroadEntities;
    private long freeRailroadId= 0;
    @Override
    protected void initTransients(){
        super.initTransients();
        this.railroadEntities= new HashMap<>();
    }

    @Override
    public void slowlyRegCompos(Entity entity) {
        super.slowlyRegCompos(entity);
        if (entity instanceof RailroadEntity rE){
            for (;this.railroadEntities.containsKey(this.freeRailroadId);){
                this.freeRailroadId++;
            }
            this.railroadEntities.put(this.freeRailroadId++, rE);
        }
    }
    @Override
    public void slowlyUnregCompos(Entity entity) {
        super.slowlyUnregCompos(entity);
    }

    public RailroadEntity getRailroadEntity(long id){
        return this.railroadEntities.get(id);
    }

    public Set<Map.Entry<Long, RailroadEntity>> getRailroadEntities(){
        return this.railroadEntities.entrySet();
    }
    public Set<Long> getRailroadEntityIds(){
        return this.railroadEntities.keySet();
    }

    public void removeRailroadEntity(long id){
        this.unregisterEntity(this.getRailroadEntity(id));
        this.railroadEntities.remove(id);
    }
}
