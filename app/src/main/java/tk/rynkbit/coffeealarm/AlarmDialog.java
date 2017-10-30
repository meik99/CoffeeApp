package tk.rynkbit.coffeealarm;

import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by michael on 10/30/17.
 */

public class AlarmDialog extends Dialog implements View.OnClickListener {
    private final ListAlarmAdapter listAlarmsAdapter;
    private EditText txtDialogAlarmName;
    private Button btnDialogAlarmCancel;
    private Button btnDialogAlarmConfirm;
    private TimePicker timeDialogAlarmTime;

    public AlarmDialog(@NonNull Context context, ListAlarmAdapter listAlarmsAdapter) {
        super(context);

        setContentView(R.layout.dialog_alarm);
        this.listAlarmsAdapter = listAlarmsAdapter;

        txtDialogAlarmName = (EditText) findViewById(R.id.txtDialogAlarmName);
        btnDialogAlarmConfirm = (Button) findViewById(R.id.btnDialogAlarmConfirm);
        btnDialogAlarmCancel = (Button) findViewById(R.id.btnDialogAlarmCancel);
        timeDialogAlarmTime = (TimePicker) findViewById(R.id.timeDialogAlarmTime);

        timeDialogAlarmTime.setIs24HourView(true);

        btnDialogAlarmConfirm.setOnClickListener(this);
        btnDialogAlarmCancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view == btnDialogAlarmCancel){
            this.dismiss();
        }else if(view == btnDialogAlarmConfirm) {
            String name = txtDialogAlarmName.getText().toString();
            int hour = 0;
            int minute;

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                hour = timeDialogAlarmTime.getHour();
                minute = timeDialogAlarmTime.getMinute();
            } else {
                hour = timeDialogAlarmTime.getCurrentHour();
                minute = timeDialogAlarmTime.getCurrentMinute();
            }

            //Workaround
            //Volley not sending 0 correctly
            if(hour == 0){
                hour = 23;
                minute = 59;
            }
            if(minute <= 0){
                minute = 1;
            }

            JSONObject jsonObject = new JSONObject();
            try {
                if(name != null && name.isEmpty() == false){
                    jsonObject.put("name", name);
                }
                jsonObject.put("hour", hour);
                jsonObject.put("minute", minute);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            System.out.println(jsonObject);
            postAlarm(jsonObject);

            this.dismiss();
        }
    }

    private void postAlarm(JSONObject alarm) {
        RequestQueue queue = Volley.newRequestQueue(getContext());
        Request request = new JsonObjectRequest(
                Request.Method.POST,
                "http://192.168.43.215:8080/api/coffee/",
                alarm,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println(response);
                        listAlarmsAdapter.updateAlarms();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.err.println(error.getMessage());
                        listAlarmsAdapter.updateAlarms();
                    }
                }
        );
        queue.add(request);
        queue.start();
    }
}
