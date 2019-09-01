package pt.simov.pl4;

import android.support.v7.app.AppCompatActivity;

public class Friends extends AppCompatActivity {

    public String username;
    public String full_name;
    public String userID;


    public Friends(){

    }

    public Friends(String username, String full_name, String userID){
        this.username = username;
        this.full_name = full_name;
        this.userID = userID;
    }
    public String getUserID(){

        return userID;
    }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getFullName() { return full_name; }
    public void setFullName(String fullName) { this.full_name = full_name; }

}
