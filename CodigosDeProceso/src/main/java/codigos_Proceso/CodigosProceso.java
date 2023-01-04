package codigos_Proceso;

import io.javalin.Javalin;
import metodos.jsonFromURL;
import metodos.searchCodigo;

/**
 * 
 * Esta clase obtiene el  codigo ingresado, para buscarlo en el JSON
 * 
 * @author: Adriana Rojas
 * @version: 22/12/2022
 * 
 */
public class CodigosProceso {

	/**
	 * ingresar en al navegador http://localhost:8000/141000
	 * 
	 * 
	 */
	public static void main(String[] args) {
		Javalin app = Javalin.create().start(8000);
		app.get("/:codigo", ctx -> {

			String codigo = ctx.pathParam("codigo");
			//String proceso = ctx.pathParam("proceso");

			CodigosProceso codigosProceso= new CodigosProceso();			
			String type=codigosProceso.codigoProceso(codigo);
			
			// muestro el resultado
			if (type != null && type != "") {
				ctx.result( codigo + " -  " + type);
			} else {
				ctx.result("No Existe el " + codigo);
			}

		});// Cierre del javalin
	}

	/**
	 * metodo Codigo de proceso, va la la direccion http y busca el codigo 
	 * 
	 * @param codigo  que se desea encontrar dentro del proceso
	 * 
	 */
	public  String codigoProceso(String codigo) {
		// busco el json en la direccion http
		jsonFromURL jsonFromURL = new jsonFromURL();
		String strJson = jsonFromURL.getJSONFromURL("http://localhost:8080/json/codigo_de_proceso_v1.json");

		// busco el codigo dentro del .json
		searchCodigo searchCodigo = new searchCodigo();
		String type = searchCodigo.getsearchCodigo(codigo, strJson);
		return type;

	}
} // Cierre de la clase
