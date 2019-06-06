package broadcastReceiver;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.RelativeLayout;

import com.cerveauroyal.R;

import activity.InvitationFragment;
import helper.InvitationHelper;

public class ReceiveInvitationBroadcastReceiver extends BroadcastReceiver {
    Activity currentActivity;

    public ReceiveInvitationBroadcastReceiver() {
        super();
    }
    public ReceiveInvitationBroadcastReceiver(Activity currentActivity) {
        super();
        this.currentActivity = currentActivity;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        RelativeLayout root = (RelativeLayout) currentActivity.findViewById(R.id.root);
        FragmentManager fragmentManager = currentActivity.getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        InvitationFragment fragment = new InvitationFragment();
        InvitationHelper.sendData(fragment,intent.getStringExtra("json"));
        fragmentTransaction.add(root.getId(), fragment);
        fragmentTransaction.commit();

    }


}
