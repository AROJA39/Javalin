package codigos_Proceso;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import metodos.jsonFromURL;

public class JsonParser {       
    public static void main(String[] args) {
    	jsonFromURL jsonFromURL = new jsonFromURL();
        String strJson = jsonFromURL.getJSONFromURL(
            "http://localhost:8080/json/codigo_de_proceso_v1.json"
        );
        try {
            JSONParser parser = new JSONParser();
            Object object = parser.parse(strJson);
            JSONObject mainJsonObject = (JSONObject) object;
             
            /*************** Phone Numbers ****************/
            JSONArray jsonArrayPhoneNumbers = (JSONArray) mainJsonObject.get("RETIROS_/_AVANCES");
            System.out.println("RETIROS_/_AVANCES : ");
            
            for (int i = 0; i < jsonArrayPhoneNumbers.size(); i++) {
                JSONObject jsonPhoneNumber = (JSONObject) jsonArrayPhoneNumbers.get(i);
                System.out.println("      RETIROS_/_AVANCES" + (i + 1));

                String type = (String) jsonPhoneNumber.get("011000");
                System.out.println("      011000 : " + type);                
            }
        }
        catch(Exception ex) {
            ex.printStackTrace();
        }
    }
}
