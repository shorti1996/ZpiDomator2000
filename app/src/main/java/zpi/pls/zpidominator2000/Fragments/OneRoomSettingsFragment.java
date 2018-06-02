package zpi.pls.zpidominator2000.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.constraint.Group;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.jakewharton.rxbinding2.view.RxView;

import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;
import retrofit2.Response;
import zpi.pls.zpidominator2000.Adapters.OneRoomLightsAdapter;
import zpi.pls.zpidominator2000.Api.ZpiApiService;
import zpi.pls.zpidominator2000.Model.Lights;
import zpi.pls.zpidominator2000.Model.RoomTemp;
import zpi.pls.zpidominator2000.R;

import static zpi.pls.zpidominator2000.Api.ZpiApiRetrofitClient.HTTP_RESPONSE_OK;
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

    private static final String TAG = OneRoomSettingsFragment.class.getSimpleName();
    private static final String ARG_ROOM_ID = "param1";
    private static final String ARG_ROOM_NAME = "param2";
    public final int TEMPERATURE_SET_DEBOUNCE_TIMEOUT = 1000;
    public final long UPDATE_PING_INTERVAL = 5L;

    public String roomName;
    public TextView setTempVal;
    public TextView currentTempVal;
    public RecyclerView lightsRv;
    public Observable<Long> ping;
    public Observable<Integer> temperatureClicksObservable;
    public Disposable pingSubscription;
    public OneRoomLightsAdapter lightsAdapter;
    public ImageButton tempUp;
    public ImageButton tempDown;
    private Group tempGroup;
    private ProgressBar tempProgressBar;
    private ProgressBar lightsProgressBar;

    private ZpiApiService apiService;
    private int roomId;
    private double currTemp = 0;
    private double newTemp = 0;
    private AtomicInteger lightsToUpdateCount = new AtomicInteger(0);
    private AtomicBoolean shouldUpdateTemp = new AtomicBoolean(true);
    private AtomicBoolean shouldUpdateLights = new AtomicBoolean(true);
    private PublishSubject<Integer> clicksUpSubject = PublishSubject.create();
    private PublishSubject<Integer> clicksDownSubject = PublishSubject.create();
    private boolean isAfterInitialTempDownsync;
    private boolean isAfterInitialLightsDownsync;

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

    public double getCurrTemp() {
        return currTemp;
    }

    public void setCurrTemp(double currTemp) {
        this.currTemp = currTemp;
        currentTempVal.setText(formatCurrentTemperature(getCurrTemp()));
    }

    public double getNewTemp() {
        return newTemp;
    }

    public void setNewTemp(double newTemp) {
        this.newTemp = newTemp;
        setTempVal.setText(formatSetTemperature(this.newTemp));
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

        currentTempVal = view.findViewById(R.id.one_room_current_temp_val);
        setTempVal = view.findViewById(R.id.one_room_set_temp_val);
        lightsRv = view.findViewById(R.id.one_room_light_rv);
        lightsRv.setLayoutManager(new LinearLayoutManager(getContext()));
        tempDown = view.findViewById(R.id.one_room_temp_down);
        tempUp = view.findViewById(R.id.one_room_temp_up);
        tempGroup = view.findViewById(R.id.group_temp_settings_card);
        tempProgressBar = view.findViewById(R.id.progressBar_temp_setting_card);
        lightsProgressBar = view.findViewById(R.id.progressBar_light_settings_card);

        RxView.clicks(tempDown).observeOn(AndroidSchedulers.mainThread())
                .subscribe(o -> {
//            showToast(getContext(), "DONW");
                    shouldUpdateTemp.set(false);
                    setNewTemp(getNewTemp() - 1);
                    clicksUpSubject.onNext(-1);
        });
        RxView.clicks(tempUp).observeOn(AndroidSchedulers.mainThread())
                .subscribe(o -> {
                    shouldUpdateTemp.set(false);
                    setNewTemp(getNewTemp() + 1);
                    clicksUpSubject.onNext(1);
                });
        temperatureClicksObservable = clicksUpSubject.mergeWith(clicksDownSubject);
        temperatureClicksObservable.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(AndroidSchedulers.mainThread())
                .debounce(TEMPERATURE_SET_DEBOUNCE_TIMEOUT, TimeUnit.MILLISECONDS)
                .subscribe(x -> {
                    Log.d(TAG, "Updating temperature to " + getNewTemp());
                    getActivity().runOnUiThread(this::commitSetTemp);
                });

        ping = Observable.interval(0L, UPDATE_PING_INTERVAL, TimeUnit.SECONDS, Schedulers.io());
        pingSubscription = ping.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(tick -> downsyncSettings());

        tempGroup.setVisibility(View.INVISIBLE);
        tempProgressBar.setVisibility(View.VISIBLE);
        lightsProgressBar.setVisibility(View.VISIBLE);

        return view;
    }

    private void commitSetTemp() {
        Log.d(TAG, "Attempting to commit temperature to api");
        RoomTemp roomTemp = new RoomTemp();
        roomTemp.setSetTemperature(getNewTemp());
        Observable<Response<Void>> setTempInRoomObservable = apiService.setTempInRoom(roomId, roomTemp);

        setTempInRoomObservable
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .onErrorResumeNext(x -> {
                    shouldUpdateTemp.set(true);
                    showToast(getContext(), "Couldn't set temperature");
                })
                .subscribe(x -> {
                    Log.d(TAG, "" + x.code());
                    if (x.code() == HTTP_RESPONSE_OK) {
                        showToast(getContext(), "Temperature has been set");
                    } else {
                        showToast(getContext(), "ERR: " + x.code());
                    }
                    shouldUpdateTemp.set(true);
                });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        pingSubscription.dispose();
    }

    private void downsyncSettings() {
        downsyncTemperatures();
        downsyncLights();
    }

    private void downsyncTemperatures() {
        if (!shouldUpdateTemp.get()) {
            showToast(getContext(), "Not updating temperature, pending changes");
            return;
        }
        Observable<RoomTemp> roomTempObservable = apiService.getTempInRoom(roomId);
        roomTempObservable
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .onErrorResumeNext(x -> {showToast(getContext(), "Couldn't get temperature");})
                .subscribe(x -> {
                    if (x != null) {
                        Double temperature = x.getTemperature();
                        if (temperature != null) {
                            setCurrTemp(temperature.intValue());
                        }
                        Double setTemperature = x.getSetTemperature();
                        if (setTemperature != null) {
                            setNewTemp(setTemperature);
                        }
                        if (!isAfterInitialTempDownsync) {
                            if (setTemperature != null && temperature != null) {
                                tempGroup.setVisibility(View.VISIBLE);
                                tempProgressBar.setVisibility(View.GONE);
                                isAfterInitialTempDownsync = true;
                            }
                        }
                    }
                });
    }

    private void downsyncLights() {
        if (lightsToUpdateCount.get() > 0) {
            showToast(getContext(), "Not updating lights, pending changes");
            return;
        }
        Observable<Lights> roomLightObservable = apiService.getLightsInRoom(roomId);
        roomLightObservable
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .onErrorResumeNext(x -> {showToast(getContext(), "Couldn't get lights");})
                .onErrorReturn(throwable -> new Lights())
                .subscribe(x -> {
                    if (x != null) {
                        List<Lights.Light> lights = x.getLights();
                        if (lights != null) {
                            for (Lights.Light light : lights) {
                                Log.d(TAG, light.getName() + ", " + light.getState());
                            }
                            if (lightsAdapter == null) {
                                lightsAdapter = new OneRoomLightsAdapter(x, this::scheduleUpsyncLight);
                                lightsRv.setAdapter(lightsAdapter);
                                if (!isAfterInitialLightsDownsync) {
                                    lightsProgressBar.setVisibility(View.GONE);
                                    isAfterInitialLightsDownsync = true;
                                }
                            }
                            lightsAdapter.swapValues(x);
                        }
                    }
                });
    }

    private void scheduleUpsyncLight(Lights.Light light, boolean enabled) {
        lightsToUpdateCount.incrementAndGet();
        Log.d("Lights", "Light " + light.getId() + " update scheduled, to sync: " + lightsToUpdateCount.get());
        Observable<Response<Void>> setLightObservable = apiService.setLightInRoom(roomId, light.getId(), enabled);
        setLightObservable
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .onErrorResumeNext(throwable -> {
                    showToast(getContext(), String.format("Can't set light: %s", ""));
                    lightsToUpdateCount.decrementAndGet();
                })
                .subscribe(x -> {
                    showToast(getContext(), "Light updated");
                    lightsToUpdateCount.decrementAndGet();
                    Log.d("Lights", "Light updated, left to set: " + lightsToUpdateCount.get());
                });
    }

    private String formatCurrentTemperature(double temperature) {
        String temperatureString = String.format(Locale.getDefault(), "%.2f", temperature);
        return getResources().getString(R.string.one_room_curr_temp, temperatureString);
    }

    private String formatSetTemperature(double temperature) {
        String temperatureString = String.format(Locale.getDefault(), "%.0f", temperature);
        return getResources().getString(R.string.one_room_set_temp, temperatureString);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
//        mListener = null;
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
        void onFragmentInteraction(Lights.Light light);
    }

    public interface OnLightStateChangedListener {
        void onLightStateChanged(Lights.Light light, boolean enabled);
    }
}
