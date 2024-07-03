package io.tomneh.canvengine.examples.cl_trains.ui;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/** Interacts with user via stdin & stdout in order to parse itself from input and execute. */
public class UserCmd {
    public static final String PROMPT = "Yr cmd: ";

    public final String mainPart;
    public final String subPart;
    public final String[] args;

    protected UserCmd(String mainPart, String subPart, String[] args) {
        this.mainPart = mainPart;
        this.subPart = subPart;
        this.args = args;
    }

    public static void promptFor(){
        System.out.print(PROMPT);
    }
    public static String[] readAsTokens(Scanner input){
        Scanner scanner = new Scanner(System.in);
        String commandLine = scanner.nextLine().stripLeading();
        var tokens = commandLine.split("\\s+");

        return tokens;
    }

    /** Returns null instead of throwing so as to meet Slawek specification without messing stdout/stderr. */
    public static UserCmd tryFromTokens(String[] tokens){
        if (tokens.length < 2){
            System.out.println("Proper command must consist of at least two tokens (which are words)."
                    +" It should also be trailed by all needed arguments. E.g.:\n"
                    +"\t`list commands`,\n" +
                    "\t`list subcommands  load create`." );
            return null;
        }

        return new UserCmd(tokens[0], tokens[1], Arrays.copyOfRange(tokens, 2, tokens.length));
    }

    public static void informMainCmdNotRecognized(Cmds cmds, String mainCmdName){
        System.out.printf("\"%s\" is not a recognized command.\n", mainCmdName);
        printAllMainCmds(cmds);
    }
    public static void printAllMainCmds(Cmds cmds){
        System.out.printf("All recognized commands are: {%s}\n",
                cmds.userCmds.keySet().stream()
                        .map(s -> String.format("`%s`", s))
                        .collect(
                        Collectors.joining(", ")
                )
        );
    }

    public static void informSubcmdNotRecognized(Cmds cmds, String mainCmdName, String subcmdName){
        System.out.printf("\"%s\" is not a recognized subcommand of `%s`.\n",
                subcmdName, mainCmdName);
        UserCmd.printAllSubcmds(cmds, mainCmdName);
    }

    public static void attemptPrintAllSubcmds(Cmds cmds, String mainCmdName){
        if (cmds.userCmds.containsKey(mainCmdName)){
            printAllSubcmds(cmds, mainCmdName);
        }else {
            System.out.printf("Main command `%s` doesn't exist.\n", mainCmdName);
            System.out.println("Use `list commands` to print all available main commands.");
        }
    }
    public static void printAllSubcmds(Cmds cmds, String mainCmdName){
        printAllSubcmds(mainCmdName, cmds.userCmds.get(mainCmdName));
    }
    public static void printAllSubcmds(String mainCmdName, HashMap<String, Consumer<String[]>> subcmds){
        System.out.printf("All recognized subcommands of `%s` are: {%s}\n",
                mainCmdName,
                subcmds
                        .keySet().stream()
                        .map(s -> String.format("`%s`", s))
                        .collect( Collectors.joining(", ") )
        );
    }

    /** Returns null instead of throwing so as to meet Slawek specification without messing stdout/stderr. */
    private static Map<String, Consumer<String[]>> tryGetSubprocedures(Cmds cmds, String mainCmdName){
        HashMap<String, Consumer<String[]>> subprocedures = cmds.userCmds.get(mainCmdName);
        if (subprocedures == null){
            informMainCmdNotRecognized(cmds, mainCmdName);
            return null;
        }
        return subprocedures;
    }

    /** Returns null instead of throwing so as to meet Slawek specification
     *  without messing stdout/stderr. */
    private Consumer<String[]> tryGetCorrespondingProcedure(Cmds cmds){
        var subprocedures = tryGetSubprocedures(cmds, this.mainPart);
        if (subprocedures==null){
            return null;
        }
        var correspondingProcedure= subprocedures.get(this.subPart);
        if (correspondingProcedure == null){
            System.out.printf("\"%s\" is not a recognized subcommand of `%s`.\n",
                    this.subPart, this.mainPart);
            printAllSubcmds(cmds, this.mainPart);
            return null;
        }
        return correspondingProcedure;
    }

    public void attemptExec(Cmds cmds){
        var procedure= this.tryGetCorrespondingProcedure(cmds);
        if (procedure != null){
            procedure.accept(this.args);
        }
    }
}
