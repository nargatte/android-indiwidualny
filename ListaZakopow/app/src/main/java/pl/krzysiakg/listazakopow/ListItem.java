package pl.krzysiakg.listazakopow;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class ListItem {
    @PrimaryKey(autoGenerate = true)
    private int id;

    public ListItem(String name) {
        this.name = name;
    }

    private String name;

    public String getName() {
        return name;
    }

    public ListItem() {
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
