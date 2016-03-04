package custom.view;

import android.app.Fragment;

import xyz.anmai.easybill.DetailsFragment;
import xyz.anmai.easybill.MainFragment;
import xyz.anmai.easybill.R;
import xyz.anmai.easybill.ReportFragment;

/**
 * Created by anquan on 2016/3/1.
 */
public class FragmentFactory {
    public static Fragment getInstanceByIndex(int index) {
        Fragment fragment = null;
        switch (index) {
            case R.id.radiobutton_details:
                fragment = new DetailsFragment();
                break;
            case R.id.radiobutton_main:
                fragment = new MainFragment();
                break;
            case R.id.radiobutton_report:
                fragment = new ReportFragment();
                break;
        }
        return fragment;
    }
}