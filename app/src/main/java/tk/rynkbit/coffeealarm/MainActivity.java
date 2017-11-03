package tk.rynkbit.coffeealarm;

import android.app.Dialog;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class MainActivity extends AppCompatActivity{

    RecyclerView listAlarms;
    FloatingActionButton btnAlarmAdd;
    Button btnCoffeeNow;
    MenuItem menuMainRefresh;
    private ListAlarmAdapter listAlarmsAdapter;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        new MenuInflater(this).inflate(R.menu.main_menu, menu);

        menuMainRefresh = menu.getItem(0);
        menuMainRefresh.setOnMenuItemClickListener(getRefreshClickListener());

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listAlarmsAdapter = new ListAlarmAdapter(getApplicationContext());

        btnAlarmAdd = (FloatingActionButton) findViewById(R.id.btnAlarmAdd);
        btnCoffeeNow = (Button) findViewById(R.id.btnCoffeeNow);
        listAlarms = (RecyclerView) findViewById(R.id.listAlarm);
        listAlarms.setLayoutManager(
                new LinearLayoutManager(
                        getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        listAlarms.setAdapter(listAlarmsAdapter);

        btnAlarmAdd.setOnClickListener(getAddAlarmClickListener());
        btnCoffeeNow.setOnClickListener(getCoffeeNowClickListener());

    }

    private MenuItem.OnMenuItemClickListener getRefreshClickListener() {
        return new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                listAlarmsAdapter.updateAlarms();
                return true;
            }
        };
    }

    private View.OnClickListener getCoffeeNowClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
                queue.add(new StringRequest("http://192.168.43.215:8080/api/coffee/now",
                        null,
                        null));
                queue.start();
            }
        };
    }

    private View.OnClickListener getAddAlarmClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new AlarmDialog(MainActivity.this, listAlarmsAdapter);
                dialog.show();
            }
        };
    }
}
