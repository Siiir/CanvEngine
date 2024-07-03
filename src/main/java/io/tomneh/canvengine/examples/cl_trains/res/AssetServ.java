package io.tomneh.canvengine.examples.cl_trains.res;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class AssetServ {
    public static final String DIR_PATH = "./assets/data/names/";
    public static final String FULL_NAMES_PATH = DIR_PATH + "full_names.txt";

    static {
        try {
            FULL_NAMES = Files.readAllLines(Paths.get(FULL_NAMES_PATH));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /** Mem. consumption < 500 KB . */
    private static final List<String> FULL_NAMES;
    private static int fullNamesGetIdx= 0;

    public static String getArbFullName(){
        if (fullNamesGetIdx == FULL_NAMES.size()){
            // Start from scratch.
            // Will rarely happen for large list of full names.
            fullNamesGetIdx= 0;
        }
        return FULL_NAMES.get(fullNamesGetIdx++);
    }
}
