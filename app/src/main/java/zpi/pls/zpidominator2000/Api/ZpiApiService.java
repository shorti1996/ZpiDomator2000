package zpi.pls.zpidominator2000.Api;

import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import zpi.pls.zpidominator2000.Model.Lights;
import zpi.pls.zpidominator2000.Model.Power;
import zpi.pls.zpidominator2000.Model.RoomTemp;
import zpi.pls.zpidominator2000.Model.Rooms;
import zpi.pls.zpidominator2000.Model.TempHistory;

/**
 * Created by wojciech.liebert on 13.05.2018.
 */

public interface ZpiApiService {

    @GET("room")
    Observable<Rooms> listRooms();

    @GET("temp/{id}")
    Observable<RoomTemp> getTempInRoom(@Path("id") int id);

    @PUT("temp/{id}")
    Observable<Response<Void>> setTempInRoom(@Path("id") int id,
                                             @Body RoomTemp roomTemp);

    @GET("temp/{id}/history/{nLast}")
    Observable<TempHistory> getTempHistoryForRoom(@Path("id") int id,
                                                  @Path("nLast") int nLastEntries);

    @GET("light/{id}")
    Observable<Lights> getLightsInRoom(@Path("id") int id);

    @PUT("light/{id}")
    Observable<Response<Void>> setLightInRoom(@Path("id") int roomId,
                                              @Query("lightId") String lightId,
                                              @Query("state") boolean state);

    @GET("power/{id}/history/{nLast}")
    Observable<Power> getPowerHistoryForRoom(@Path("id") int id,
                                             @Path("nLast") int nLastEntries);
}
