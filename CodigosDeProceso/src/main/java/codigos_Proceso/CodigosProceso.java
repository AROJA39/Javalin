package codigos_Proceso;

import io.javalin.Javalin;
import metodos.jsonFromURL;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;  
import org.json.simple.parser.JSONParser;


public class CodigosProceso {
//ingresar en rl navegador http://localhost:8000/hello/011000
    public static void main(String[] args) {
        Javalin app = Javalin.create().start(8000);
        app.get("/hello/:name", ctx -> {
        	String valor = ctx.pathParam("name");
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
                
                for (int i = 0; i < jsonArrayPhoneNumbers.size(); i++) {
                    JSONObject jsonPhoneNumber = (JSONObject) jsonArrayPhoneNumbers.get(i);

                    String type = (String) jsonPhoneNumber.get(valor);
                    System.out.println(valor + type); 
                    if (type != null) {
                        ctx.result("RETIROS_/_AVANCES: 011000 - " + type);
                    } else {
                        ctx.result("No Existe");
                    }
                }
                
               
            }
            catch(Exception ex) {
                ex.printStackTrace();
            }
        });
    }
}
