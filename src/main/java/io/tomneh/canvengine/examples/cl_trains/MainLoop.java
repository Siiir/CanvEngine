package io.tomneh.canvengine.examples.cl_trains;

import io.tomneh.canvengine.examples.cl_trains.railroad.RailroadMgr;
import io.tomneh.canvengine.examples.cl_trains.railroad.RailroadScene;
import io.tomneh.canvengine.examples.cl_trains.ui.Cmds;
import io.tomneh.canvengine.examples.cl_trains.ui.UserCmd;
import io.tomneh.canvengine.exec.cmd.Command;
import io.tomneh.canvengine.util.SyncChannel;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.locks.ReentrantLock;

public class MainLoop {
    public static void main(String[] _args) {
        Cmds cmds;
        SyncChannel<Runnable> incomingInstr= new SyncChannel<>();
        ArrayList<Runnable> incomingInstrBuf= new ArrayList<>();
        {
            SyncChannel<Command> commandChannel = new SyncChannel<>();
            var stdoutLock= new ReentrantLock();
            new Thread(() -> {
                var sceneMgr= new RailroadMgr(new RailroadScene(), commandChannel, incomingInstr, stdoutLock);
                for (;;){
                    sceneMgr.readThanExecCmds();
                    sceneMgr.updateScene();
                }
            }).start();

            cmds= new Cmds(commandChannel, stdoutLock);
        }

        System.out.printf(
                "To impact or get insight into the railroad simulation enter a command"
                + " on the right of the prompt “%s“.\n"
                + "Tip: enter `list commands` to list all available commands.“\n",
                UserCmd.PROMPT
        );
        Scanner scanner = new Scanner(System.in);

        for (;;) {
            incomingInstr.takeAll(incomingInstrBuf);
            for (var instr : incomingInstrBuf) {
                instr.run();
            }

            try{
                cmds.stdoutLock.lock();

                UserCmd.promptFor();
                var tokens= UserCmd.readAsTokens(scanner);
                var userCmd= UserCmd.tryFromTokens(tokens);
                if (userCmd != null){
                    userCmd.attemptExec(cmds);
                }
            }finally {
                cmds.stdoutLock.unlock();
            }
        }
    }
}
