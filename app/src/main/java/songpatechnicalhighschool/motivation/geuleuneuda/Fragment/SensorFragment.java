package songpatechnicalhighschool.motivation.geuleuneuda.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import songpatechnicalhighschool.motivation.geuleuneuda.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SensorFragment extends Fragment {


    public SensorFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_sensor, container, false);
        return root;
    }

}
