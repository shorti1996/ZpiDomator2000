package zpi.pls.zpidominator2000.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by wojciech.liebert on 13.05.2018.
 */

public class Rooms {

    @SerializedName("rooms")
    @Expose
    private List<Room> rooms = new LinkedList<>();

    public List<Room> getRooms() {
        return rooms;
    }

    public void setRooms(List<Room> rooms) {
        this.rooms = rooms;
    }

    public class Room {

        @SerializedName("roomId")
        @Expose
        private Integer roomId;
        @SerializedName("name")
        @Expose
        private String name;

        public Integer getRoomId() {
            return roomId;
        }

        public void setRoomId(Integer roomId) {
            this.roomId = roomId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

    }

}