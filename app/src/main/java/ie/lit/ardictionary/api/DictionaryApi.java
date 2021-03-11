package ie.lit.ardictionary.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import ie.lit.ardictionary.model.Word;
import ie.lit.ardictionary.utils.WordDeserializer;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DictionaryApi {
    // API credentials
    private static final String BASE_URL = "https://od-api.oxforddictionaries.com:443/api/v2/entries/en/";
    private static final String API_KEY = "ae363e9c82a1e76ae04efedd3016df96";
    private static final String APP_ID = "8a95da47";
    private static final String LANGUAGE = "en";

    private static Retrofit retrofit = null;

    // build Retrofit client
    public static Retrofit getClient(Gson gson){
        // build okHttpClient
        OkHttpClient okHttpClient = buildClient();

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(okHttpClient)
                .build();

        return retrofit;
    }

    private static OkHttpClient buildClient(){
        // create logging interceptor to log api response
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        // create header interceptor to pass in api auth credentials
        Interceptor apiAuthInterceptor = new Interceptor() {
            @NotNull
            @Override
            public Response intercept(@NotNull Chain chain) throws IOException {
                Request request = chain.request().newBuilder()
                        .addHeader("api_key", API_KEY)
                        .addHeader("app_id", APP_ID)
                        .build();

                return chain.proceed(request);
            }
        };

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .addInterceptor(apiAuthInterceptor)
                .build();

        return okHttpClient;
    }
}
