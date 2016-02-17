package com.team19.gtmovies;

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
}
