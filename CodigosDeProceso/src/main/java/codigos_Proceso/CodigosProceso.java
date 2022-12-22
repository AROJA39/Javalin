package codigos_Proceso;

import io.javalin.Javalin;
import metodos.jsonFromURL;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;  
import org.json.simple.parser.JSONParser;


public class CodigosProceso {
//ingresar en rl navegador http://localhost:8000/DEPOSITOS_CUENTA/211000
    public static void main(String[] args) {
        Javalin app = Javalin.create().start(8000);
        app.get("/:proceso/:codigo", ctx -> {
        	String codigo = ctx.pathParam("codigo");
        	String proceso = ctx.pathParam("proceso");
        	jsonFromURL jsonFromURL = new jsonFromURL();
            String strJson = jsonFromURL.getJSONFromURL(
                "http://localhost:8080/json/codigo_de_proceso_v1.json"
            );
            try {
                JSONParser parser = new JSONParser();
                Object object = parser.parse(strJson);
                JSONObject mainJsonObject = (JSONObject) object;
                 
                /*************** Phone Numbers ****************/
                JSONArray jsonArrayPhoneNumbers = (JSONArray) mainJsonObject.get(proceso);
                
                for (int i = 0; i < jsonArrayPhoneNumbers.size(); i++) {
                    JSONObject jsonPhoneNumber = (JSONObject) jsonArrayPhoneNumbers.get(i);

                    String type = (String) jsonPhoneNumber.get(codigo);
                    System.out.println(codigo+" " + type); 
                    if (type != null) {
                        ctx.result(proceso+": " +codigo+" -  "+ type);
                    } else {
                        ctx.result("No Existe el "+ proceso+" con " +codigo);
                    }
                }
            }
            catch(Exception ex) {
                ex.printStackTrace();
                ctx.result("No Existe el "+ proceso+" con " +codigo);
            }
        });
    }
}
