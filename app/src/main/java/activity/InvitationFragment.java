package activity;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.cerveauroyal.R;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import helper.AccountHelper;
import helper.ActivityHelper;
import helper.AvatarHelper;
import helper.RankHelper;
import helper.RequestHelper;
import model.Constant;
import model.User;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class InvitationFragment extends Fragment implements View.OnClickListener {
    User challenger;
    String matchId;
    int subject;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.invitation, container, false);
        //prepare date
        String userJson = getArguments().getString("user");
        matchId = getArguments().getString("matchId");
        subject = getArguments().getInt("subject");
        challenger = User.read(userJson);
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

        avatarImage.setImageResource(AvatarHelper.getAvatarDrawableId(challenger.getAvatar(), getActivity()));
        nickname.setText(challenger.getnickname());
        rankImage.setImageResource(RankHelper.getRankDrawableId(challenger.getRank(), getActivity()));
        rankName.setText(RankHelper.getRankName(challenger.getRank()));

        //set subject
        TextView subjectText = (TextView) v.findViewById(R.id.subjectInvitation);
        subjectText.setText(getSubjectName(subject));

        return v;
    }

    private void accept(View view) {
        RequestHelper.httpGetRequest("http://cerveauroyal-env.tdsz9xheaw.eu-west-3.elasticbeanstalk.com/acceptInvitation",
                buildRespondInvitationJsonString(true),
                new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {

                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        String responseString = response.body().string();
                        Intent intent = new Intent(getActivity(), MatchActivity.class);
                        intent.putExtra("json", responseString);
                        startActivity(intent);
                    }
                });
    }

    private void escape(View view) {
        RequestHelper.httpGetRequest("http://cerveauroyal-env.tdsz9xheaw.eu-west-3.elasticbeanstalk.com/acceptInvitation",
                buildRespondInvitationJsonString(false),
                new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {

                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {

                    }
                } );
    }


    private String buildRespondInvitationJsonString(Boolean success) {
        JSONObject json = new JSONObject();
        try {
            json.put("matchId", matchId);
            json.put("success", success);
        } catch (JSONException e) {
            System.out.println(e.getStackTrace());
        }
        return json.toString();
    }

    private String getSubjectName(int subject){
        switch (subject){
            case 1:
                return "Geography";
            case 2:
                return "Literature";
            case 3:
                return "Math";
            case 4:
                return "History";
            case 5:
                return "Art";
            case 6:
                return "Music";
            case 7:
                return "English";
            case 8:
                return "CommonSense";
        }
        return null;
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
