package pt.simov.pl4;

public class FriendsItems {

    public String title;
    public String description;


    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public FriendsItems(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public FriendsItems(){

    }

}
