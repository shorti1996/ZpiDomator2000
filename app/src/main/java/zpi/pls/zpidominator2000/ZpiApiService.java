package zpi.pls.zpidominator2000;

import io.reactivex.Observable;
import retrofit2.http.GET;

/**
 * Created by wojciech.liebert on 13.05.2018.
 */

public interface ZpiApiService {

    @GET("room")
    Observable<Rooms> listRooms();


    // TODO etc
}
