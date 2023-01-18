package codigos_Proceso;

import java.io.FileReader;

import org.json.JSONObject;
import org.json.simple.parser.JSONParser;

import io.javalin.Javalin;

/**
 * 
 * Esta clase obtiene el codigo ingresado, para buscarlo en el JSON
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
			String ruta="C:\\Users\\rojas\\Documents\\BDB\\json\\src\\main\\java\\json\\DiasHabiles.json";
			Object ob = new JSONParser().parse(new FileReader(ruta));
			//JSONObject js = (JSONObject) ob;

	        //String firstName = (String) js.get("0109");
	        String firstName = ob.toString();

	        
			ctx.result( firstName);

		});// Cierre del javalin
	}

} // Cierre de la clase
