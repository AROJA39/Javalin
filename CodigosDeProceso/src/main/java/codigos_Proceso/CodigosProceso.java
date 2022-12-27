package codigos_Proceso;

import io.javalin.Javalin;
import metodos.jsonFromURL;
import metodos.searchCodigo;



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
	 * @param codigo que se desea encontrar dentro del proceso
	 * @param proceso es el tipo de transaccion
	 * 
	 */
	public static void main(String[] args) {
		Javalin app = Javalin.create().start(8000);
		app.get("/:proceso/:codigo", ctx -> {
			
			String codigo = ctx.pathParam("codigo");
			String proceso = ctx.pathParam("proceso");
			
			// busco el json en la direccion http
			jsonFromURL jsonFromURL = new jsonFromURL();
			String strJson = jsonFromURL.getJSONFromURL("http://localhost:8080/json/codigo_de_proceso_v1.json");
			
			//busco el codigo dentro del .json
			searchCodigo searchCodigo= new searchCodigo();
			String type = searchCodigo.getsearchCodigo(codigo, proceso, strJson);
			
			//muestro el resultado
			if (type != null && type != "") {
				ctx.result(proceso + ": " + codigo + " -  " + type);
			} else {
				ctx.result("No Existe el " + proceso + " con " + codigo);
			}

		});//Cierre del javalin
	}
} //Cierre de la clase
