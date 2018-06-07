package zpi.pls.zpidominator2000.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by wojciech.liebert on 31.05.2018.
 */

public class OutsideTemp {

    @SerializedName("temperature")
    @Expose
    private Double temperature;

    public Double getTemperature() {
        return temperature;
    }

    public void setHouseTemperature(Double temperature) {
        this.temperature = temperature;
    }

}