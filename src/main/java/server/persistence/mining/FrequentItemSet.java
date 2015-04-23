package server.persistence.mining;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.annotation.Id;

import java.util.ArrayList;

public class FrequentItemSet {

    @Id
    @JsonIgnore
    private String id;

    private ArrayList<String> items;
    private int support;

    public int getSupport() { return support; }
    public void setSupport(int support) { this.support = support; }

    public void setItems(ArrayList<String> items) { this.items = items; }
    public ArrayList<String> getItems() { return items; }

}
