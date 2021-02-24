package ie.lit.ardictionary.model;

import java.util.List;

public class User {
    private String name;
    private String email;
    private String uid;
    private String type;
    private List<Notebook> notebooks;

    public User(){}

    public User(String name, String email, String uid, String type){
        this.name = name;
        this.email = email;
        this.uid = uid;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
