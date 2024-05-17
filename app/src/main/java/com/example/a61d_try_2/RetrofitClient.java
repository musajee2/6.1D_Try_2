package com.example.a61d_try_2;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public class RetrofitClient {

    public interface QuizRequest {
        @GET("getQuiz")
        Call<com.example.a61d_try_2.QuizResponse> getQuiz(@Query("topic") String topic);
    }

    private static final String BASE_URL = "http://10.0.2.2:5000/";
    private static RetrofitClient instance;
    private QuizRequest quizRequest;

    private RetrofitClient() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(new OkHttpClient.Builder().readTimeout(10, java.util.concurrent.TimeUnit.MINUTES).build())
                .build();
        quizRequest = retrofit.create(QuizRequest.class);
    }

    public static synchronized RetrofitClient getInstance() {
        if (instance == null) {
            instance = new RetrofitClient();
        }
        return instance;
    }

    public void fetchQuestions(String topic, Callback<com.example.a61d_try_2.QuizResponse> callback) {
        quizRequest.getQuiz(topic).enqueue(callback);
    }
}
