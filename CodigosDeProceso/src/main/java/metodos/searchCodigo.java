package metodos;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class searchCodigo {
	/**
	 * metodo searchCodigo busca dentro del json el proceso y el codigo
	 * 
	 * @param codigo que se desea encontrar dentro del proceso
	 * @param proceso es el tipo de transaccion
	 * @param strJson llega todo el json 
	 * @return String del codigo encontrado, de lo contrario un vacio
	 * 
	 */	
	public String getsearchCodigo(String codigo, String proceso, String strJson) {
		try {
			JSONParser parser = new JSONParser();
			Object object = parser.parse(strJson);
			JSONObject mainJsonObject = (JSONObject) object;

			JSONArray jsonArrayCodigoProceso = (JSONArray) mainJsonObject.get(proceso);

			for (int i = 0; i < jsonArrayCodigoProceso.size();) {
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
}
