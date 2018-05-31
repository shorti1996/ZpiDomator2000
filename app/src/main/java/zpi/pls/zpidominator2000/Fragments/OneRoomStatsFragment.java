package zpi.pls.zpidominator2000.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import zpi.pls.zpidominator2000.Api.ZpiApiService;
import zpi.pls.zpidominator2000.Model.TempHistory;
import zpi.pls.zpidominator2000.R;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OneRoomStatsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link OneRoomStatsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OneRoomStatsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private final int N_LAST_TEMP_ENTRIES_DEFAULT = 300;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private ZpiApiService apiService;
    private int roomId;
    public LineChart tempLineChart;

    public OneRoomStatsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment OneRoomStatsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static OneRoomStatsFragment newInstance(ZpiApiService param1, int param2) {
        OneRoomStatsFragment fragment = new OneRoomStatsFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
        fragment.apiService = param1;
        fragment.roomId = param2;
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_one_room_stats, container, false);
        tempLineChart = view.findViewById(R.id.one_room_stats_temp_line_chart);

        Observable<TempHistory> tempHistoryObservable = apiService.getTempHistoryForRoom(roomId, N_LAST_TEMP_ENTRIES_DEFAULT);

        tempHistoryObservable
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .doOnError(
                        x -> {
                            Log.d("AA", x.getMessage());
                            Toast.makeText(getContext(), "Couldn't load temp history", Toast.LENGTH_SHORT).show();
                        })


                .onErrorReturnItem(new TempHistory())
                .subscribe((TempHistory tempHistory) -> {
                    List<Entry> entries = new ArrayList<>();
                    List<Double> temperatureHistory = tempHistory.getTemperatureHistory();
                    if (temperatureHistory != null && !temperatureHistory.isEmpty()) {
                        for (int i = 0; i < temperatureHistory.size(); i++) {
                            Integer historyEntry = (temperatureHistory.get(i)).intValue();
                            Log.d("AA", "" + historyEntry);
                            entries.add(new Entry(i, historyEntry));
                        }
                        LineDataSet lineDataSet = new LineDataSet(entries, "Temperatura");
                        LineData lineData = new LineData(lineDataSet);
                        tempLineChart.setData(lineData);
                        tempLineChart.invalidate();
                    } else {
                        Log.d("AA", "empty history :(");
                    }
                });
        return view;
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
