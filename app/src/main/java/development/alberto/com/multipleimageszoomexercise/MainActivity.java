package development.alberto.com.multipleimageszoomexercise;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.List;

import development.alberto.com.multipleimageszoomexercise.Model.Movie;
import development.alberto.com.multipleimageszoomexercise.api.IMovies;
import development.alberto.com.multipleimageszoomexercise.util.Constant;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MainActivity extends AppCompatActivity {

    private RestAdapter mRestAdapt;
    private IMovies mApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //restAdapter of the models
        mRestAdapt = new RestAdapter.Builder()
                .setEndpoint(Constant.BASE_URL)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();
        mApi = mRestAdapt.create(IMovies.class);
        mApi.getMovies(new Callback<List<Movie>>() {
            @Override
            public void success(List<Movie> movies, Response response) {
                //set up de View Pager
                ViewPager mPager = (ViewPager) findViewById(R.id.pager);
                ScreenSlidePagerAdapter mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager(), movies);
                mPager.setAdapter(mPagerAdapter);
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });

    }
}
