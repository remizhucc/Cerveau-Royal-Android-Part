package activity;
//TODO put this file somewhere else
public class ProfilFriends {
    private String nickname;
    private int avatarId;
    public ProfilFriends(String nickname, int avatarId){
        this.nickname=nickname;
        this.avatarId=avatarId;
    }

    public String getNickname(){
        return nickname;
    }

    public int getAvatarId(){
        return avatarId;
    }
}
