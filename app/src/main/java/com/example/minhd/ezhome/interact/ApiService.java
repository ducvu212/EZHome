package com.t3h.basemvp.interact;

import com.t3h.basemvp.module.ItemSongReponse;
import com.t3h.basemvp.module.ItemSongSearch;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by ducnd on 5/25/17.
 */

public interface ApiService {
    @GET("jOut.ashx")
    Observable<List<ItemSongSearch>> queryMusic(@Query("k") String musicName, @Query("h") String webSite,
                                               @Query("code") String code);


}
