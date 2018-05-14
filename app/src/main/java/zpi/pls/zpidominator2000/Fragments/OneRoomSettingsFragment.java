package zpi.pls.zpidominator2000.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import zpi.pls.zpidominator2000.Api.ZpiApiService;
import zpi.pls.zpidominator2000.Model.RoomTemp;
import zpi.pls.zpidominator2000.R;

import static zpi.pls.zpidominator2000.Utils.showToast;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OneRoomSettingsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link OneRoomSettingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OneRoomSettingsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_ROOM_ID = "param1";
    private static final String ARG_ROOM_NAME = "param2";

    // TODO: Rename and change types of parameters
    private int roomId;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private ZpiApiService apiService;

    private TextView title;
    public String roomName;
    public TextView currentTempVal;

    public OneRoomSettingsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment OneRoomSettingsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static OneRoomSettingsFragment newInstance(ZpiApiService zpiApiService, int roomId, String roomName) {
        OneRoomSettingsFragment fragment = new OneRoomSettingsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_ROOM_ID, roomId);
        args.putString(ARG_ROOM_NAME, roomName);
        fragment.setArguments(args);
        fragment.apiService = zpiApiService;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            roomId = getArguments().getInt(ARG_ROOM_ID);
            roomName = getArguments().getString(ARG_ROOM_NAME);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_one_room_settings, container, false);

//        ((TextView) view.findViewById(R.id.one_room_title)).setText(roomName);
        currentTempVal = view.findViewById(R.id.one_room_current_temp_val);

//        RoomTemp roomTemp = new RoomTemp();
//        roomTemp.setSetTemperature(12);
//        Observable<Response<Void>> setTempInRoomObservable = apiService.setTempInRoom(roomId, roomTemp);
//
//        setTempInRoomObservable
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribeOn(Schedulers.io())
//                .doOnError(x -> showToast("Couldn't set temperature"))
//                .subscribe(x -> {
//                    Log.d("AAAAAAAAaa", "" + x.code());
//                    if (x.code() == HTTP_RESPONSE_OK) {
//                        showToast("Temperature has been set");
//                    } else {
//                        showToast("ERR: " + x.code());
//                    }
//                });

        Observable<Long> ping = Observable.interval(0L, 5L, TimeUnit.SECONDS, Schedulers.io());
        ping.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(tick -> UpdateCurrentTemp());

        return view;
    }

    private void UpdateCurrentTemp() {
        Observable<RoomTemp> roomTempObservable = apiService.getTempInRoom(roomId);
        roomTempObservable
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .doOnError(x -> showToast(getContext(), "Couldn't get temperature"))
                .subscribe(x -> currentTempVal.setText(formatTemperature(x.getTemperature())));
    }

    private String formatTemperature(double temperature) {
        String temperatureString = String.format("%.2f", temperature);
        return getResources().getString(R.string.one_room_curr_temp, temperatureString);
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
//                    + " must implement OnFragmentInteractionListener");
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
}
