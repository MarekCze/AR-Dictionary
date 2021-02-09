package ie.lit.ardictionary.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import ie.lit.ardictionary.model.Word;
import ie.lit.ardictionary.utils.WordDeserializer;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DictionaryApi {

    private static final String BASE_URL = "https://dictionaryapi.com/api/v3/references/sd3/json/";
    private String str = "{word}?key=0fb33c6f-c632-467c-8f72-1be2b685075b";

    private static Retrofit retrofit = null;

    public static Retrofit getClient(){
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        Gson gson = initGson();

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .build();

        return retrofit;
    }

    private static Gson initGson(){
        Type type = new TypeToken<List<Word>>(){}.getType();
        GsonBuilder gsonBuilder = new GsonBuilder().registerTypeAdapter(type, new WordDeserializer());
        Gson gson = gsonBuilder.create();

        return gson;
    }
}
