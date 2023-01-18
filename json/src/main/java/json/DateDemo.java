/**
 * 
 */
package json;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDateTime;
import java.io.BufferedReader;
import java.io.FileReader;
import java.net.*;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import io.javalin.http.ContentType;
import io.javalin.http.HttpCode;

import org.json.JSONArray;
import org.json.JSONObject;

import io.javalin.Javalin;
//import sun.dc.path.PathException;

/**
 * @author
 *
 */
public class DateDemo {

	static String fechaYHoraCalendarioTransaccion = null;
	static String fechaCalendarioTransaccion = null;
	static String horaCalendarioTransaccion = null;
	static String fechaCompensacionTransaccion = null;
	static final int ONE_UNIT_MORE = 1;
	static LocalDateTime fechaHoy = null;
	static Boolean esJornadaNormal = true; // En día hábil es 'true'.
	public static Boolean esDiaHabil = null; // AGREGAMOS 'PUBLIC'
	static final LocalTime FINAL_HOUR_FOR_NORMAL_JOURNEY = LocalTime.of(21, 00, 00);// Solamente aplica para día hábil
																					// (no sábados, ni domingos, ni
																					// festivos).
	static DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("MMddHHmmss");
	static DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HHmmss");
	static final Integer HORA_LIMITE_CIERRE = 0;
	static String estadoProceso = null;
	static String clasificacion;
	volatile static Boolean estaEnCierre = null;
	static Map<String, DatosHashmap> datosTransaccionesPrueba = new HashMap<>();

	public static void inicializarDatos() {
		// Datos validos MMddHHmmss
		fechaYHoraCalendarioTransaccion = "0106110000";
		fechaCalendarioTransaccion = fechaYHoraCalendarioTransaccion.substring(0, 4);
		horaCalendarioTransaccion = fechaYHoraCalendarioTransaccion.substring(4, 10);
		// Datos válidos MMdd
		fechaCompensacionTransaccion = "0106";
		// Fecha del sistema
		fechaHoy = LocalDateTime.now();
		estaEnCierre = false;
		// Tipo de jornada hábil o no hábil
		esJornadaNormal = calcularSiEsJornadaNormal();
		// Indica si se trata de un día hábil
		esDiaHabil = true;
		// Estado del proceso
		estadoProceso = "";
	}

	public static final void armarDatosTransaccionesPrueba() {

		for (int i = 0; i < 10; i++) {
			String llave = getRandomDate();
			Boolean esHabil = jsonurl("http://localhost:8000/FESTIVOS", llave);
			DatosHashmap datos = new DatosHashmap();
			datos.llave = llave;
			datos.strHoraNormal = getHoraAleatoria("JORNADA_NORMAL");
			datos.strFechaCalendario = llave + datos.strHoraNormal;
			datos.strFechaCompensacion = llave;
			datos.esCierre = false;
			datos.esDiaHabil = esHabil;
			datosTransaccionesPrueba.put(datos.strFechaCalendario, datos);

			datos = new DatosHashmap();
			datos.llave = llave;
			datos.strHoraCierre = getHoraAleatoria("JORNADA_CIERRE");
			datos.strFechaCalendarioCierre = llave + datos.strHoraCierre;
			datos.strFechaCompensacionCierre = llave;
			datos.esCierre = true;
			datos.esDiaHabil = esHabil;
			datosTransaccionesPrueba.put(datos.strFechaCalendarioCierre, datos);

			// System.out.println("llave"+llave);
			// System.out.println("datos.llave"+datos.llave);

		}

	}

	public static String getRandomDate() {
		Random rand = new Random();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();
		cal.set(2023, 1, 1);
		long start = cal.getTimeInMillis();
		cal.set(2023, 12, 31);
		long end = cal.getTimeInMillis();
		Date d = new Date(start + (long) (rand.nextDouble() * (end - start)));
		String mmdd = (format.format(d)).toString().replaceAll("-", "").substring(4);
		// System.out.println("a"+mmdd+"a");
		return mmdd;
	}

