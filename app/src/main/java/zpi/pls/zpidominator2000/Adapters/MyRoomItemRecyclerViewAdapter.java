package zpi.pls.zpidominator2000.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import zpi.pls.zpidominator2000.Fragments.RoomsFragment.OnRoomSelectedListener;
import zpi.pls.zpidominator2000.Model.Rooms;
import zpi.pls.zpidominator2000.R;
import zpi.pls.zpidominator2000.dummy.DummyContent.DummyItem;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
 * specified {@link OnRoomSelectedListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyRoomItemRecyclerViewAdapter extends RecyclerView.Adapter<MyRoomItemRecyclerViewAdapter.ViewHolder> {

    private final List<Rooms.Room> mValues;
    private final OnRoomSelectedListener mListener;

    public MyRoomItemRecyclerViewAdapter(Rooms rooms, OnRoomSelectedListener listener) {
        mValues = rooms.getRooms();
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_roomitem, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mIdView.setText(String.valueOf(mValues.get(position).getRoomId() + 1));
        holder.mContentView.setText(mValues.get(position).getName());

        holder.mView.setOnClickListener(v -> {
            if (null != mListener) {
                // Notify the active callbacks interface (the activity, if the
                // fragment is attached to one) that an item has been selected.
                mListener.onRoomListFragmentInteraction(holder.mItem);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView;
        public final TextView mContentView;
        public Rooms.Room mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.id);
            mContentView = (TextView) view.findViewById(R.id.content);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
