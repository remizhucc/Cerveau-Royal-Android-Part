package helper;

import android.app.Activity;

import model.Constant;

public class RankHelper {


    public static int getRankDrawableId(Constant.RANK rank, Activity activity) {
        String mDrawableName = "@drawable/rank" + rank.toString();
        return activity.getResources().getIdentifier(mDrawableName, "drawable", activity.getPackageName());
    }

    public static String getRankName(Constant.RANK rank) {
        switch (rank.toString()) {
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
