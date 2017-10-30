package tk.rynkbit.coffeealarm;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import tk.rynkbit.coffeealarm.entity.Alarm;

public class MainActivity extends AppCompatActivity{

    RecyclerView listAlarms;
    Button btnAlarmAdd;
    private ListAlarmAdapter listAlarmsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listAlarmsAdapter = new ListAlarmAdapter(getApplicationContext());

        btnAlarmAdd = (Button) findViewById(R.id.btnAlarmAdd);
        listAlarms = (RecyclerView) findViewById(R.id.listAlarm);
        listAlarms.setLayoutManager(
                new LinearLayoutManager(
                        getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        listAlarms.setAdapter(listAlarmsAdapter);

        btnAlarmAdd.setOnClickListener(getAddAlarmClickListener());
    }

    private View.OnClickListener getAddAlarmClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new AlarmDialog(MainActivity.this);
                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        listAlarmsAdapter.updateAlarms();
                    }
                });
                dialog.show();
            }
        };
    }
}
