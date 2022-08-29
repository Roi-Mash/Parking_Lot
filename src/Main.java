import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;


public class Main {

    public static void main(String[] args) throws ParseException {
    String[][]  logs =
            {

                    {"11111", "TRUCK", "2020-01-01T11:00:00.000Z","ENTRANCE" },
                    {"22222", "TRUCK", "2020-01-01T09:00:00.000Z","ENTRANCE" },
                    {"33333", "CAR", "2020-01-01T06:25:00.000Z","ENTRANCE" },
                    {"44444", "MOTORCYCLE", "2020-01-01T18:30:00.000Z","ENTRANCE" },
                    {"55555", "MOTORCYCLE", "2020-01-01T15:52:00.000Z","ENTRANCE" },

                     //Remain last night test
                    {"1000001", "TRUCK", "2020-01-01T07:00:00.000Z","EXIT" },
                    {"1000002", "TRUCK", "2020-01-01T09:00:00.000Z","EXIT" },

                    //Exit before X time by 1 minute
                    {"11111", "TRUCK", "2020-01-01T13:59:00.000Z","EXIT" },
                    {"33333", "CAR", "2020-01-01T08:24:00.000Z","EXIT" },
                    {"44444", "MOTORCYCLE", "2020-01-01T19:29:00.000Z","EXIT" }

////                     Exit after X time
//                    {"11111", "TRUCK", "2020-01-01T14:00:00.000Z","EXIT" },
//                    {"33333", "CAR", "2020-01-01T008:25:00.000Z","EXIT" },
//                    {"44444", "MOTORCYCLE", "2020-01-01T19:30:00.000Z","EXIT" }


            };

    VehiaclesOfEachType(logs);
    System.out.println("There are " + NumberOfVehicles(logs) + " vehicles that stay more than the time set for them");
    System.out.println("There are " + VehiclesAtNight(logs) + " vehicles that spent the night");
    System.out.println("The Busiest hours of the day were: " + BusiestHours(logs).toString());

    }


    /**
     *  Prints how many vehicles of each type there are in the parking lot
     */
    public static void VehiaclesOfEachType(String[][] logs) throws ParseException {
        ParkingLot parkinglot = new ParkingLot(logs);
        System.out.println("There are:\n"+parkinglot.getTypeCounter()[0]+" motorcycles"
                            +"\n"+parkinglot.getTypeCounter()[1]+" cars"
                            +"\n"+parkinglot.getTypeCounter()[2]+" trucks\nin the parking lot");


    }

    /**
     * Returns how many vehicles spent more than 'X' hours
     * Motorcycle : X = 1
     * Car : X = 2
     * Truck : X = 3
     */
    public static int NumberOfVehicles(String[][] logs) throws ParseException {
        int num_of_cars = 0;
        ParkingLot parkinglot = new ParkingLot(logs);

        Collection<Vehicle> vehicles = parkinglot.getVehicles().values();

        for(Vehicle vehicle : vehicles){

            // Case where the car left the parking lot
            if(vehicle.isEnd_parking()){
                int exit_time_by_hour = vehicle.getExit_data_time().get(Calendar.HOUR_OF_DAY);
                int entrance_time_by_hour = vehicle.getParking_date_time().get(Calendar.HOUR_OF_DAY);
                int parking_time_by_hours = exit_time_by_hour - entrance_time_by_hour;
                int exit_time_by_minute = vehicle.getExit_data_time().get(Calendar.MINUTE);
                int entrance_time_by_minute = vehicle.getParking_date_time().get(Calendar.MINUTE);
                int parking_time_by_minutes = exit_time_by_minute - entrance_time_by_minute;

                // x depend on the type of the vehicle
                int x = vehicle.getType().ordinal()+1;

                if(parking_time_by_hours > x || (parking_time_by_hours == x && parking_time_by_minutes >= 0)){
                    num_of_cars++;
                }
            }

            // Case where the car has not left the parking lot
            else{
                num_of_cars++;
            }
        }
        return num_of_cars;
    }

    /**
     * Returns how many vehicles has spent the last night in the parking lot
     */
    public static int VehiclesAtNight(String[][] logs) throws ParseException {
        ParkingLot parkingLot = new ParkingLot(logs);
        return parkingLot.getVehicles_at_night();
    }


    /**
     * Returns the busiest hours of the day using prefix sum technique
     */
    public static ArrayList<Integer> BusiestHours(String[][] logs) throws ParseException {
        ArrayList<Integer> busiest_hours = new ArrayList<>();
        ParkingLot parkinglot = new ParkingLot(logs);
        int[] cars_by_hour = new int[24+1];

        Collection<Vehicle> vehicles = parkinglot.getVehicles().values();
        for(Vehicle vehicle : vehicles) {
            int entrance_time_by_hour = vehicle.getParking_date_time().get(Calendar.HOUR_OF_DAY);
            int exit_time_by_hour;

            // if the vehicle never exit the parking lot, mark it as staying until the end of the day
            if(vehicle.isEnd_parking()) {
                exit_time_by_hour = vehicle.getExit_data_time().get(Calendar.HOUR_OF_DAY);
            }
            else {
                exit_time_by_hour = 23;
            }

            cars_by_hour[entrance_time_by_hour]++;
            cars_by_hour[exit_time_by_hour+1]--;
        }

        int max = cars_by_hour[0];
        for (int i = 0; i < cars_by_hour.length-1; i++) {
            cars_by_hour[i+1] += cars_by_hour[i];
            if(max < cars_by_hour[i+1]){
                max = cars_by_hour[i+1];
            }
        }

        for(int i = 0 ; i < cars_by_hour.length-1; i++){
            if(max == cars_by_hour[i]){
                busiest_hours.add(i);
            }
        }
        return busiest_hours;
    }
}
