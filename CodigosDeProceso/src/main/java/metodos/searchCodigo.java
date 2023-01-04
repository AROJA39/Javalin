package metodos;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class searchCodigo {
	/**
	 * metodo searchCodigo busca dentro del json el  codigo
	 * 
	 * @param codigo que se desea encontrar dentro del proceso
	 * @param strJson llega todo el json 
	 * @return String del codigo encontrado, de lo contrario un vacio
	 * 
	 */	
	public String getsearchCodigo(String codigo, String strJson) {
		try {
			JSONParser parser = new JSONParser();
			Object obj = parser.parse(strJson);			 
			JSONObject jsonObject = (JSONObject) obj;	 
			String type = (String) jsonObject.get(codigo);			
			return type;
		} catch (Exception ex) {
			System.out.println("Error" + ex.getMessage());
			return "";

		}
	}//Cierre del método
}
