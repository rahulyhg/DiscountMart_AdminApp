package sourabh.discountmartadmin.data;


import sourabh.discountmartadmin.app.AppConfig;

/**
 * Created by Downloader on 2/22/2017.
 */

public class AdSliderData {


    private Integer id;
    private String name;
    private String image;
    private Integer status;

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
        return AppConfig.IMAGES_BASE+image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

}

