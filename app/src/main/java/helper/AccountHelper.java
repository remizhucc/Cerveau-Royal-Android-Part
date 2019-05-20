package helper;

import android.app.Activity;
import android.content.SharedPreferences;

import model.User;

import static android.content.Context.MODE_PRIVATE;

public class AccountHelper {

    //preferences
    public static String getMyEmailFromPreferences(Activity activity) {
        SharedPreferences userInformation = activity.getSharedPreferences("user", 0);
        String email = userInformation.getString("email", null);  //second parameter default value
        return email;
    }
    public static String getMyNicknameFromPreferences(Activity activity) {
        SharedPreferences userInformation = activity.getSharedPreferences("user", 0);
        String nickname = userInformation.getString("nickname", null);  //second parameter default value
        return nickname;
    }

    public static int getMyAvatarFromPreferences(Activity activity) {
        SharedPreferences userInformation = activity.getSharedPreferences("user", 0);
        int avatar = userInformation.getInt("avatar", 0);  //second parameter default value
        return avatar;
    }

    public static String getMyRankFromPreferences(Activity activity) {
        SharedPreferences userInformation = activity.getSharedPreferences("user", 0);
        String rank = userInformation.getString("rank", null);  //second parameter default value
        return rank;
    }


    //login page

    public static void setMyInformationFromServer(String email) {

    }


    public static boolean isAuthentic(String email, String password) {

    }

    //signup page

    public static boolean isPasswordAndComfirmPasswordMatch(String password, String password2) {
        return password.equals(password2);
    }

    public static boolean isSignUpInServerSuccess(String email, String password, String nickname, int avatar) {

    }
}
