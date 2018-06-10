package zpi.pls.zpidominator2000.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by wojciech.liebert on 11.05.2018.
 */

/**
 * {@link android.support.v4.view.PagerAdapter} that can display a relevant informations about a selected {@link zpi.pls.zpidominator2000.Model.Rooms.Room}
 */
public class OneRoomPageAdapter extends FragmentStatePagerAdapter {
    List<Fragment> fragmentsInsideMe;
    List<String> titles;

    public OneRoomPageAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
        fragmentsInsideMe = new LinkedList<>();
        titles = new LinkedList<>();
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentsInsideMe.get(position);
    }

    @Override
    public int getCount() {
        return fragmentsInsideMe.size();
    }

    public void addFragment(Fragment fragment, String title) {
        fragmentsInsideMe.add(fragment);
        titles.add(title);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles.get(position);
    }

    public void d() {
        for (Fragment f : fragmentsInsideMe) {
            f.onDestroy();
        }
    }
}
