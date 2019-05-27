package activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

import com.cerveauroyal.R;

import java.util.ArrayList;
import java.util.List;

public class FriendsActivity extends Activity {
      private List<ProfilFriends> friend = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friends);
        getFriends();
        FriendsAdapter adapter = new FriendsAdapter(FriendsActivity.this,R.layout.listfriends,friend);
        ListView listview = (ListView) findViewById(R.id.list_view);
        listview.setAdapter(adapter);
    }

    private void getFriends(){

    }
}
