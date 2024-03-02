package main.util;

public class IdGenerate {
    private static int id = 0;
    public static int generationNewId() {
        return id++;
    }

}
