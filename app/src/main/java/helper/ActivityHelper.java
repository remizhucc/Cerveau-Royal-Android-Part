package helper;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import com.cerveauroyal.R;

public class ActivityHelper {
    //check if we touch the place without keyboard
    public static boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] leftTop = { 0, 0 };
            v.getLocationInWindow(leftTop);
            int left = leftTop[0], top = leftTop[1], bottom = top + v.getHeight(), right = left
                    + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 保留点击EditText的事件
                return false;
            } else {
                return true;
            }
        }
        return false;
    }

    //hide the keyboard
    public static Boolean hideInputMethod(Context context, View v) {
        InputMethodManager imm = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            return imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
        return false;
    }

    public static class BlueButtonListener implements View.OnTouchListener {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if(event.getAction() == MotionEvent.ACTION_UP){
                v.setBackgroundResource(R.drawable.button_green);
            }
            if(event.getAction() == MotionEvent.ACTION_DOWN){
                v.setBackgroundResource(R.drawable.button_dark_grey);
            }
            return false;
        }
    }

    public static class GreyButtonListener implements View.OnTouchListener {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if(event.getAction() == MotionEvent.ACTION_UP){
                v.setBackgroundResource(R.drawable.button_grey);
            }
            if(event.getAction() == MotionEvent.ACTION_DOWN){
                v.setBackgroundResource(R.drawable.button_dark_grey);
            }
            return false;
        }
    }
}
