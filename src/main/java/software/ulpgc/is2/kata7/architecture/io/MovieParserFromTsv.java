package software.ulpgc.is2.kata7.architecture.io;

import software.ulpgc.is2.kata7.architecture.model.Movie;

public class MovieParserFromTsv {
    public static Movie fromTsv(String str) {
        return fromTsv(str.split("\t"));
    }

    private static Movie fromTsv(String[] split) {
        return new Movie(split[2], toIntTsv(split[5]), toIntTsv(split[7]));
    }

    private static int toIntTsv(String str) {
        // Si está vacío (es null) retorna -1
        if (str.equals("\\N")) return -1;
        return Integer.parseInt(str);
    }
}
