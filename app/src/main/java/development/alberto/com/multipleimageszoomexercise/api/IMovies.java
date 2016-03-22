package development.alberto.com.multipleimageszoomexercise.api;

import java.util.List;

import development.alberto.com.multipleimageszoomexercise.Model.Movie;
import development.alberto.com.multipleimageszoomexercise.util.Constant;
import retrofit.Callback;
import retrofit.http.GET;

/**
 * Created by alber on 22/03/2016.
 */
public interface IMovies {
    @GET(Constant.MOVIES)
    public void getMovies(Callback<List<Movie>> response);
}
