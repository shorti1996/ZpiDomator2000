package zpi.pls.zpidominator2000.Api;

import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import zpi.pls.zpidominator2000.Model.Lights;
import zpi.pls.zpidominator2000.Model.RoomTemp;
import zpi.pls.zpidominator2000.Model.Rooms;

/**
 * Created by wojciech.liebert on 13.05.2018.
 */

public interface ZpiApiService {

    @GET("room")
    Observable<Rooms> listRooms();

    @GET("temp/{id}")
    Observable<RoomTemp> getTempInRoom(@Path("id") int id);

    @PUT("temp/{id}")
    Observable<Response<Void>> setTempInRoom(@Path("id") int id, @Body RoomTemp roomTemp);

    @GET("light/{id}")
    Observable<Lights> getLightsInRoom(@Path("id") int id);
}
