package activity;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cerveauroyal.R;

import java.util.List;

import helper.AvatarHelper;
import model.ProfilFriends;

public class FriendsAdapter extends ArrayAdapter<ProfilFriends> {
    private int resourceId;

    public FriendsAdapter(Context context, int resource, List<ProfilFriends> objects) {
        super(context, resource, objects);
        resourceId = resource;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        final ProfilFriends friend = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
        ImageView avatarImage = (ImageView) view.findViewById(R.id.avatar);
        TextView friendName = (TextView) view.findViewById(R.id.nickname);
        avatarImage.setImageResource(getContext().getResources().getIdentifier("@drawable/avatar_" + String.valueOf(friend.getAvatarId()), "drawable", getContext().getPackageName()));
        friendName.setText(friend.getNickname());

        ImageView add=(ImageView) view.findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), StartGameActivity.class);
                intent.putExtra("withUser", true);
                intent.putExtra("userId", friend.getId());
                intent.putExtra("avatar_player2", friend.getAvatarId());
                intent.putExtra("nickname_player2", friend.getNickname());

                getContext().startActivity(intent);
            }

        });
        return view;
    }
}
