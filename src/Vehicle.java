import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

enum Type{
    MOTORCYCLE,
    CAR,
    TRUCK
}

public class Vehicle {
    private String id;
    private Type type;
    private Calendar parking_date_time = Calendar.getInstance();
    private Calendar exit_date_time = Calendar.getInstance();
    private boolean end_parking = false;

    final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

    public Vehicle(String id, String type, String date_time) throws ParseException {
        this.id = id;
        switch (type){
            case "TRUCK":
                this.type = Type.TRUCK;
                break;
            case "MOTORCYCLE":
                this.type = Type.MOTORCYCLE;
                break;
            case "CAR":
                this.type = Type.CAR;
                break;
        }
        parking_date_time.setTime(formatter.parse(date_time));


    }


    public Calendar getExit_data_time() {
        return exit_date_time;
    }

    public Calendar getParking_date_time() {
        return parking_date_time;
    }

    public String getId() {
        return id;
    }

    public Type getType() {
        return type;
    }

    public void setExit_data_time(String exit_date_time) throws ParseException {
        this.exit_date_time.setTime(formatter.parse(exit_date_time));
    }

    public boolean isEnd_parking() {
        return end_parking;
    }

    public void setEnd_parking(boolean end_parking) {
        this.end_parking = end_parking;
    }
}
