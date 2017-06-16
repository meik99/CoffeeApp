package tk.rynkbit.coffeealarm;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity
        implements
            Response.ErrorListener,
            Response.Listener<String>,
            TimePickerDialog.OnTimeSetListener{
    private static final String URL = "http://10.0.0.4:5000/";

    private TextView txtCurrentTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtCurrentTime = (TextView) findViewById(R.id.txtCurrentTime);
        getCurrentTime();
    }

    private void getCurrentTime(){
        StringRequest request = new StringRequest(
                Request.Method.GET, URL, this, this
        );
        Volley.newRequestQueue(this).add(request);
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Toast
                .makeText(this, error.getLocalizedMessage(), Toast.LENGTH_SHORT)
                .show();
    }

    @Override
    public void onResponse(String response) {
        String time = response;

        if(time == null || time.equals("HH:MM")){
            time = "Not set";
        }

        txtCurrentTime.setText(time);
    }

    public void setTime(View view) {
        int hourOfDay;
        int minuteOfHour;
        Date date = new Date();
        TimePickerDialog timePickerDialog;
        Calendar calendar = Calendar.getInstance();

        calendar.setTime(date);
        hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        minuteOfHour = calendar.get(Calendar.MINUTE);

        timePickerDialog =
                new TimePickerDialog(this, this, hourOfDay, minuteOfHour, true);
        timePickerDialog.show();
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        JSONObject object = new JSONObject();
        try {
            object.put("time",
                    String.format(Locale.ENGLISH, "%2d:%2d", hourOfDay, minute)
                        .replace(" ", "0")
            );
            JsonObjectRequest request = new JsonObjectRequest(
                    Request.Method.POST, URL, object, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    getCurrentTime();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    getCurrentTime();
                }
            });
            Volley.newRequestQueue(this).add(request);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
