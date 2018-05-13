package zpi.pls.zpidominator2000;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.LinkedList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnOneRoomInteractionListener} interface
 * to handle interaction events.
 * Use the {@link OneRoomFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OneRoomFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnOneRoomInteractionListener mListener;

    private TextView titleView;
    private ViewPager viewPager;

    private TabLayout tabLayout;

    public OneRoomFragment() {
        // Required empty public constructor
    }

    // TODO: Maybe uri or something
    public static OneRoomFragment newInstance(String id) {
        OneRoomFragment fragment = new OneRoomFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, id);
//        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//        Toast.makeText(this.getContext(), mParam1, Toast.LENGTH_SHORT).show();
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_one_room, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        titleView = view.findViewById(R.id.one_room_title);
//        titleView.setText(mParam1);
        viewPager = view.findViewById(R.id.one_room_viewpager);
        setupViewPager(viewPager);

    }

    private void setupViewPager(ViewPager viewPager) {
        OneRoomSettingsFragment oneRoomSettingsFragment = OneRoomSettingsFragment.newInstance("", "");
        OneRoomStatsFragment oneRoomStatsFragment = OneRoomStatsFragment.newInstance("", "");
        OneRoomPageAdapter adapter = new OneRoomPageAdapter(getFragmentManager());
//        oneRoomSettingsFragment.setRetainInstance(true);
//        oneRoomStatsFragment.setRetainInstance(true);
        adapter.addFragment(oneRoomSettingsFragment, "CONTROL");
        adapter.addFragment(oneRoomStatsFragment, "STATS");
        viewPager.setAdapter(null);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(0);
        tabLayout.setupWithViewPager(viewPager);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onOneRoomFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnOneRoomInteractionListener) {
            mListener = (OnOneRoomInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnOneRoomInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener.onOneRoomFragmentByeBye();
        mListener = null;
    }

    public void setTabLayout(TabLayout tabLayout) {
        this.tabLayout = tabLayout;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnOneRoomInteractionListener {
        // TODO: Update argument type and name
        void onOneRoomFragmentInteraction(Uri uri);
        void onOneRoomFragmentByeBye();
    }
}
