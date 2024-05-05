package Elevator;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class elevatorOperator {

	private static final String FILE_PATH = "src/Configuration/elevator.properties";

    public static void main(String[] args) {
    	
    	
    	String filePath = FILE_PATH;
        Properties properties = new Properties();
        try (FileInputStream input = new FileInputStream(filePath)) {
            properties.load(input);
            int noOfFloors = Integer.parseInt(properties.getProperty("noOfFloors"));
            int noOfElevators = Integer.parseInt(properties.getProperty("noOfElevators"));
            
            Building building = new Building(noOfFloors, noOfElevators);
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }
        // Perform other actions related to the elevator system
        
    }



}
