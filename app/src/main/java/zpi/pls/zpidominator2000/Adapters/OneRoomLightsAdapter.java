package zpi.pls.zpidominator2000.Adapters;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;

import java.util.List;

import zpi.pls.zpidominator2000.Fragments.OneRoomSettingsFragment;
import zpi.pls.zpidominator2000.Model.Lights;
import zpi.pls.zpidominator2000.R;

/**
 * Created by wojciech.liebert on 14.05.2018.
 */
public class OneRoomLightsAdapter extends RecyclerView.Adapter<OneRoomLightsAdapter.ViewHolder> {

    private final List<Lights.Light> mValues;
    private final OneRoomSettingsFragment.OnFragmentInteractionListener mListener;

    public OneRoomLightsAdapter(Lights lights, OneRoomSettingsFragment.OnFragmentInteractionListener listener) {
        mValues = lights.getLights();
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.one_room_settings_light_vh, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Lights.Light light = mValues.get(position);
        holder.mItem = light;
        holder.mNameView.setText(light.getName());
        holder.mSwitch.setChecked(light.getState());
        holder.mSwitch.setOnCheckedChangeListener((theSwitch, newState) -> {
            Log.d("AAAAAAA", String.format("LIGHT %s SWITCH: %s", light.getName(), newState));
//            mListener.onFragmentInteraction(holder.mItem);
        });

        holder.mView.setOnClickListener(v -> {
            if (null != mListener) {
                // Notify the active callbacks interface (the activity, if the
                // fragment is attached to one) that an item has been selected.
                mListener.onFragmentInteraction(holder.mItem);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mNameView;
        public final Switch mSwitch;
        public Lights.Light mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mNameView = view.findViewById(R.id.one_room_light_name);
            mSwitch = view.findViewById(R.id.one_room_light_switch);
        }
    }
}