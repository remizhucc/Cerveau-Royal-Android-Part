package helper;

import android.app.Activity;
import android.content.SharedPreferences;

import model.User;

import static android.content.Context.MODE_PRIVATE;

public class AccountHelper {
    public static User getMyInformationFromPreferences(Activity activity) {
        SharedPreferences userInformation = activity.getSharedPreferences("user", 0);
        String silent = userInformation.getString("user",null);  //second parameter default value
        return new User();
    }

    public static void setMyInformationToPreferences(Activity activity, User user) {
        SharedPreferences.Editor editor = activity.getSharedPreferences("user", MODE_PRIVATE).edit();
        editor.putString("name", user.name);
        editor.apply();
    }

    public static void getMyInformationFromServer(){

    }


    public static boolean isAuthentic(String email, String password){

    }
}
