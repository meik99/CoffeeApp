package tk.rynkbit.coffeealarm.entity;

/**
 * Created by michael on 10/30/17.
 */

public class Alarm {
    String id;
    String name;
    int hour;
    int minute;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public Alarm(String id, String name, int hour, int minute) {

        this.id = id;
        this.name = name;
        this.hour = hour;
        this.minute = minute;
    }

    public Alarm() {

    }
}
