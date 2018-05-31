package zpi.pls.zpidominator2000.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import zpi.pls.zpidominator2000.Adapters.MyRoomItemRecyclerViewAdapter;
import zpi.pls.zpidominator2000.Api.ZpiApiService;
import zpi.pls.zpidominator2000.Model.Rooms;
import zpi.pls.zpidominator2000.R;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_roomitem_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
//            recyclerView.setAdapter(new MyRoomItemRecyclerViewAdapter(DummyContent.ITEMS, mListener));
            Observable<Rooms> roomsObservable = apiService.listRooms();
            roomsObservable
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .doOnError(x -> {
                        Log.d("AA", x.getMessage());
                        Toast.makeText(getContext(), "Couldn't load rooms", Toast.LENGTH_SHORT).show();
                    })
                    .onErrorReturnItem(new Rooms())
                    .subscribe((Rooms rooms) -> {
                        for (Rooms.Room r : rooms.getRooms()) {
                            Log.d("AA", r.getName());
                        }
                        recyclerView.setAdapter(new MyRoomItemRecyclerViewAdapter(rooms, mListener));
                    });
        }
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
