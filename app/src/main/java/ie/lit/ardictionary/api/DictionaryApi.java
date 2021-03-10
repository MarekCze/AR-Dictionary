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
    // API url
    private static final String BASE_URL = "https://dictionaryapi.com/api/v3/references/sd3/json/";
    // key
    private String str = "{word}?key=0fb33c6f-c632-467c-8f72-1be2b685075b";
    private String apiKey = "eaccd8cdb6b1812304e778a7f862d430";
    private static Retrofit retrofit = null;
    // build Retrofit client
    public static Retrofit getClient(){
        // build interceptor for logging
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
    // build custom GSON instance
    private static Gson initGson(){
        Type type = new TypeToken<Word>(){}.getType();
        GsonBuilder gsonBuilder = new GsonBuilder().registerTypeAdapter(type, new WordDeserializer());
        Gson gson = gsonBuilder.create();

        return gson;
    }
}