	public static Boolean jsonurl(String urljson, String fecha) {
		try {
			URL url = new URL(urljson);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "application/json");
			if (conn.getResponseCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
			}
			JSONObject json = new JSONObject(IOUtilities.readFullyAsString(conn.getInputStream()));
			// System.out.println("no habil" + json.get(fecha));
			conn.disconnect();
			return false;
		} catch (Exception e) {
			// TODO: handle exception
		}
		// System.out.println("habil " + fecha);
		return true;
	}

	public static JSONObject jsonurl2(String urljson) {
		try {
			URL url = new URL(urljson);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "application/json");
			if (conn.getResponseCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
			}

			JSONArray json = new JSONArray(IOUtilities.readFullyAsString(conn.getInputStream()));
			JSONObject jsonObject = null;
			for (int i = 0; i < json.length(); i++) {
				jsonObject = (JSONObject) json.get(i);
				String fechaCompensacion = (String) jsonObject.get("fechaCompensacion");
				String fechaTransaccion = (String) jsonObject.get("fechaTransaccion");
				String cierreActivo = (String) jsonObject.get("cierreActivo");
				String resultadoEsperado = (String) jsonObject.get("resultadoEsperado");
				String comentario = (String) jsonObject.get("comentario");
			}
			conn.disconnect();
			return jsonObject;
		} catch (Exception e) {
			System.out.println("error" + e.getMessage());
		}
		return null;
	}

	public static JSONArray mostrardatosprueba() {
		String string = null;
		String json = "";
		for (String llaveNavegacion : datosTransaccionesPrueba.keySet()) {
			DatosHashmap datos = datosTransaccionesPrueba.get(llaveNavegacion);
			StringBuilder data = new StringBuilder();
			string = null;

			DateDemo.esDiaHabil = datos.esDiaHabil;
			if (datos.esCierre) {
				DateDemo.fechaCompensacionTransaccion = datos.strFechaCompensacionCierre;
				DateDemo.fechaCalendarioTransaccion = datos.strFechaCalendarioCierre;
				DateDemo.esJornadaNormal = false;
			} else {
				DateDemo.fechaCompensacionTransaccion = datos.strFechaCompensacion;
				DateDemo.fechaCalendarioTransaccion = datos.strFechaCalendario;
				DateDemo.esJornadaNormal = true;
			}

			data // .append("[").append(llaveNavegacion ).append( "]->")
					.append("{\"fechaCompensacion\": \"").append(datos.llave).append("\",")
					.append(" \"fechaTransaccion\": \"").append(DateDemo.fechaCalendarioTransaccion).append("\",");

			clasificarTransaccionComoCurrentONextDay_v3(fechaCompensacionTransaccion, fechaCalendarioTransaccion,
					esJornadaNormal);
			String cierre = "NO";
			if (datos.esCierre) {
				cierre = "SI";
			}

			data.append(" \"cierreActivo\": \"" + cierre + "\"," + " \"resultadoEsperado\": \"" + clasificacion + "\","
					+ " \"comentario\": \"" + clasificacion + "\"},");
			json = json + data.toString();
			// System.out.println(data.toString());

		}
		// System.out.println(json.toString());
		JSONArray jsondatospruebav2 = new JSONArray("[" + json.substring(0, json.length() - 1) + "]");
		// System.out.println(jsondatospruebav2.toString());
		return jsondatospruebav2;

	}

	public static final String getHoraAleatoria(LocalTime initialTime, LocalTime finalTime) {
		Random rand = new Random();
		int hours = rand.nextInt((finalTime.getHour() - initialTime.getHour()) + ONE_UNIT_MORE) + initialTime.getHour();
		int minutes = rand.nextInt((finalTime.getMinute() - initialTime.getMinute()) + ONE_UNIT_MORE)
				+ initialTime.getMinute();
		int seconds = rand.nextInt((finalTime.getSecond() - initialTime.getSecond()) + ONE_UNIT_MORE)
				+ initialTime.getSecond();

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HHmmss");
		LocalTime random = LocalTime.of(hours, minutes, seconds);
		// System.out.println("hora " + random.format(formatter));
		return random.format(formatter);

	}

	public static String getHoraAleatoria(String rango) {
		String aleatorioHora = "";
		switch (rango) {
		case "JORNADA_NORMAL":
			aleatorioHora = getHoraAleatoria(LocalTime.of(03, 00, 00), LocalTime.of(20, 59, 59));
			// System.out.println("Hora Current" + aleatorioHora);
			break;
		case "JORNADA_CIERRE":
			aleatorioHora = getHoraAleatoria(LocalTime.of(21, 00, 00), LocalTime.of(23, 59, 59));
			break;
		}
		return aleatorioHora;
	}

	static class DatosHashmap {
		boolean esDiaHabil = true;
		boolean esCierre = false;
		String llave = "N/A";
		String strFechaCalendario = "N/A";
		String strFechaCompensacion = "N/A";
		String strFechaCalendarioCierre = "N/A";
		String strFechaCompensacionCierre = "N/A";
		String strHoraNormal = "N/A";
		String strHoraCierre = "N/A";
	}

	// Método que recorra los días de prueba, y varíe la llaveNavegacion de
	// compensación, la llaveNavegacion de la transacción, y si está en jornada
	// normal encender o apagar el booleano esJornadaNormal.

	/*
	 * private static Boolean calcularSiEsDiaHabil( LocalDate date ){ ; }
	 */

	private static Boolean calcularSiEsJornadaNormal() {
		if (estaEnCierre) {
			return false;
		} else {
			return true;
		}
	}

	public static String clasificarTransaccionComoCurrentONextDay_v2(String fechaCompensacionString,
			String fechaCalendarioString) {
		if (!fechaCompensacionString.equals(fechaCalendarioString.substring(0, 4)) || isClosedJourney()) {
			clasificacion = "NEXT_DAY";
		} else {
			clasificacion = "CURRENT";
		}
		return clasificacion;
	}

	public synchronized static Boolean isClosedJourney() {
		LocalTime transactionHourReceived = LocalTime.now();
		if (isWorkingDay()) {
			if (transactionHourReceived.isAfter(FINAL_HOUR_FOR_NORMAL_JOURNEY) && !estaEnCierre) {
				estaEnCierre = true;
			}
		}
		return estaEnCierre;
	}

	public static String clasificarTransaccionComoCurrentONextDay_v3(String fechaCompensacionString,
			String fechaCalendarioString, Boolean isClosedJourney) {
		if (!fechaCompensacionString.equals(fechaCalendarioString.substring(0, 4)) || !isClosedJourney) {
			clasificacion = "NEXT_DAY";
		} else {
			clasificacion = "CURRENT";
		}
		return clasificacion;
	}

	public static Boolean isWorkingDay() {
		// esDiaHabil = false; // OJO. ESTÁ QUEMADO.
		return esDiaHabil;
	}

	public static void apagarCierre() {
		estaEnCierre = false;
	}

	public static Object parse(String ruta) throws Exception {
		BufferedReader brBufferedReader = new BufferedReader(new FileReader(ruta));
		StringBuffer stringBuffer = new StringBuffer();
		String aux = null;
		while ((aux = brBufferedReader.readLine()) != null) {
			stringBuffer.append(aux);
		}
		// JSONObject js = new JSONObject(stringBuffer.toString());
		String json = stringBuffer.toString();
		// System.out.println(json);
		if (json.matches("[\\t ]*?\\[.*?")) {
			return new JSONArray(json);
		} else {
			return new JSONObject(json);
		}

	}

	public static void main(String args[]) {

		inicializarDatos();
		// armarDatosTransaccionesPrueba();

		Javalin app = Javalin.create().start(8000);

		// System.out.println(""+(new File(".")).getAbsolutePath());

		app.get("/{codigo}", ctx -> {
			String codigo = ctx.pathParam("codigo");
			try {
				if (codigo.contentEquals("DATOS_PRUEBA2")) {
					armarDatosTransaccionesPrueba();
					JSONArray datosprueba = mostrardatosprueba();
					ctx.status(HttpCode.OK).result(datosprueba.toString());
				} else {

					String ruta = ".\\json\\Configuraciones.json";

					JSONObject js = (JSONObject) parse(ruta);
					String firstName = (String) js.get(codigo);
					// ctx.result(firstName);

					String ruta2 = firstName;
					Object js2 = parse(ruta2);
					// ctx.result(js2.toString());
					ctx.status(HttpCode.OK).contentType(ContentType.JSON).result(js2.toString());
				}
			} catch (FileNotFoundException fileNotFoundException) {
				ctx.status(HttpCode.NOT_FOUND).contentType(ContentType.JSON).result("Archivo no encontrado.");
			}
			/*
			 * catch (PathException pathException){
			 * ctx.status(HttpCode.NOT_ACCEPTABLE).contentType(ContentType.JSON).result(
			 * "Ruta no encontrada." ); }
			 */
			catch (Exception exception) {
				ctx.status(HttpCode.INTERNAL_SERVER_ERROR).contentType(ContentType.JSON)
						.result("El archivo recuperado no se puede procesar.");
			}

		});// Cierre del javalin
		app.get("/{codigo}/{key}", ctx -> {
			String codigo = ctx.pathParam("codigo");
			String key = ctx.pathParam("key");
			try {
				String ruta = ".\\json\\Configuraciones.json";

				JSONObject js = (JSONObject) parse(ruta);
				String firstName = (String) js.get(codigo);

				String ruta2 = firstName;
				Object js2 = parse(ruta2);
				
				JSONObject jsonObject = (JSONObject) js2;
				String firstName2 = (String) jsonObject.get(key);
				String ruta3 = firstName2;
				
				ctx.status(HttpCode.OK).contentType(ContentType.JSON).result(ruta3);

			} catch (FileNotFoundException fileNotFoundException) {
				ctx.status(HttpCode.NOT_FOUND).contentType(ContentType.JSON).result("Archivo no encontrado.");
			}
			/*
			 * catch (PathException pathException){
			 * ctx.status(HttpCode.NOT_ACCEPTABLE).contentType(ContentType.JSON).result(
			 * "Ruta no encontrada." ); }
			 */
			catch (Exception exception) {
				ctx.status(HttpCode.INTERNAL_SERVER_ERROR).contentType(ContentType.JSON)
						.result("El archivo recuperado no se puede procesar.");
			}

		});// Cierre del javalin
	}
}
