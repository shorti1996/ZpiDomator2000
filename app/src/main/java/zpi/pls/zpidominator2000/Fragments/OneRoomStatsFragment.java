package zpi.pls.zpidominator2000.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import zpi.pls.zpidominator2000.Api.ZpiApiService;
import zpi.pls.zpidominator2000.Model.TempHistory;
import zpi.pls.zpidominator2000.R;
import zpi.pls.zpidominator2000.Utils;


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
    private final int N_LAST_TEMP_ENTRIES_DAY = 30;
    private final int N_LAST_TEMP_ENTRIES_MONTH = 300;
    private final int N_LAST_POW_ENTRIES_DAY = 30;
    private final int N_LAST_POW_ENTRIES_MONTH = 300;
    private final int N_LAST_LIGHT_ENTRIES_DAY = 30;
    private final int N_LAST_LIGHT_ENTRIES_MONTH = 300;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private ZpiApiService apiService;
    private int roomId;
    public LineChart chart1;
    public LineChart chart2;
    private TextView chart1Title;
    private TextView chart2Title;
    private Spinner spinner;
    private ArrayAdapter<String> spinnerAdapter;
    private String[] spinnerItems;

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
        chart1 = view.findViewById(R.id.one_room_stats_temp_day_line_chart);
        chart2 = view.findViewById(R.id.one_room_stats_temp_month_line_chart);
        chart1Title = view.findViewById(R.id.chart_1_title);
        chart2Title = view.findViewById(R.id.chart_2_title);
        spinner = view.findViewById(R.id.one_room_stats_charts_spinner);
        spinnerItems = getResources().getStringArray(R.array.stats_spinner_array);
        spinnerAdapter = new ArrayAdapter<>(this.getContext(),
                R.layout.support_simple_spinner_dropdown_item,
                spinnerItems);
        spinner.setAdapter(spinnerAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String spinnerItem = spinnerItems[position];
                if (spinnerItem.equals(getString(R.string.stats_spinner_temp))) {
                    loadTemp();
                } else if (spinnerItem.equals(getString(R.string.stats_spinner_lights))) {
                    loadLight();
                } else if (spinnerItem.equals(getString(R.string.stats_spinner_power))) {
                    loadPower();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

//        tempHistoryObservableDay
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribeOn(Schedulers.io())
//                .doOnNext(tempHistory -> loadToChart(chart1, tempHistory.getTemperatureHistory()))
//                .subscribe();


//        tempHistoryObservableDay
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribeOn(Schedulers.io())
//                .onErrorResumeNext(x -> {
//                    Log.d("AA", "Couldn't load temp history");
//                    Utils.showToast(getContext(), "Couldn't load temp history");
//                })
//                .doOnNext((TempHistory tempHistory) -> {
//                    List<Entry> entries = new ArrayList<>();
//                    List<Integer> temperatureHistory = tempHistory.getTemperatureHistory();
//                    if (temperatureHistory != null && !temperatureHistory.isEmpty()) {
//                        for (int i = 0; i < temperatureHistory.size(); i++) {
//                            Integer historyEntry = (temperatureHistory.get(i)).intValue();
//                            Log.d("AA", "" + historyEntry);
//                            entries.add(new Entry(i, historyEntry));
//                        }
//                        LineDataSet lineDataSet = new LineDataSet(entries, "Temperatura");
//                        LineData lineData = new LineData(lineDataSet);
//                        chart1.setData(lineData);
//                        chart1.invalidate();
//                    }
//                })
//                .observeOn(Schedulers.io())
//                .flatMap(tempHistory -> tempHistoryObservableMonth)
//                .doOnNext(tempHistory -> {
//                    Log.d("AAAAAAAAAAAAA", "aaa");
//                    for (Integer d: tempHistory.getTemperatureHistory()) {
//                        Log.d("AA", "" + d);
//                    }
//                })
//                .subscribe();
        return view;
    }

    private void loadTemp() {
        Observable<TempHistory> tempHistoryObservableMonth = apiService.getTempHistoryForRoom(roomId, 10);
        Observable<TempHistory> tempHistoryObservableDay = apiService.getTempHistoryForRoom(roomId, N_LAST_LIGHT_ENTRIES_DAY);

//        List<Double> tempDay = Sine.generate(100, 1f);
//        List<Double> tempMonth = Sine.generate(300, 1f);
//        loadToChart(chart1, tempDay, "Temperatura");
//        loadToChart(chart2, tempMonth, "Temperatura");

        resetChartData(chart1);
        resetChartData(chart2);


        chart1Title.setText("Temperatura 24 h");
        chart2Title.setText("Temperatura 7 dni");

        tempHistoryObservableDay
                .timeout(10, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(tempHistoryDay -> {
                    loadToChart(chart1, tempHistoryDay.getTemperatureHistory(), "Temperatura");
                })
                .concatWith(tempHistoryObservableMonth
                        .timeout(10, TimeUnit.SECONDS)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnNext(tempHistoryMonth -> {
                            loadToChart(chart2, tempHistoryMonth.getTemperatureHistory(), "Temperatura");
                        })
                )
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorResumeNext(throwable -> {
                    Utils.showToast(getContext(), "BAD");
                })
                .subscribe();
    }

    private void loadLight() {
        List<Double> lightDay = Sine.generate(100, 1f);
        List<Double> lightMonth = Sine.generate(300, 1f);

        resetChartData(chart1);
        resetChartData(chart2);

        loadToChart(chart1, lightDay, "Światło");
        loadToChart(chart2, lightMonth, "Światło");

        chart1Title.setText("Światło 24 h");
        chart2Title.setText("Światło 7 dni");
    }

    private void loadPower() {
        List<Double> powerDay = Sine.generate(100, 1f);
        List<Double> powerMonth = Sine.generate(300, 1f);

        resetChartData(chart1);
        resetChartData(chart2);

        loadToChart(chart1, powerDay, "Energia");
        loadToChart(chart2, powerMonth, "Energia");

        chart1Title.setText("Energia 24 h");
        chart2Title.setText("Energia 7 dni");
    }

    private static void resetChartData(LineChart lineChart) {
        lineChart.setData(null);
        lineChart.invalidate();
    }

    private static void loadToChart(LineChart lineChart, List<Double> values, String dataLabel) {
        List<Entry> entries = new ArrayList<>();
        if (values != null && !values.isEmpty()) {
            for (int i = 0; i < values.size(); i++) {
                Double historyEntry = values.get(i);
                entries.add(new Entry(i, historyEntry.floatValue()));
            }
            LineDataSet lineDataSet = new LineDataSet(entries, dataLabel);
            LineData lineData = new LineData(lineDataSet);
            lineChart.setData(lineData);
            lineChart.invalidate();
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

    static class Sine {
        public static List<Double> generate(int count, float step) {
            List<Double> sinVals = new LinkedList<>();
            float curr = 0f;
            for (int i = 0; i < count; i++) {
                double v = Math.sin(Math.toRadians(curr));
                sinVals.add(v);
                curr += step;
            }
            return sinVals;
        }
    }
}
