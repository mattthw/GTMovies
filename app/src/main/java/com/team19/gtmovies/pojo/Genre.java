package com.team19.gtmovies.pojo;

/**
 * Created by Jim Jang on 2016-02-17.
 */
public enum Genre {
    ACT_ADV, ANIM, ART, CLASSIC, COMD, DOCU, DRAMA, HORROR, KIDS_FAM, MUSIC, MYST,
    ROMANCE, SCIFI, SPECIAL, SPORTS, TV, WEST;

    @Override
    public String toString() {
        if (this == ACT_ADV) return "Action & Adventure";
        if (this == ANIM) return "Animation";
        if (this == ART) return "Art House & International";
        if (this == CLASSIC) return "Classics";
        if (this == COMD) return "Comedy";
        if (this == DOCU) return "Documentary";
        if (this == DRAMA) return "Drama";
        if (this == HORROR) return "Horror";
        if (this == KIDS_FAM) return "Kids & Family";
        if (this == MUSIC) return "Musical & Performing Arts";
        if (this == MYST) return "Mystery & Suspense";
        if (this == ROMANCE) return "Romance";
        if (this == SCIFI) return "Science Fiction & Fantasy";
        if (this == SPECIAL) return "Special Interest";
        if (this == SPORTS) return "Sports & Fitness";
        if (this == TV) return "Television";
        if (this == WEST) return "Western";
        return "Hail Jinu";
    }

    /**
     * Reads the given String and converts it into a Genre
     * Will return null if the String is not an existing Genre
     *
     * @param str the String to read
     * @return the appropriate Genre, null if non-existing Genre
     */
    public static Genre toGenre(String str) {
        if (str.equals("Action & Adventure")) return ACT_ADV;
        if (str.equals("Animation")) return ANIM;
        if (str.equals("Art House & International")) return ART;
        if (str.equals("Classics")) return CLASSIC;
        if (str.equals("Comedy")) return COMD;
        if (str.equals("Documentary")) return DOCU;
        if (str.equals("Drama")) return DRAMA;
        if (str.equals("Horror")) return HORROR;
        if (str.equals("Kids & Family")) return KIDS_FAM;
        if (str.equals("Musical & Performing Arts")) return MUSIC;
        if (str.equals("Mystery & Suspense")) return MYST;
        if (str.equals("Romance")) return ROMANCE;
        if (str.equals("Science Fiction & Fantasy")) return SCIFI;
        if (str.equals("Special Interest")) return SPECIAL;
        if (str.equals("Sports & Fitness")) return SPORTS;
        if (str.equals("Television")) return TV;
        if (str.equals("Western")) return WEST;
        return null;
    }
}
