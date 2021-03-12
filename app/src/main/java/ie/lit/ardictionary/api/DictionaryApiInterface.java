package ie.lit.ardictionary.api;

import java.util.List;

import ie.lit.ardictionary.model.Word;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface DictionaryApiInterface {
    @GET("entries/en/{word}")
    Call<Word> getWordDefinition(@Path("word") String word);

    @GET("lemmas/en/{rootWord}")
    Call<String> getRootWord(@Path("rootWord") String rootWord);
}
