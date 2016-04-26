package com.team19.gtmovies.pojo;

/**
 * Created by Austin Leal on 2016-04-25.
 *
 * @author Austin Leal
 * @version 1.0
 */
public enum Rating {
    G, PG, PG_13, R, UNRATED, NC_17;

    @Override
    public String toString() {
        switch (this) {
            case G:
                return "G";
            case PG:
                return "PG";
            case PG_13:
                return "PG-13";
            case R:
                return "R";
            case UNRATED:
                return "Unrated";
            case NC_17:
                return "NC-17"; //Don't watch these kids
            default:
                return "Bow to Austin";
        }
    }

    /**
     * Reads the given String and converts it into a Rating
     * Will return null if the String is not an existing Rating
     *
     * @param str the String to read
     * @return the appropriate Rating, null if non-existing Rating
     */
    public static Rating toRating(String str) {
        if (str.equals("G")) {
            return G;
        }
        if (str.equals("PG")) {
            return PG;
        }
        if (str.equals("PG-13")) {
            return PG_13;
        }
        if (str.equals("R")) {
            return R;
        }
        if (str.equals("Unrated")) {
            return UNRATED;
        }
        if (str.equals("NC-17")) {
            return NC_17;   //Don't watch these kids
        }
        return null;
    }
}
