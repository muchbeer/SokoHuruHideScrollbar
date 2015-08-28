package sokohuru.muchbeer.king.sokohuruhidescrollbar.activities.settings;

import android.os.Parcel;
import android.os.Parcelable;


/**
 * Created by muchbeer on 6/4/2015.
 */
public class Soko implements Parcelable{

    private int id;
    private String title;
    private String image;
    private String postdate;
    private String place;
    private String jimbo;

    //Sokoni
    private String name;
    private String description;
    private String desc;
    private String moto;
    private String mkoa;
    private String username;
    private String pid;
    private String created;


    public Soko() {

    }

    public Soko(Parcel input) {
        id =input.readInt();
        title=input.readString();
        image=input.readString();
        place=input.readString();
        jimbo=input.readString();

        name= input.readString();
        description=input.readString();
        desc=input.readString();
        moto=input.readString();
        mkoa=input.readString();
        username=input.readString();
        created = input.readString();
    }

    public Soko(int id,
                String title,
                String image,
                String postdate,
                String place,
                String jimbo,

                String name,
                String description,
                String desc,
                String moto,
                String mkoa,
                String username,
                String created) {

        this.id = id;
        this.title = title;
        this.image = image;
        this.postdate = postdate;
        this.place = place;
        this.jimbo=jimbo;

        this.name = name;
        this.description=description;
        this.desc=description;
        this.moto=moto;
        this.mkoa=mkoa;
        this.username=username;
        this.created=created;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getMoto() {
        return moto;
    }

    public void setMoto(String moto) {
        this.moto = moto;
    }

    public String getMkoa() {
        return mkoa;
    }

    public void setMkoa(String mkoa) {
        this.mkoa = mkoa;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }
    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image= image;
    }




    public String getPostdate() {
        return postdate;
    }

    public void setPostdate(String postdate) {
        this.postdate = postdate;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getJimbo() {
        return jimbo;
    }

    public void setJimbo(String jimbo) {
        this.jimbo = jimbo;
    }

    @Override
    public String toString() {
        return
                "ID: " +id+
                        "Title: "+title+
                        "Image: " + image+
                        "Created on: "+ postdate+
                        "Place: " + place+
                        "username:" +username;
    }

    @Override
    public int describeContents() {
        L.m("Describe Contents  Movie");
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        L.m("writeToParcel Movie");
        parcel.writeInt(id);
        parcel.writeString(title);
        parcel.writeString(image);
        parcel.writeString(jimbo);
        parcel.writeString(place);
        parcel.writeString(username);
        parcel.writeString(postdate);
        parcel.writeString(description);
    }

    public static final Creator<Soko> CREATOR
            = new Creator<Soko>() {
        public Soko createFromParcel(Parcel in) {
            L.m("create from parcel : Movie");
            return new Soko(in);
        }

        public Soko[] newArray(int size) {
            return new Soko[size];
        }
    };

}