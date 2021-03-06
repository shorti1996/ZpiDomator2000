package zpi.pls.zpidominator2000.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import zpi.pls.zpidominator2000.Adapters.MyRoomItemRecyclerViewAdapter;
import zpi.pls.zpidominator2000.Api.ZpiApiService;
import zpi.pls.zpidominator2000.Model.Rooms;
import zpi.pls.zpidominator2000.R;
import zpi.pls.zpidominator2000.Utils;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnRoomSelectedListener}
 * interface.
 */
public class RoomsFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnRoomSelectedListener mListener;
    private ZpiApiService apiService;
    private RecyclerView recyclerView;
    private View progressBar;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public RoomsFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static RoomsFragment newInstance(ZpiApiService zpiApiService) {
        RoomsFragment fragment = new RoomsFragment();
        fragment.apiService = zpiApiService;
//        Bundle args = new Bundle();
//        args.putInt(ARG_COLUMN_COUNT, columnCount);
//        fragment.setArguments(args);
//        fragment.mListener = callback;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_roomitem_list, container, false);

        progressBar = view.findViewById(R.id.progressBar_rooms_list);
        progressBar.setVisibility(View.VISIBLE);
        recyclerView = view.findViewById(R.id.room_list);
        // Set the adapter
            Context context = view.getContext();
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            Observable<Rooms> roomsObservable = apiService.listRooms();
            roomsObservable
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .onErrorResumeNext(x -> {
                        Log.d("AA", "no rooms fml");
                        Utils.showToast(getContext(), "Couldn't load rooms");
                        progressBar.setVisibility(View.GONE);
                    })
                    .onErrorReturn(throwable -> new Rooms())
                    .subscribe((Rooms rooms) -> {
                        progressBar.setVisibility(View.GONE);
                        for (Rooms.Room r : rooms.getRooms()) {
                            Log.d("AA", r.getName());
                        }
                        recyclerView.setAdapter(new MyRoomItemRecyclerViewAdapter(rooms, mListener));
                    });
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnRoomSelectedListener) {
            mListener = (OnRoomSelectedListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnRoomSelectedListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnRoomSelectedListener {
        // TODO: Update argument type and name
        void onRoomListFragmentInteraction(Rooms.Room item);
    }
}
