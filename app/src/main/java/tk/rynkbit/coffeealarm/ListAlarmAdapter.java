package tk.rynkbit.coffeealarm;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import tk.rynkbit.coffeealarm.entity.Alarm;

/**
 * Created by michael on 10/30/17.
 */

public class ListAlarmAdapter extends RecyclerView.Adapter<ListAlarmAdapter.AlarmViewHolder> {
    private final Context mContext;
    private final List<Alarm> mAlarms;

    public ListAlarmAdapter(Context context) {
        this.mContext = context;

        mAlarms = getAlarms();
    }

    private List<Alarm> getAlarms() {
        final List<Alarm> result = new LinkedList<>();
        Request<JSONArray> listAlarmRequest = new JsonArrayRequest(
                Request.Method.GET,
                "http://192.168.43.215:8080/api/coffee/",
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        for(int i = 0; i < response.length(); i++){
                            String id;
                            String name;
                            int hour;
                            int minute;

                            try {
                                JSONObject jsonObject = response.getJSONObject(i);
                                id = jsonObject.getString("_id");
                                name = jsonObject.getString("name");
                                hour = jsonObject.getInt("hour");
                                minute = jsonObject.getInt("minute");
                                result.add(new Alarm(id, name, hour, minute));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        ListAlarmAdapter.this.notifyDataSetChanged();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.err.println(error.getMessage());
                    }
                });
        RequestQueue queue = Volley.newRequestQueue(mContext);
        queue.add(listAlarmRequest);
        queue.start();

        return result;
    }

    @Override
    public AlarmViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_alarm, parent, false);
        AlarmViewHolder alarmViewHolder = new AlarmViewHolder(v);
        return alarmViewHolder;
    }

    @Override
    public void onBindViewHolder(AlarmViewHolder holder, int position) {
        if(position >= 0 && position < mAlarms.size()){
            Alarm alarm = mAlarms.get(position);
            holder.txtAlarmItemName.setText(alarm.getName());
            holder.txtAlarmItemTime.setText(
                    alarm.getHour() + ":" + alarm.getMinute()
            );
            holder.chkAlarmItemOnOff.setChecked(true);
        }
    }

    @Override
    public int getItemCount() {
        return mAlarms.size();
    }

    class AlarmViewHolder extends RecyclerView.ViewHolder {
        TextView txtAlarmItemName;
        TextView txtAlarmItemTime;
        Switch chkAlarmItemOnOff;

        public AlarmViewHolder(View itemView) {
            super(itemView);

            txtAlarmItemName = (TextView) itemView.findViewById(R.id.txtItemAlarmName);
            txtAlarmItemTime = (TextView) itemView.findViewById(R.id.txtItemAlarmTime);
            chkAlarmItemOnOff = (Switch) itemView.findViewById(R.id.chkItemAlarmOnOff);
        }
    }
}
