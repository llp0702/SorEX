package su.sorex.com.sorex_front_app.Models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import su.sorex.com.sorex_front_app.MyExceptions.IdNotFound;
import su.sorex.com.sorex_front_app.Utils.PrefManager;

public class Item{
    public static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    private String name;
    private int price;
    private Date dateRetour;
    private String owner;
    private String desc;
    private int id;
    private String type;
    public static ArrayList<Item> items = new ArrayList<Item>();

    public static ArrayList<Item> myItems = new ArrayList<Item>();

    public Item(int id, String owner, String name, String desc, int price, String type, Date dateRetour){
        setId(id);
        setDesc(desc);
        setOwner(owner);
        setName(name);
        setPrice(price);
        setType(type);
        setDateRetour(dateRetour);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public static ArrayList<Item> getTemplateList() {
        return items;
    }

    public Date getDateRetour() {
        return dateRetour;
    }

    public void setDateRetour(Date dateRetour) {
        this.dateRetour = dateRetour;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }


    public static List<Item> getItemsFromString(String s){
        List<Item> returnedItems = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(s);
            for(int i = 0; i < jsonArray.length(); i++) {
                JSONObject o = jsonArray.getJSONObject(i);
                int id = o.getInt("idItem");
                String prop = PrefManager.getInstance().getId();
                String title = o.getString("title");
                String desc = o.getString("description");
                int price = o.getInt("prixParJour");
                String type = o.getJSONObject("type").getString("nom");
                Date ret = formatDateFromString(o.getString("dateDeRetour"));
                Item tmp = new Item(id, prop, title, desc, price, type, ret);
                returnedItems.add(tmp);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IdNotFound idNotFound) {
            idNotFound.printStackTrace();
        }
        return returnedItems;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public static Date formatDateFromString(String s) {
        if(s==null)return null;
        try {
            return DATE_FORMAT.parse(s);
        } catch (ParseException e) {

        }
        return null;

    }
}
