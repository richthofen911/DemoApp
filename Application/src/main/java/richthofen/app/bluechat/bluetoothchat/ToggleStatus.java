package richthofen.app.bluechat.bluetoothchat;
/**
 * Created by richthofen80 on 2/26/15.
 */
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;


@DatabaseTable(tableName = "toggleStatus")
public class ToggleStatus {
    public static final String ID = "id";
    public static final String NAME = "name";
    public static final String STATUS = "status";

    @DatabaseField(generatedId=true, useGetSet=true, columnName=ID)
    private int id;
    @DatabaseField(useGetSet=true, columnName=NAME)
    private String name;
    @DatabaseField(useGetSet=true, columnName=STATUS)
    private String status;

    public ToggleStatus(){

    }

    public int getId(){return this.id;}
    public void setId(int id){this.id = id;}
    public String getName(){return this.name;}
    public void setName(String name){this.name = name;}
    public String getStatus(){return this.status;}
    public void setStatus(String status){this.status = status;}
    public String toString(){ return this.name + this.getStatus(); }

}
