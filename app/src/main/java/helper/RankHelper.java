package helper;

import android.app.Activity;

public class RankHelper {


    public static int getRankDrawableId(String rank, Activity activity) {
        String mDrawableName = "@drawable/rank" + rank;
        return activity.getResources().getIdentifier(mDrawableName, "drawable", activity.getPackageName());
    }

    public static String getRankName(String rank) {
        switch (rank) {
            case "pawn":
                return "Pawn";
            case "knight":
                return "Knight";
            case "bishop":
                return "Bishop";
            case "tower":
                return "Tower";
            case "queen":
                return "Queen";
            default:
                return "NULL";
        }
    }
}
