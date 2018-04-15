package com.example.daniel.values;

/**
 * Created by Daniel on 2017-04-27.
 */

public class AddTrainingValues {
    final public static int OPEN_FROM_MAIN_MENU =1;
    final public static int OPEN_FROM_SCHEDULE =2;
    final public static int OPEN_FROM_PROGRESS =3;
    static public boolean DROP_SET=true;
    static private boolean isDropSet;
    public static boolean isDropSet() {
        return isDropSet;
    }
    public static void setDropSet(Boolean bool) {
        isDropSet=bool;
    }
}
