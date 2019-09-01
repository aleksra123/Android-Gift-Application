package pt.simov.pl4;


public class Person{
    private int id;
    private String firstName;
    private String lastName;

    public Person(int id, String first,String last) {
        this.id = id;
        this.firstName = first;
        this.lastName = last;
    }

    public void setId(int id) {
        this.id = id;
    }
    public int getId() {
        return id;
    }
    public void setFirstName(String name) {
        this.firstName = name;
    }
    public String getFirstName() {
        return firstName;
    }
    public void setLastName(String name) {
        this.lastName = name;
    }
    public String getLastName() {
        return lastName;
    }

}
