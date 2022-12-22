package metodos;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

/**
 * 
 * Esta clase obtiene el Json en URL y lo convierte en String
 * 
 * @author: Adriana Rojas
 * @version: 22/12/2022
 * 
 */
public class jsonFromURL {
	/**
	 * metodo getJSONFromURL
	 * 
	 * @param strUrl
	 * @return String
	 * 
	 */
	public String getJSONFromURL(String strUrl) {
		String jsonText = "";

		try {
			URL url = new URL(strUrl);
			InputStream is = url.openStream();
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));

			String line;
			while ((line = bufferedReader.readLine()) != null) {
				jsonText += line + "\n";
			}

			is.close();
			bufferedReader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonText;
	}
}//Cierre del método
