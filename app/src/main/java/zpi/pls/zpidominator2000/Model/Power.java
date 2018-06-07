package zpi.pls.zpidominator2000.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by wojciech.liebert on 31.05.2018.
 */

public class Power {

    @SerializedName("roomId")
    @Expose
    private Integer roomId;
    @SerializedName("lightPowerHistory")
    @Expose
    private List<Double> lightPowerHistory = null;
    @SerializedName("climatPowerHistory")
    @Expose
    private List<Double> climatPowerHistory = null;

    public Integer getRoomId() {
        return roomId;
    }

    public void setRoomId(Integer roomId) {
        this.roomId = roomId;
    }

    public List<Double> getLightPowerHistory() {
        return lightPowerHistory;
    }

    public void setLightPowerHistory(List<Double> lightPowerHistory) {
        this.lightPowerHistory = lightPowerHistory;
    }

    public List<Double> getClimatPowerHistory() {
        return climatPowerHistory;
    }

    public void setClimatPowerHistory(List<Double> climatPowerHistory) {
        this.climatPowerHistory = climatPowerHistory;
    }

}