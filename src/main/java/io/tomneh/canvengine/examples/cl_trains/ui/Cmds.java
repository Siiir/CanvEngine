package io.tomneh.canvengine.examples.cl_trains.ui;

import io.tomneh.canvengine.examples.cl_trains.entity.Station;
import io.tomneh.canvengine.examples.cl_trains.entity.train.Carriage;
import io.tomneh.canvengine.examples.cl_trains.entity.train.Locomotive;
import io.tomneh.canvengine.examples.cl_trains.entity.train.Train;
import io.tomneh.canvengine.examples.cl_trains.railroad.RailroadMgr;
import io.tomneh.canvengine.examples.cl_trains.railroad.RailroadScene;
import io.tomneh.canvengine.exec.cmd.Command;
import io.tomneh.canvengine.util.SyncChannel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class Cmds {
    final protected SyncChannel<Command> cmdChannel;
    public final ReentrantLock stdoutLock;
    final protected HashMap<String, HashMap<String, Consumer<String[]>>> userCmds;

    public Cmds(SyncChannel<Command> commandChannel, ReentrantLock stdoutLock) {
        this.cmdChannel = commandChannel;
        this.stdoutLock = stdoutLock;

        Cmds theseCmds= this;
        this.userCmds = new HashMap<>() {
            {
                this.put("create", new HashMap<>() {
                    {
                        this.put("train", args -> {
                            if (args.length > 0){
                                throw new UnsupportedOperationException();
                            }
                            cmdChannel.push(ctx -> {
                                var railroadMgr= (RailroadMgr)ctx;
                                Train train= Train.constructRand();
                                railroadMgr.getScene().registerEntity(train);
                            });
                        });
                        this.put("locomotive", args -> {
                            if (args.length > 0){
                                throw new UnsupportedOperationException();
                            }
                            cmdChannel.push(ctx -> {
                                var railroadMgr= (RailroadMgr)ctx;
                                Locomotive locomotive= Locomotive.constructRand();
                                railroadMgr.getScene().registerEntity(locomotive);
                            });
                        });
                        this.put("car", args -> {
                            if (args.length > 0){
                                throw new UnsupportedOperationException();
                            }
                            cmdChannel.push(ctx -> {
                                var railroadMgr= (RailroadMgr)ctx;
                                Carriage car= Carriage.constructRand();
                                railroadMgr.getScene().registerEntity(car);
                            });
                        });
                        this.put("station", args -> {
                            if (args.length > 0){
                                throw new UnsupportedOperationException();
                            }
                            cmdChannel.push(ctx -> {
                                var railroadMgr= (RailroadMgr)ctx;
                                Station station= Station.constructRand();
                                railroadMgr.getScene().registerEntity(station);
                            });
                        });
                    }
                });
                this.put("assign", new HashMap<>() {
                    {
                        this.put("car", args -> {
                            System.err.println("Unsupported operation.");
                            /*ToDo-4*/
                        });
                    }
                });
                this.put("load", new HashMap<>() {
                    {
                        this.put("cars", args -> {
                            if (args.length == 0){
                                System.out.println("Specify which cars to load." +
                                        " Eg.: `load cars  0 6 9`");
                            }else {
                                var ids= Arrays.stream(args).map(s-> Long.parseLong(s)).toList();
                                theseCmds.cmdChannel.push(ctx -> {
                                    var mgr= (RailroadMgr) ctx;
                                    RailroadScene scene = mgr.getScene();
                                    for (long id :
                                            ids) {
                                        ((Carriage)scene.getRailroadEntity(id)).isLoaded= true;
                                    }
                                });
                            }
                        });
                    }
                });
                this.put("remove", new HashMap<>() {
                    {
                        this.put("entities", args -> {
                            if (args.length == 0){
                                System.err.println("`remove entities` must have at least one argument.");
                                return;
                            }
                            var entityIds= Arrays.stream(args).map(Long::parseLong).toList();
                            theseCmds.cmdChannel.push(ctx -> {
                                var mgr= (RailroadMgr)ctx;
                                for (long id :
                                        entityIds) {
                                    RailroadScene scene = mgr.getScene();
                                    scene.removeRailroadEntity(id);
                                }
                            });
                        });
                    }
                });
                this.put("list", new HashMap<>() {
                    {
                        this.put("commands", args -> {
                            UserCmd.printAllMainCmds(theseCmds);
                        });
                        this.put("subcommands", args -> {
                            if (args.length == 0){
                                System.out.println(
                                        "Please, specify for which main commands you want to list subcommands.\n"
                                                +"E.g. command usage `list subcommands load remove`,"
                                                +" will print subcommands of `load` & `remove` commands."
                                );
                                return;
                            }
                            for (var subcmdName : args){
                                UserCmd.attemptPrintAllSubcmds(theseCmds, subcmdName);
                            }
                        });
                        this.put("entities", args -> {
                            if (args.length > 0){
                                System.err.println("`list entities` doesn't take any arguments.");
                                return;
                            }
                            var lock= new Object();
                            theseCmds.cmdChannel.push( ctx -> {
                                var mgr= (RailroadMgr) ctx;
                                var ids= mgr.getScene().getRailroadEntityIds().stream();
                                System.out.printf( "All ids of existing entities are: {%s}.",
                                        ids.map(id-> id.toString())
                                                .collect(Collectors.joining(", "))
                                );
                                lock.notify();
                            });
                            synchronized (lock){
                                try {
                                    lock.wait();
                                } catch (InterruptedException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        });
                    }
                });
                this.put("info", new HashMap<>() {
                    {
                        this.put("entities", args -> {
                            if (args.length == 0){
                                Object lock= new Object();
                                theseCmds.cmdChannel.push(ctx -> {
                                    var mgr= (RailroadMgr)ctx;
                                    var railroadEntities = mgr.getScene().getRailroadEntities();
                                    if (railroadEntities.size()>0){
                                        railroadEntities.forEach(entry-> {
                                            System.out.printf("\tInfo for %d: %s\n", entry.getKey(), entry.getValue());
                                        });
                                    }
                                    synchronized (lock){
                                        lock.notify();
                                    }
                                });
                                synchronized (lock){
                                    try {
                                        lock.wait();
                                    } catch (InterruptedException e) {
                                        throw new RuntimeException(e);
                                    }
                                }
                            }else{
                                var entityIds= new ArrayList<Long>(args.length);
                                for (var arg : args){
                                    Long entityId= Long.parseLong(arg);
                                    entityIds.add( entityId );
                                }
                                theseCmds.cmdChannel.push(
                                        ctx -> {
                                            System.out.println();
                                            for (long id :
                                                    entityIds) {
                                                var railRoadMgr = (RailroadMgr) ctx;
                                                var entity = railRoadMgr.getScene().getRailroadEntity(id);
                                                var entityInfo = entity == null ? "It doesn't exist." : entity.toString();

                                                System.out.printf("Info for %d: %s\n", id, entityInfo);
                                            }
                                        }
                                );
                            }
                        });
                        this.put("command", args -> {
                            if (args.length == 0 || args.length>2){
                                throw new UnsupportedOperationException();
                            }
                            String mainCmd = args[0];
                            switch (mainCmd){
                                case "info":
                                    if (args.length == 1){
                                        System.out.print(
                                            "`info` command gives you helpful information about given topic. Eg.:\n"
                                            + "\t`info entities` — displays current state of entities in the game;\n"
                                            + "\t`info command  info` — displays manual for `info` main command;\n"
                                        );
                                    } else {
                                        String subcmd = args[1];
                                        switch (subcmd){
                                            case "entities":
                                                System.out.println(
                                                        "`info entities` — displays current state of entities in the game."
                                                );
                                                break;
                                            case "command":
                                                System.out.print(
                                                        "`info command  {cmd_name}` — displays manual for the" +
                                                                " main command named {cmd_name}.\n"
                                                        + "`info command  {cmd_name} {subcmd_name}` — displays" +
                                                                " manual for the full command" +
                                                                " named {cmd_name subcmd_name}.\n"
                                                );
                                                break;
                                            default:
                                                UserCmd.informSubcmdNotRecognized(theseCmds, mainCmd, subcmd);
                                        }
                                    }
                                    break;
                                case "list":
                                    if (args.length == 1){
                                        System.out.print(
                                                "`list` command displays all instances of something. Eg.:\n"
                                                        + "\t`list entities` — displays a list of identifiers" +
                                                        " of all entities in the game;\n"
                                                        + "\t`list commands` — displays a list of" +
                                                        " all available main commands;\n"
                                                        + "\t`list subcommands  create remove info` — displays a list of" +
                                                        " subcommands for commands `create`, `remove`, `info`;\n"
                                        );
                                    } else {
                                        String subcmd = args[1];
                                        switch (subcmd){
                                            case "entities":
                                                System.out.println("`list entities` — displays a list of identifiers" +
                                                        " of all entities in the game.");
                                                break;
                                            case "commands":
                                                System.out.println("`list commands` — displays a list of" +
                                                        " all available main commands.");
                                                break;
                                            case "subcommands":
                                                System.out.println("`list subcommands  {cmd_name..}` — displays a list of" +
                                                        " subcommands for each of main command named {cmd_name}. Eg.:\n"
                                                        + "\t`list subcommands  create remove info` — displays a list of" +
                                                        " subcommands for commands `create`, `remove`, `info`."
                                                );
                                                break;
                                            default:
                                                UserCmd.informSubcmdNotRecognized(theseCmds, mainCmd, subcmd);
                                        }
                                    }
                                    break;
                                case "remove":
                                    if (args.length == 1){
                                        System.out.print(
                                                "`remove` command removes something. Eg.:\n"
                                                        + "\t`remove entities  1 8 90` — removes 3 entities" +
                                                        " from simulation with respective ids: 1, 8, 90;\n"
                                        );
                                    } else {
                                        String subcmd = args[1];
                                        switch (subcmd){
                                            case "entities":
                                                System.out.print("`remove entities  {entity_id..}` — for each" +
                                                    " {entity_id} removes entity identified by it" +
                                                    " from simulation. Eg.:\n"
                                                    + "\t`remove entities  1 8 90` — removes 3 entities" +
                                                    " from simulation with respective ids: 1, 8, 90;\n");
                                                break;
                                            default:
                                                UserCmd.informSubcmdNotRecognized(theseCmds, mainCmd, subcmd);
                                        }
                                    }
                                    break;
                                case "load":
                                    if (args.length == 1){
                                        System.out.print(
                                                "`load` command loads something. Eg.:\n"
                                                        + "\t`load cars  1 8 90` — loads 3 carriages" +
                                                        " which have ids: 1, 8, 90;\n"
                                        );
                                    } else {
                                        String subcmd = args[1];
                                        switch (subcmd){
                                            case "cars":
                                                System.out.print("`load cars  {car_id..}` — for each" +
                                                        " {car_id} loads carriage identified by it" +
                                                        " inside the simulation. They're loaded/full afterwards. Eg.:\n"
                                                        + "\t`load cars  1 8 90` — loads 3 cars" +
                                                        " which have respective ids: 1, 8, 90;\n");
                                                break;
                                            default:
                                                UserCmd.informSubcmdNotRecognized(theseCmds, mainCmd, subcmd);
                                        }
                                    }
                                    break;
                                case "create":
                                    if (args.length == 1){
                                        System.out.print(
                                                "`create` command creates something. Eg.:\n"
                                                        + "\t`create train` — creates one train with random parameters;\n"
                                        );
                                    } else {
                                        String subcmd = args[1];
                                        switch (subcmd){
                                            case "station":
                                                System.out.println("`create station` — creates one station with random parameters.");
                                                break;
                                            case "train":
                                                System.out.println("`create train` — creates one train with random parameters.");
                                                break;
                                            case "locomotive":
                                                System.out.println("`create locomotive` — creates one locomotive with random parameters.");
                                                break;
                                            case "car":
                                                System.out.println("`create car` — creates one carriage with random parameters.");
                                                break;
                                            default:
                                                UserCmd.informSubcmdNotRecognized(theseCmds, mainCmd, subcmd);
                                        }
                                    }
                                    break;
                                default:
                                    if (theseCmds.userCmds.containsKey(mainCmd)){
                                        System.out.printf("We don't have helping manual for `%s` command yet.\n", mainCmd);
                                    }else{
                                        UserCmd.informMainCmdNotRecognized(theseCmds, mainCmd);
                                    }
                            };
                        });
                    }
                });
            }
        };
    }
}
