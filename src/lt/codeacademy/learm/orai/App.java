package lt.codeacademy.learm.orai;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

public class App {
	
	static List<Place> places;
	static Scanner sc = new Scanner(System.in);

	public static void main(String[] args) throws IOException, JsonSyntaxException {
		getPlaces();
		Place place = askForPlace();
		System.out.println(place.name);
		System.out.println(place.administrativeDivision);
		
		showForecast(place);
	
		
	}

	private static void showForecast(Place place) throws IOException {
		String str = String.format("https://api.meteo.lt/v1/places/%s/forecasts/long-term", place.code);
		URL url = new URL(str);
		String json = HttpHelper.sendGET(url);
		Gson gs = new Gson ();
		Forecast fr = gs.fromJson(json,  Forecast.class);
		fr.forecastTimestamps.stream().forEach(e -> System.out.println(e.airTemperature));
		printForecast(fr);
		
	}

	private static void printForecast(Forecast fr) {
		double minAir = fr.forecastTimestamps.stream()
				.map(e -> e.airTemperature)
				.min((x,y) -> x.compareTo(y)).get();
	
			System.out.println("Minimali temp: " + minAir);
		
		Double maxAir = fr.forecastTimestamps.stream()
				.map(e -> e.airTemperature)
				.max((x,y) -> x.compareTo(y)).get();
	
			System.out.println("Maksimali temp: " + maxAir);
			
		
						
		
		int stulpeliuSkaicius = fr.forecastTimestamps.size();
		int iki = (int) Math.ceil(maxAir);
		int nuo = (int) Math.floor(minAir);
		
	
		for (int i = iki; i > nuo; i--) {
			System.out.print(String.format("%5d|", i));
			for (int j = 0; j < stulpeliuSkaicius; j++) {
				char symbol = ' ';
				if (i == 0) symbol = '-';
				int temp = (int)Math.round(fr.forecastTimestamps.get(j).airTemperature);
				if (i == temp) symbol = '`';
					System.out.print(symbol);
					
					
			}
			System.out.println();
		}
	}

	private static Place askForPlace() {
		List<Place> filteredPlaces = getManyPlaces();
		
		while (true) {
			System.out.println("Iveskite pasirinktos vietoves numeri");
			rodytiNumeruotaSarasa(filteredPlaces);
			int numeris = getNumberFromList(filteredPlaces);
			return filteredPlaces.get(numeris);
		}
	}

	private static int getNumberFromList(List<Place> filteredPlaces) {
		while(true) {
		System.out.println("Iveskite pasirinktos vietoves numeri");
		String answer = sc.nextLine();
		try {
			int skaicius = Integer.parseInt(answer);
			if (skaicius < 0 || skaicius >+ filteredPlaces.size()) 
				continue;
			return skaicius;
		} catch (Exception e) {
			System.out.println("bandykite darkart. Iveskite psirinktos vietoves numeri.");
			}
	}	
	}

	private static void rodytiNumeruotaSarasa(List<Place> filteredPlaces) {
		System.out.println("---------------------------------");
		for(int i = 0; i < filteredPlaces.size(); i++) {
			System.out.println(i + " ; " + filteredPlaces.get(i).name);
		}
		System.out.println("---------------------------------");
	}

	private static List <Place> getManyPlaces() {
		while(true) {
			System.out.println("Iveskite ieskomos vietoves pavadinima");
			
			String pavadinimas = sc.nextLine();
			
			List <Place> sarasas = getFilteredPlaces(pavadinimas);
			
			if(!sarasas.isEmpty())
				return sarasas;
			
		}
		
	}

	private static List<Place> getFilteredPlaces(String pavadinimas) {

		return places.stream()
				.filter(( p -> p.name.contains(pavadinimas)))
				.toList();
	}

	private static void getPlaces() throws MalformedURLException, IOException {
		URL placeUrl = new URL("https://api.meteo.lt/v1/places");
		String jsonPlaces = HttpHelper.sendGET(placeUrl);
		
		Gson gs = new Gson();
		places = List.of(gs.fromJson(jsonPlaces, Place[].class));
		System.out.println(places.size());
	}

}
