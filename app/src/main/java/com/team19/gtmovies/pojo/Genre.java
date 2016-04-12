package com.team19.gtmovies.pojo;

/**
 * Created by Jim Jang on 2016-02-17.
 */
public enum Genre {
    ACT_ADV, ANIM, ART, CLASSIC, COMD, DOCU, DRAMA, HORROR, KIDS_FAM, MUSIC, MYST,
    ROMANCE, SCIFI, SPECIAL, SPORTS, TV, WEST;

    @Override
    public String toString() {
        switch (this) {
            case ACT_ADV:
                return "Action & Adventure";
            case ANIM:
                return "Animation";
            case CLASSIC:
                return "Classics";
            case COMD:
                return "Comedy";
            case DOCU:
                return "Documentary";
            case DRAMA:
                return "Drama";
            case HORROR:
                return "Horror";
            case KIDS_FAM:
                return "Kids & Family";
            case MUSIC:
                return "Musical & Performing Arts";
            case MYST:
                return "Mystery & Suspense";
            case ROMANCE:
                return "Romance";
            case SCIFI:
                return "Science Fiction & Fantasy";
            case SPECIAL:
                return "Special Interest";
            case SPORTS:
                return "Sports & Fitness";
            case TV:
                return "Television";
            case WEST:
                return "Western";
            default:
                return "Hail Jinu";
        }
    }

    /**
     * Reads the given String and converts it into a Genre
     * Will return null if the String is not an existing Genre
     *
     * @param str the String to read
     * @return the appropriate Genre, null if non-existing Genre
     */
    public static Genre toGenre(String str) {
        if (str.equals("Action & Adventure")) {
            return ACT_ADV;
        }
        if (str.equals("Animation")) {
            return ANIM;
        }
        if (str.equals("Art House & International")) {
            return ART;
        }
        if (str.equals("Classics")) {
            return CLASSIC;
        }
        if (str.equals("Comedy")) {
            return COMD;
        }
        if (str.equals("Documentary")) {
            return DOCU;
        }
        if (str.equals("Drama")) {
            return DRAMA;
        }
        if (str.equals("Horror")) {
            return HORROR;
        }
        if (str.equals("Kids & Family")) {
            return KIDS_FAM;
        }
        if (str.equals("Musical & Performing Arts")) {
            return MUSIC;
        }
        if (str.equals("Mystery & Suspense")) {
            return MYST;
        }
        if (str.equals("Romance")) {
            return ROMANCE;
        }
        if (str.equals("Science Fiction & Fantasy")) {
            return SCIFI;
        }
        if (str.equals("Special Interest")) {
            return SPECIAL;
        }
        if (str.equals("Sports & Fitness")) {
            return SPORTS;
        }
        if (str.equals("Television")) {
            return TV;
        }
        if (str.equals("Western")) {
            return WEST;
        }
        return null;
    }
}
