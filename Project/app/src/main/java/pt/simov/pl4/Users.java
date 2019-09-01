package pt.simov.pl4;

import android.os.Parcel;
import android.os.Parcelable;

public class Users implements Parcelable {


    private String uid;
    private String username;


    public String getUsername() {
        return username;
    }

    public void setUsername(String title) {
        this.username = username;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.uid);
        dest.writeString(this.username);

    }

    public Users() {
    }

    protected Users(Parcel in) {
        this.uid = in.readString();
        this.username = in.readString();

    }

    public static final Parcelable.Creator<Users> CREATOR = new Parcelable.Creator<Users>() {
        @Override
        public Users createFromParcel(Parcel source) {
            return new Users(source);
        }

        @Override
        public Users[] newArray(int size) {
            return new Users[size];
        }
    };

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Users note = (Users) o;

        if (uid != null ? !uid.equals(note.uid) : note.uid != null) return false;
        if (username != null ? !username.equals(note.username) : note.username != null) return false;
        return username != null ? username.equals(note.username) : note.username == null;


    }

    @Override
    public int hashCode() {
        int result = uid != null ? uid.hashCode() : 0;
        result = 31 * result + (username != null ? username.hashCode() : 0);
        return result;
    }
}
