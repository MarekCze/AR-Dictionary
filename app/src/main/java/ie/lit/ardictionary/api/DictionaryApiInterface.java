package ie.lit.ardictionary.api;

import java.util.List;

import ie.lit.ardictionary.model.Word;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface DictionaryApiInterface {

    @GET("{word}")
    Call<List<Word>> getWord(@Path("word") String word, @Query("key") String key);
}
