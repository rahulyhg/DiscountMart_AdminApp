package sourabh.discountmartadmin.data;

import java.util.ArrayList;

/**
 * Created by Sourabh on 3/17/2017.
 */

public class GenericCategoryData {

    private Integer id;
    private String name;
    private String image;
    private Integer position;
    private Integer status;
    private String created_on;
    private Integer parent_id;



    private ArrayList<GenericCategoryData> subcategories = null;

    public ArrayList<GenericCategoryData> getSubcategories() {
        return subcategories;
    }

    public void setSubcategories(ArrayList<GenericCategoryData> subcategories) {
        this.subcategories = subcategories;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getCreated_on() {
        return created_on;
    }

    public void setCreated_on(String created_on) {
        this.created_on = created_on;
    }

    public Integer getParent_id() {
        return parent_id;
    }

    public void setParent_id(Integer parent_id) {
        this.parent_id = parent_id;
    }
}
