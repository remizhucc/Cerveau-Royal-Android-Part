package model;

public class ProfilFriends {
    private String nickname;
    private int avatarId;
    private int id;

    public ProfilFriends(String nickname, int avatarId, int id) {
        this.nickname = nickname;
        this.avatarId = avatarId;
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public int getAvatarId() {
        return avatarId;
    }

    public int getId() {
        return id;
    }
}
