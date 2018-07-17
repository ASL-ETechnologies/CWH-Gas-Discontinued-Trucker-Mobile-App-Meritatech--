package com.meritatech.myrewardzpos.interfaces;

import com.meritatech.myrewardzpos.model.LoginDataModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by user on 12/8/2017.
 */

public interface loginInterface {
    @GET("login.php")
    Call<LoginDataModel> login(
            @Query("smId") String smId,
            @Query("pwd") String pwd,
            @Query("token") String token,
              @Query("devDetails") String devDetails
    );
}
