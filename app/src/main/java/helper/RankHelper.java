package helper;

import android.app.Activity;

import model.Constant;

public class RankHelper {


    public static int getRankDrawableId(Constant.RANK rank, Activity activity) {
        String mDrawableName = "@drawable/rank_" + rank.toString().toLowerCase();
        return activity.getResources().getIdentifier(mDrawableName, "drawable", activity.getPackageName());
    }

    public static String getRankName(Constant.RANK rank) {
        switch (rank) {
            case PAWN:
                return "Pawn";
            case KNIGHT:
                return "Knight";
            case BISHOP:
                return "Bishop";
            case TOWER:
                return "Tower";
            case QUEEN:
                return "Queen";
            default:
                return "NULL";
        }
    }

    public static String getRankName(String rank){
        Constant.RANK ConstantRank = Constant.RANK.valueOf(rank);
        switch (ConstantRank) {
            case PAWN:
                return "Pawn";
            case KNIGHT:
                return "Knight";
            case BISHOP:
                return "Bishop";
            case TOWER:
                return "Tower";
            case QUEEN:
                return "Queen";
            default:
                return "NULL";
        }
    }
}
