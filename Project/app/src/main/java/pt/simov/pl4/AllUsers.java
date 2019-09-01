package pt.simov.pl4;

public class AllUsers {
    public String username;
    public String status;
    public String full_name;

    public AllUsers(){

    }

    public AllUsers(String username, String status, String full_name) {
        this.username = username;
        this.status = status;
        this.full_name = full_name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }



}
