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
	 * 
	 * 
	 */
	public static void main(String[] args) {
		Javalin app = Javalin.create().start(8000);
		app.get("/:proceso/:codigo", ctx -> {

			String codigo = ctx.pathParam("codigo");
			String proceso = ctx.pathParam("proceso");

			String type=CodigoProceso(codigo,proceso);
			
			// muestro el resultado
			if (type != null && type != "") {
				ctx.result(proceso + ": " + codigo + " -  " + type);
			} else {
				ctx.result("No Existe el " + proceso + " con " + codigo);
			}

		});// Cierre del javalin
	}

	/**
	 * metodo Codigo de proceso, va la la direccion http y busca el codigo 
	 * 
	 * @param codigo  que se desea encontrar dentro del proceso
	 * @param proceso es el tipo de transaccion
	 * @param proceso es el tipo de transaccion
	 * 
	 */
	public static String CodigoProceso(String codigo, String proceso) {
		// busco el json en la direccion http
		jsonFromURL jsonFromURL = new jsonFromURL();
		String strJson = jsonFromURL.getJSONFromURL("http://localhost:8080/json/codigo_de_proceso_v1.json");

		// busco el codigo dentro del .json
		searchCodigo searchCodigo = new searchCodigo();
		String type = searchCodigo.getsearchCodigo(codigo, proceso, strJson);
		return type;

	}
} // Cierre de la clase
