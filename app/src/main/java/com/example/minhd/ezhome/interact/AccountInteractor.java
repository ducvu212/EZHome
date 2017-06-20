package com.t3h.basemvp.interact;

import com.t3h.basemvp.common.Constants;
import com.t3h.basemvp.module.ItemSongReponse;
import com.t3h.basemvp.module.ItemSongSearch;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;

/**
 * Created by ducnd on 5/21/17.
 */

public class AccountInteractor {
    private static AccountInteractor instance = new AccountInteractor();

    private AccountInteractor() {

    }

    public static AccountInteractor getInstance() {
        return instance;
    }

    public Observable<List<ItemSongReponse>> getMusic(String name) {
        return ApiConnector.getInstance().getMusic(name);


//        ApiConnector.getInstance().getMusic(name)
//                .subscribe(new Consumer<List<ItemSongReponse>>() {
//                    @Override
//                    public void accept(List<ItemSongReponse> itemSongReponses) throws Exception {
//
//                    }
//                }, new Consumer<Throwable>() {
//                    @Override
//                    public void accept(Throwable throwable) throws Exception {
//
//                    }
//                });
    }

    public Observable<List<ItemSongSearch>> getListMusic(String nameMusic) {
        return ApiConnector.getInstance().getListMusic(nameMusic,
                Constants.WEB_SITE_NAME,
                Constants.CODE);
    }
}
