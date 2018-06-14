package zpi.pls.zpidominator2000.Fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.Group;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import zpi.pls.zpidominator2000.Api.ZpiApiService;
import zpi.pls.zpidominator2000.Model.HouseTemp;
import zpi.pls.zpidominator2000.Model.OutsideTemp;
import zpi.pls.zpidominator2000.Model.RoomTemp;
import zpi.pls.zpidominator2000.Model.Rooms;
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
    private static final String LOG_TAG = HomePlanFragment.class.getSimpleName();
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
    private CardView floorInfoCard;
    private CardView roomInfoCard;
    private ZpiApiService apiService;
    private TextView floorTemp;
    private TextView outsideTemp;
    private TextView roomTemp;
    private TextView roomSetTemp;
    private List<Rooms.Room> roomList;
    private TextView roomName;
    private RoomsFragment.OnRoomSelectedListener onRoomSelectedListener;
    private Group floorGroup;
    private Group roomGroup;
    private ProgressBar floorProgressBar;
    private ProgressBar roomProgressBar;

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
    public static HomePlanFragment newInstance(String param1,
                                               String param2,
                                               ZpiApiService zpiApiService,
                                               RoomsFragment.OnRoomSelectedListener onRoomSelectedListener) {
        HomePlanFragment fragment = new HomePlanFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        fragment.apiService = zpiApiService;
        fragment.onRoomSelectedListener = onRoomSelectedListener;
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
        floorInfoCard = view.findViewById(R.id.cardViewFloorInfo);
        roomInfoCard = view.findViewById(R.id.cardViewRoomInfo);
        floorTemp = view.findViewById(R.id.floor_info_floor_temp);
        outsideTemp = view.findViewById(R.id.floor_info_outside_temp_val);
        floorGroup = view.findViewById(R.id.group_floor);
        floorProgressBar = view.findViewById(R.id.progressBar_floor_card);

        roomTemp = view.findViewById(R.id.floor_info_room_temp);
        roomSetTemp = view.findViewById(R.id.floor_info_room_settemp);
        roomName = view.findViewById(R.id.floor_info_room_name);
        roomGroup = view.findViewById(R.id.group_room);
        roomProgressBar = view.findViewById(R.id.progressBar_room_card);

        loadRooms();
        loadFloor(isOnZeroFloor);
    }

    private void loadRooms() {
        if (apiService != null) {
            apiService.listRooms()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .onErrorReturn(throwable -> new Rooms())
                    .onErrorResumeNext(throwable -> {
                        Utils.showToast(getContext(), "Couldn't load rooms for home plan");
                    })
                    .doOnNext(rooms -> HomePlanFragment.this.roomList = rooms.getRooms())
                    .subscribe();
        }
    }

    @SuppressLint("DefaultLocale")
    private void showFloorInfoCard() {
        floorInfoCard.setVisibility(View.VISIBLE);
        roomInfoCard.setVisibility(View.INVISIBLE);
        floorGroup.setVisibility(View.GONE);
        floorProgressBar.setVisibility(View.VISIBLE);

        if (apiService != null) {
            apiService.getHouseTemp()
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .onErrorResumeNext(throwable -> {
                        Utils.showToast(HomePlanFragment.this.getContext(), "Couldn't update house temp");
                    })
                    .onErrorReturn(throwable -> new HouseTemp())
                .doOnError(throwable -> {})
                .zipWith(apiService.getOutsideTemp()
                            .subscribeOn(Schedulers.io())
                        .onErrorReturn(throwable -> new OutsideTemp())
                            .observeOn(AndroidSchedulers.mainThread()), (houseTemp, outsideTemp) -> {
                        floorTemp.setText(String.format("%.1f °C", houseTemp.getHouseTemperature()));
                        HomePlanFragment.this.outsideTemp.setText(String.format("%.1f °C", outsideTemp.getTemperature()));
                        floorGroup.setVisibility(View.VISIBLE);
                        floorProgressBar.setVisibility(View.GONE);
                        return true;
                    })
                    .subscribe();
        }
    }

    @SuppressLint("DefaultLocale")
    private void showRoomInfoCard(Rooms.Room room) {
        floorInfoCard.setVisibility(View.INVISIBLE);
        roomInfoCard.setVisibility(View.VISIBLE);
        roomGroup.setVisibility(View.GONE);
        roomProgressBar.setVisibility(View.VISIBLE);
        if (onRoomSelectedListener != null) {
            roomInfoCard.setOnClickListener(v -> {
                onRoomSelectedListener.onRoomListFragmentInteraction(room);
            });
        }

        roomName.setText(room.getName());
        apiService.getTempInRoom(room.getRoomId())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .onErrorResumeNext(throwable -> {Utils.showToast(getContext(), "Couldn't update room temp");})
                .onErrorReturn(throwable -> new RoomTemp())
                .doOnNext(roomTemp -> {
                    HomePlanFragment.this.roomTemp.setText(String.format("%.1f °C", roomTemp.getTemperature()));
                    roomSetTemp.setText(String.format("%.1f °C", roomTemp.getSetTemperature()));
                    roomProgressBar.setVisibility(View.GONE);
                    roomGroup.setVisibility(View.VISIBLE);
                }).subscribe();
    }

    public void swapFloor() {
        isOnZeroFloor = !isOnZeroFloor;
        loadFloor(isOnZeroFloor);
    }

    private void loadFloor(boolean isOnGroundFloor) {
        showFloorInfoCard();

        View view = getView();
        if (view != null) {
            ViewGroup floor_content = view.findViewById(R.id.include_floor);
            floor_content.removeAllViews();
            if (isOnGroundFloor) {
                getLayoutInflater().inflate(R.layout.parter_layout, floor_content);
                homePlanIv = view.findViewById(R.id.homeFromAboveIv);
                Glide.with(this)
                        .load(R.drawable.a1)
                        .into(homePlanIv);
                homeName.setText(R.string.ground_floor);

                view.findViewById(R.id.button_parter1_1).setOnClickListener(v -> {
                    goToRoom(0);
                });
                view.findViewById(R.id.button_parter1_2).setOnClickListener(v -> {
                    goToRoom(0);
                });
                view.findViewById(R.id.button_parter2).setOnClickListener(v -> {
                    goToRoom(1);
                });
                view.findViewById(R.id.button_parter3).setOnClickListener(v -> {
                    goToRoom(2);
                });
                view.findViewById(R.id.button_parter4).setOnClickListener(v -> {
                    goToRoom(3);
                });
                view.findViewById(R.id.button_parter5).setOnClickListener(v -> {
                    goToRoom(4);
                });
            } else {
                getLayoutInflater().inflate(R.layout.pietro1_layout, floor_content);
                homePlanIv = view.findViewById(R.id.homeFromAboveIv);
                Glide.with(this)
                        .load(R.drawable.a1)
                        .into(homePlanIv);
                homeName.setText(R.string.first_floor);

                view.findViewById(R.id.button_pietro1_1).setOnClickListener(v -> {
                    goToRoom(5);
                });
                view.findViewById(R.id.button_pietro1_2).setOnClickListener(v -> {
                    goToRoom(6);
                });
                view.findViewById(R.id.button_pietro1_3).setOnClickListener(v -> {
                    goToRoom(7);
                });
                view.findViewById(R.id.button_pietro1_4).setOnClickListener(v -> {
                    goToRoom(8);
                });
                view.findViewById(R.id.button_pietro1_5).setOnClickListener(v -> {
                    goToRoom(9);
                });
            }
        }
    }

    private void goToRoom(int index) {
        if (roomList != null) {
            if (!roomList.isEmpty()) {
                showRoomInfoCard(roomList.get(index));
            } else  {
                Log.w(LOG_TAG, "Can't go to room because rooms list downloaded from server is empty");
            }
        } else {
            Log.w(LOG_TAG, "Can't go to room because there are no rooms downloaded from server");
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
