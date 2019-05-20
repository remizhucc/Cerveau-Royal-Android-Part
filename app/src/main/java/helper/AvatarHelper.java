package helper;

import android.app.Activity;
import android.support.v4.content.res.ResourcesCompat;

import com.cerveauroyal.R;

public class AvatarHelper {
    public static int getAvatarDrawableId(int i, Activity activity){
        String mDrawableName = "@drawable/avatar_"+String.valueOf(i);
        return activity.getResources().getIdentifier(mDrawableName , "drawable", activity.getPackageName());
    }

}
