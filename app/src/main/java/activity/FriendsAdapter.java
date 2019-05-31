package activity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cerveauroyal.R;

import java.util.List;

import model.ProfilFriends;

public class FriendsAdapter extends ArrayAdapter<ProfilFriends> {
    private int resourceId;
    public FriendsAdapter(Context context, int resource, List<ProfilFriends> objects) {
        super(context, resource, objects);
        resourceId=resource;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ProfilFriends friend = getItem(position);
        View view= LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
        ImageView avatarImage=(ImageView)view.findViewById(R.id.avatar_player2);
        TextView friendName=(TextView) view.findViewById(R.id.nickname);
        avatarImage.setImageResource(friend.getAvatarId());
        friendName.setText(friend.getNickname());
        return view;
    }
}
