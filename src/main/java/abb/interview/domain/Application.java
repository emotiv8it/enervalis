package abb.interview.domain;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import abb.interview.api.MaxPerDeviceAndGroup;
import abb.interview.api.MeasurementStatistics;
import abb.interview.api.TotalPerGroup;

public class Application {

    private final Measurements measurements = new Measurements();
    private final ObjectMapper objectMapper = new ObjectMapper(); // can be shared !
    private static final String filename = "measurements.json";
    
    
    // Business Activities
    
    /**
     * Return the loaded measurements
     * @return Measurements object wrapper
     */
    public MeasurementStatistics getMeasurements() {
    	return measurements;
    }
    
    
    /**
     * Load new measurements into the application memory via an input stream providing JSON content.
     * 
     * @param inStream {@link InputStream}
     * @return boolean indicating success or failure
     * @throws IOException general IO exception when parsing,loading or mapping of JSON input fails
     */
    public boolean loadMeasurements(InputStream inStream) throws IOException {
    	
    	if(inStream == null) 
    		throw new IOException(String.format("Failed to read null input stream"));
    	
    	try {
    		measurements.clear();
    		List<Measurement> input = objectMapper.readValue(inStream,new TypeReference<List<Measurement>>(){});
    		if(input !=null) {
    			measurements.putAll(
    					input.stream().collect(Collectors.toMap(Measurement::getKey,  // the key is the identifier for map
    													measurement -> {			  // generate a submap
    														List<Measurement> list = new ArrayList<>();
    														list.add(measurement);
    														return list;
    													},
    													(list1, list2) -> {
    							                            list1.addAll(list2); // Merge lists if same key
    							                            return list1;
    													}
													))
    					);
    			return true;    			
    		}else {
    			throw new IOException(String.format("Failed to load any measurements due to empty input stream"));
    		}
    	}catch(JsonParseException pe) {
    		throw new IOException(String.format("Failed to load any measurements due to JSON parsing error"),pe);
    	}catch(JsonMappingException me) {
    		throw new IOException(String.format("Failed to load any measurements due to JSON mapping error"),me);    		
    	}
    }
    
    
    // ENTRY POINT
    // -----------
    
    public static void main(String[] args) throws IOException {
    	System.out.println("Application Measurements");
    	
    	InputStream in = Application.class.getClassLoader().getResourceAsStream(filename);
    	if(in == null) throw new IOException(String.format("Failed to find file %s in classpath !",filename));
    	System.out.println(String.format("Measurement file %s found",filename));

    	
    	Application app = new Application();
    	app.loadMeasurements(in);
    	System.out.println(String.format("Measurement input from %s was loaded, containing %d entries",filename,app.getMeasurements().size()));
    	
    	System.out.println("1) get totals based on average power, per device group, each direction : ");
    	List<TotalPerGroup> totals = app.getMeasurements().getTotalsPerGroup();
		
    	for (TotalPerGroup total : totals) {
    		for (Entry<Direction, Double> entry : total.getPowerPerDirection().entrySet()) {
    			System.out.println(String.format("%s,\t %s,\t %.4f", total.getDeviceGroup(), entry.getKey(), entry.getValue()));
			}
		}
    	
    	System.out.println("2) get list of all devices with max power:");
    	List<MaxPerDeviceAndGroup> totals2 = app.getMeasurements().getMaxForAllDevices();
    	for (MaxPerDeviceAndGroup maxPerDeviceAndGroup : totals2) {
			System.out.println(maxPerDeviceAndGroup.toString());
		}
    }
}
