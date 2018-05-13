package zpi.pls.zpidominator2000.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by wojciech.liebert on 14.05.2018.
 */

public class RoomTemp {

    @SerializedName("roomId")
    @Expose
    private Integer roomId;
    @SerializedName("temperature")
    @Expose
    private Double temperature;
    @SerializedName("setTemperature")
    @Expose
    private Integer setTemperature;

    public Integer getRoomId() {
        return roomId;
    }

    public void setRoomId(Integer roomId) {
        this.roomId = roomId;
    }

    public Double getTemperature() {
        return temperature;
    }

    public void setTemperature(Double temperature) {
        this.temperature = temperature;
    }

    public Integer getSetTemperature() {
        return setTemperature;
    }

    public void setSetTemperature(Integer setTemperature) {
        this.setTemperature = setTemperature;
    }

}