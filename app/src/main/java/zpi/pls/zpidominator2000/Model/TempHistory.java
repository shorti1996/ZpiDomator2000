package zpi.pls.zpidominator2000.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by wojciech.liebert on 24.05.2018.
 */

public class TempHistory {

    @SerializedName("roomId")
    @Expose
    private Integer roomId;
    @SerializedName("temperatureHistory")
    @Expose
    private List<Integer> temperatureHistory = null;

    public Integer getRoomId() {
        return roomId;
    }

    public void setRoomId(Integer roomId) {
        this.roomId = roomId;
    }

    public List<Integer> getTemperatureHistory() {
        return temperatureHistory;
    }

    public void setTemperatureHistory(List<Integer> temperatureHistory) {
        this.temperatureHistory = temperatureHistory;
    }

}