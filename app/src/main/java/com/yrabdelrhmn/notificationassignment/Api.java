package com.yrabdelrhmn.notificationassignment;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface Api {
@POST("users")
Call<DataModel> createPost(@Body DataModel dataModel);
}
