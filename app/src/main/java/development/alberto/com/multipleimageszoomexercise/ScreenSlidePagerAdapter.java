package development.alberto.com.multipleimageszoomexercise;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

import development.alberto.com.multipleimageszoomexercise.Model.Movie;

/**
 * Created by alber on 22/03/2016.
 */
public class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {

    private List<Movie> model;
    private String img;

    public ScreenSlidePagerAdapter(FragmentManager fm, List<Movie> model) {
        super(fm);
        this.model = model;
    }


    @Override
    public Fragment getItem(int position) {
        img = model.get(position).getImage();
        FragmentImages fi = new FragmentImages();
        fi.setImg(getImg());
        return fi;
    }

    @Override
    public int getCount() {
        return model.size();
    }

    /**Extra methods**/
    public String getImg(){
        return img;
    }
}
