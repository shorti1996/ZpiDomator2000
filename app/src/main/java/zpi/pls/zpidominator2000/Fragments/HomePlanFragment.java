package zpi.pls.zpidominator2000.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import zpi.pls.zpidominator2000.R;
import zpi.pls.zpidominator2000.Utils;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HomePlanFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HomePlanFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomePlanFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    ImageView homePlanIv;
    ConstraintLayout rootView;
    TextView floorText;
    TextView homeName;

    boolean isOnZeroFloor = true;

    private OnFragmentInteractionListener mListener;

    public HomePlanFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomePlanFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomePlanFragment newInstance(String param1, String param2) {
        HomePlanFragment fragment = new HomePlanFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home_plan, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rootView = view.findViewById(R.id.ConstraintLayoutRoot);
        floorText = view.findViewById(R.id.floorTv);
        homeName = view.findViewById(R.id.home_name);
/*        homePlanIv.setOnClickListener(view1 -> {
            swapFloor();
        });*/
        loadFloor(isOnZeroFloor);

    }

    public void swapFloor() {
        isOnZeroFloor = !isOnZeroFloor;
        loadFloor(isOnZeroFloor);
    }

    private void loadFloor(boolean isOnGroundFloor) {
        View view = getView();
        if (view != null) {
            ViewGroup floor_content = view.findViewById(R.id.include_floor);
            floor_content.removeAllViews();
            if (isOnGroundFloor) {
                getLayoutInflater().inflate(R.layout.parter_layout, floor_content);
                homePlanIv = view.findViewById(R.id.homeFromAboveIv);
                Glide.with(this)
                        .load(R.drawable.parter)
                        .into(homePlanIv);
                homeName.setText(R.string.ground_floor);

                view.findViewById(R.id.button_parter1_1).setOnClickListener(v -> {
                    Utils.showToast(getContext(), "1");
                });
                view.findViewById(R.id.button_parter1_2).setOnClickListener(v -> {
                    Utils.showToast(getContext(), "1");
                });
                view.findViewById(R.id.button_parter2).setOnClickListener(v -> {
                    Utils.showToast(getContext(), "2");
                });
                view.findViewById(R.id.button_parter3).setOnClickListener(v -> {
                    Utils.showToast(getContext(), "3");
                });
                view.findViewById(R.id.button_parter4).setOnClickListener(v -> {
                    Utils.showToast(getContext(), "4");
                });
                view.findViewById(R.id.button_parter5).setOnClickListener(v -> {
                    Utils.showToast(getContext(), "5");
                });
            } else {
                getLayoutInflater().inflate(R.layout.pietro1_layout, floor_content);
                homePlanIv = view.findViewById(R.id.homeFromAboveIv);
                Glide.with(this)
                        .load(R.drawable.pietro1)
                        .into(homePlanIv);
                homeName.setText(R.string.first_floor);

                view.findViewById(R.id.button_pietro1_1).setOnClickListener(v -> {
                    Utils.showToast(getContext(), "1");
                });
                view.findViewById(R.id.button_pietro1_2).setOnClickListener(v -> {
                    Utils.showToast(getContext(), "2");
                });
                view.findViewById(R.id.button_pietro1_3).setOnClickListener(v -> {
                    Utils.showToast(getContext(), "3");
                });
                view.findViewById(R.id.button_pietro1_4).setOnClickListener(v -> {
                    Utils.showToast(getContext(), "4");
                });
                view.findViewById(R.id.button_pietro1_5).setOnClickListener(v -> {
                    Utils.showToast(getContext(), "5");
                });
            }
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnOneRoomInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public interface HomePlanFragmentInteractionListener {
        void onSwapFloor();
    }
}
