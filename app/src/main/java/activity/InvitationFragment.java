package activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.cerveauroyal.R;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import helper.AccountHelper;
import helper.ActivityHelper;
import helper.AvatarHelper;
import helper.RankHelper;
import model.Constant;
import model.User;
import okhttp3.Call;

public class InvitationFragment extends Fragment implements View.OnClickListener {
    User challenger;
    String agentName;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.invitation, container, false);
        //prepare date
        String userJson = getArguments().getString("user");
        agentName = getArguments().getString("agentName");
        challenger=User.read(userJson);
        //set onclick function
        Button accept = (Button) v.findViewById(R.id.button_accept);
        accept.setOnTouchListener(new ActivityHelper.GreenButtonListener());
        accept.setOnClickListener(this); // calling onClick() method
        Button escape = (Button) v.findViewById(R.id.button_escape);
        escape.setOnClickListener(this);
        escape.setOnTouchListener(new ActivityHelper.RedButtonListener());

        //set nickname avatar...
        ImageView avatarImage = (ImageView) v.findViewById(R.id.avatarImageInvitation);
        TextView nickname = (TextView) v.findViewById(R.id.nicknameInvatation);
        ImageView rankImage = (ImageView) v.findViewById(R.id.rankImageInvitation);
        TextView rankName = (TextView) v.findViewById(R.id.rankNameInvitation);

        avatarImage.setImageResource(AvatarHelper.getAvatarDrawableId(challenger.getAvatar(),getActivity()));
        nickname.setText(challenger.getnickname());
        rankImage.setImageResource(RankHelper.getRankDrawableId(challenger.getRank(),getActivity()));
        rankName.setText(RankHelper.getRankName(challenger.getRank()));
        return v;
    }

    private void accept(View view){
        try {
            OkHttpUtils.get()
                    .url("http://cerveauroyal-env.tdsz9xheaw.eu-west-3.elasticbeanstalk.com/invitation")
                    .addParams("JSON", URLEncoder.encode(buildRespondInvitationJsonString(true), "utf-8"))
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e) {

                        }

                        @Override
                        public void onResponse(String response) {
                            Intent intent = new Intent(getActivity(), MatchActivity.class);
                            intent.putExtra("json", response);
                            startActivity(intent);
                        }
                    });
        } catch (UnsupportedEncodingException e) {
            System.out.println(e.getStackTrace());
        }
    }
    private void escape(View view){
        try {
            OkHttpUtils.get()
                    .url("http://cerveauroyal-env.tdsz9xheaw.eu-west-3.elasticbeanstalk.com/invitation")
                    .addParams("JSON", URLEncoder.encode(buildRespondInvitationJsonString(false), "utf-8"))
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e) {

                        }

                        @Override
                        public void onResponse(String response) {

                        }
                    });
        } catch (UnsupportedEncodingException e) {
            System.out.println(e.getStackTrace());
        }
    }


    private String buildRespondInvitationJsonString(Boolean success){
        JSONObject json = new JSONObject();
        try {
            json.put("agentName", agentName);
            json.put("success", success);
        } catch (JSONException e) {
            System.out.println(e.getStackTrace());
        }
        return json.toString();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.button_accept:
                accept(v);
                break;

            case R.id.button_escape:
                escape(v);
                break;

            default:
                break;
        }

    }
}
//TODO put invitation pop up center