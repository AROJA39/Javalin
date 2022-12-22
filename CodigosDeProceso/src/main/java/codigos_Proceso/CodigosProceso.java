package codigos_Proceso;

import io.javalin.Javalin;
import metodos.jsonFromURL;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 * 
 * Esta clase obtiene el proceso y codigo ingresado, para buscarlo en el JSON
 * 
 * @author: Adriana Rojas
 * @version: 22/12/2022
 * 
 */
public class CodigosProceso {

	/**
	 * ingresar en al navegador http://localhost:8000/DEPOSITOS_CUENTA/211000
	 * 
	 */
	public static void main(String[] args) {
		Javalin app = Javalin.create().start(8000);
		app.get("/:proceso/:codigo", ctx -> {
			String codigo = ctx.pathParam("codigo");
			String proceso = ctx.pathParam("proceso");
			jsonFromURL jsonFromURL = new jsonFromURL();
			String strJson = jsonFromURL.getJSONFromURL("http://localhost:8080/json/codigo_de_proceso_v1.json");
			String type = searchCodigo(codigo, proceso, strJson);
			if (type != null && type != "") {
				ctx.result(proceso + ": " + codigo + " -  " + type);
			} else {
				ctx.result("No Existe el " + proceso + " con " + codigo);
			}

		});//Cierre del javalin
	}

	/**
	 * metodo searchCodigo busca dentro del json el proceso y el codigo
	 * 
	 * @param codigo que se desea encontrar dentro del proceso
	 * @param proceso es el tipo de transaccion
	 * @param strJson llega todo el json 
	 * @return String del codigo encontrado, de lo contrario un vacio
	 * 
	 */
	public static String searchCodigo(String codigo, String proceso, String strJson) {
		try {
			JSONParser parser = new JSONParser();
			Object object = parser.parse(strJson);
			JSONObject mainJsonObject = (JSONObject) object;

			JSONArray jsonArrayCodigoProceso = (JSONArray) mainJsonObject.get(proceso);

			for (int i = 0; i < jsonArrayCodigoProceso.size(); i++) {
				JSONObject jsonCodigoProceso = (JSONObject) jsonArrayCodigoProceso.get(i);

				String type = (String) jsonCodigoProceso.get(codigo);
				System.out.println(codigo + " " + type);
				return type;
			}
		} catch (Exception ex) {
			System.out.println("Error" + ex.getMessage());
			return "";

		}
		return "";
	}//Cierre del método
} //Cierre de la clase
