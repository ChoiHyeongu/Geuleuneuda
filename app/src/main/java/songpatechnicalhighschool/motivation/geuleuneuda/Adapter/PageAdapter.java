package songpatechnicalhighschool.motivation.geuleuneuda.Adapter;

import android.hardware.Sensor;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import songpatechnicalhighschool.motivation.geuleuneuda.Fragment.ListFragment;
import songpatechnicalhighschool.motivation.geuleuneuda.Fragment.SensorFragment;

public class PageAdapter extends FragmentStatePagerAdapter {

    int size;

    public PageAdapter(FragmentManager fm, int size) {
        super(fm);
        this.size = size;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                SensorFragment sensorFragment = new SensorFragment();
                return sensorFragment;
            case 1:
                ListFragment listFragment = new ListFragment();
                return  listFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return size;
    }
}
