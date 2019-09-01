package pt.simov.pl4;


public class Item {
    private int id;
    private String itemName;


    public Item(int id, String first) {
        this.id = id;
        this.itemName = first;
    }

    public void setId(int id) {
        this.id = id;
    }
    public int getId() {
        return id;
    }
    public void setItemName(String item) {
        this.itemName = item;
    }
    public String getItemName() {
        return itemName;
    }


}
