package com.t3h.basemvp.interact;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.t3h.basemvp.module.ItemSongReponse;
import com.t3h.basemvp.module.ItemSongSearch;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by ducnd on 5/21/17.
 */

class ApiConnector {
    private static ApiConnector instance = new ApiConnector();
    private ApiService apiService;

    private ApiConnector() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS);

        Retrofit.Builder retrofit = new Retrofit.Builder();
        retrofit.baseUrl("http://j.ginggong.com")
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(builder.build());
        apiService = retrofit.build().create(ApiService.class);
    }

    static ApiConnector getInstance() {
        return instance;
    }

    public Observable<List<ItemSongReponse>> getMusic(String name) {
        String newName = name.replaceAll(" ", "+");
        //tuong tac voi server
        return Observable.create(new ObservableOnSubscribe<List<ItemSongReponse>>() {
            @Override
            public void subscribe(ObservableEmitter<List<ItemSongReponse>> e)
                    throws Exception {
                List<ItemSongReponse> reponses = new ArrayList<>();
                Document document = Jsoup.connect("http://mp3.zing.vn/tim-kiem/bai-hat.html?q=" + newName).get();
                Element element = document.select("div.wrap-content").first();
                Elements elements = element.select("div.item-song");
                for (Element elSong : elements) {
                    String dataCode = elSong.attr("data-code").toString();
                    String link = elSong.select("h3").select("a").first().attr("href").toString();
                    String title = elSong.select("h3").select("a").first().attr("title").toString();
                    reponses.add(new ItemSongReponse(dataCode, title));
                }
                //
                e.onNext(reponses);
            }
        });

    }

    public Observable<List<ItemSongSearch>> getListMusic(String nameMusic, String webSite,
                                                         String code) {
        return apiService.queryMusic(nameMusic, webSite, code);
    }
}
