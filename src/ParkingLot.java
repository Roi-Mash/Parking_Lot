import java.text.ParseException;
import java.util.*;
import java.util.regex.Pattern;

public class ParkingLot {

    private Hashtable<String,Vehicle> vehicles = new Hashtable<>();
    private int[] type_counter = new int[3];
    private int vehicles_at_night = 0;


    public ParkingLot(String[][] logs) throws ParseException {

        for(int i=0 ; i<logs.length ; i++){
            String id = logs[i][0];
            String type = logs[i][1];
            String date_time = logs[i][2];
            String action = logs[i][3];

            // Check if the log is valid and in order
            if(isLogValid(id,type,date_time,action))
            {
                Vehicle vehicle;
                // if the vehicle leaves, add exit date and time
                // and subtract it from the vehicle counter
                if(action.equals("EXIT")){
                    if(vehicles.containsKey(id)) {
                        this.vehicles.get(id).setExit_data_time(date_time);
                        this.vehicles.get(id).setEnd_parking(true);
                        type_counter[this.vehicles.get(id).getType().ordinal()]--;
                    }
                    else{
                        vehicles_at_night++;
                    }
                }

                // case of an entrance of a vehicle
                else {
                    vehicle = new Vehicle(id, type, date_time);
                    vehicles.put(id, vehicle);
                    type_counter[vehicle.getType().ordinal()]++;

                }
            }
        }
    }


    private boolean isLogValid(String id, String type, String date_time, String action){

        // Create patterns of the requested input
        Pattern id_pattern = Pattern.compile("^\\d+$");
        Pattern type_pattern = Pattern.compile("^(TRUCK|CAR|MOTORCYCLE)$");
        Pattern action_pattern = Pattern.compile("^(ENTRANCE|EXIT)$");
        //pattern is adapted to the parking lot's opening hours
        Pattern date_time_pattern = Pattern.compile("^\\d{4}-\\d{2}-\\d{2}T((0[6-9]|1[0-9]|2[0-2]):\\d{2}|23:[0-2]\\d):\\d{2}.\\d{3}Z$");

        return id_pattern.matcher(id).matches() &&
                type_pattern.matcher(type).matches() &&
                date_time_pattern.matcher(date_time).matches() &&
                action_pattern.matcher(action).matches();
    }

    public Hashtable<String, Vehicle> getVehicles() {
        return vehicles;
    }

    public int[] getTypeCounter() {
        return type_counter;
    }

    public int getVehicles_at_night(){
        return vehicles_at_night;
    }

}
