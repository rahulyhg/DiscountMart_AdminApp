package sourabh.discountmartadmin.data;

import java.util.List;

/**
 * Created by Sourabh on 3/21/2017.
 */

public class CityData {
    private Integer id;
    private String parent_id;
    private String city_name;
    private Integer status;
    private String created_on;

    private List<CityData> areas = null;


    public List<CityData> getAreas() {
        return areas;
    }

    public void setAreas(List<CityData> areas) {
        this.areas = areas;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getParent_id() {
        return parent_id;
    }

    public void setParent_id(String parent_id) {
        this.parent_id = parent_id;
    }

    public String getCity_name() {
        return city_name;
    }

    public void setCity_name(String city_name) {
        this.city_name = city_name;
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
}
