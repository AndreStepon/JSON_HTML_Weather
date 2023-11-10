package lt.codeacademy.learm.orai;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpHelper {
	
	private static final String USER_AGENT = "Mozilla/5.0";
	
	public static String sendGET(URL obj) throws IOException {
		
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		con.setRequestMethod("GET");
		con.setRequestProperty("User-Agent", USER_AGENT);
		int responseCode = con.getResponseCode();
		System.out.println("GET Response Code :: " + responseCode);
		if (responseCode == HttpURLConnection.HTTP_OK) {
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
			
			return response.toString();
		} else {
			System.out.println("GET request did not work.");
		}
		return null;
	}
	
	public static String getURL() {
		String geturl = "https://api.meteo.lt/v1/stations/";
		return geturl;
		
	}

}
