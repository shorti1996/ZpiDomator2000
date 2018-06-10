package zpi.pls.zpidominator2000.Adapters;

import android.support.v7.widget.RecyclerView;
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

/**
 * {@link RecyclerView.Adapter} that can display a {@link zpi.pls.zpidominator2000.Model.Lights.Light}
 */
public class OneRoomLightsAdapter extends RecyclerView.Adapter<OneRoomLightsAdapter.ViewHolder> {

    private List<Lights.Light> mValues;
    private final OneRoomSettingsFragment.OnLightStateChangedListener mListener;

    public OneRoomLightsAdapter(Lights lights, OneRoomSettingsFragment.OnLightStateChangedListener listener) {
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
        holder.mSwitch.setOnClickListener(view -> mListener.onLightStateChanged(light, ((Switch) view).isChecked()));
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public void swapValues(Lights lights) {
        mValues = lights.getLights();
        notifyDataSetChanged();
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