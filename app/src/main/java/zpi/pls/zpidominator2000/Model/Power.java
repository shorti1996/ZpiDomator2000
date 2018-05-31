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
    private List<Integer> lightPowerHistory = null;
    @SerializedName("climatPowerHistory")
    @Expose
    private List<Integer> climatPowerHistory = null;

    public Integer getRoomId() {
        return roomId;
    }

    public void setRoomId(Integer roomId) {
        this.roomId = roomId;
    }

    public List<Integer> getLightPowerHistory() {
        return lightPowerHistory;
    }

    public void setLightPowerHistory(List<Integer> lightPowerHistory) {
        this.lightPowerHistory = lightPowerHistory;
    }

    public List<Integer> getClimatPowerHistory() {
        return climatPowerHistory;
    }

    public void setClimatPowerHistory(List<Integer> climatPowerHistory) {
        this.climatPowerHistory = climatPowerHistory;
    }

}