package zpi.pls.zpidominator2000.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by wojciech.liebert on 31.05.2018.
 */

public class HouseTemp {

    @SerializedName("houseTemperature")
    @Expose
    private Double houseTemperature;

    public Double getHouseTemperature() {
        return houseTemperature;
    }

    public void setHouseTemperature(Double houseTemperature) {
        this.houseTemperature = houseTemperature;
    }

}