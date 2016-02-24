package flightcatch;

public class Destinations {
	String min;
	String max;
	public final static String[] destinations = {"Dublino", "Berlino", "Amsterdam", "Parigi", "Barcellona", "Londra", 
			"Milano","Eindhoven", "Bruxelles", "Copenhagen", "Praga", "Oslo", "Los Angeles", "Istanbul", "New York", "Stoccolma", "Colonia"
	};
	
	public Destinations(String dest){
		this.min = getMin(dest);
		this.max = getMax(dest);
	}
	
	
	public static String getMax(String s){
		String max = "";
		switch (s.toLowerCase()){
		case "amsterdam":
			max = "amsterdam-schiphol";
			break;
		case "los angeles":
			max = "los-angeles";
			break;
		case "new york":
			max = "new-york";
			break;
		default :
			max = s.toLowerCase();
			break;
		}
		return max;
	}
	public static String getGoogleMin(String s){
		String min = "";
		switch (s.toLowerCase()){
		case "stoccolma":
			min  = "ARN,BMA,NYO";
			break;
		case "roma":
			min = "ROM";
			break;
		case "dublino":
			min = "DUB";
			break;
		case "berlino":
			min = "BER,TXL,SXF,QPP";
			break;
		case "amsterdam":
			min = "AMS,ZYA";
			break;
		case "parigi":
			min = "PAR";
			break;
		case "barcellona":
			min = "BCN,YJB";
			break;
		case "londra":
			min = "LON";
			break;
		case "milano":
			min = "MXP,LIN,BGY,IPR,XIK";
			break;
		case "eindhoven":
			min = "EIN";
			break;
		case "bruxelles":
			min = "BRU,CRL,ZYR";
			break;
		case "copenhagen":
			min = "CPH,ZGH";
			break;
		case "praga":
			min = "PRG,XYG";
			break;
		case "oslo":
			min = "OSL";
			break;
		case "los angeles":
			min = "LAX";
			break;
		case "istanbul":
			min = "IST,SAW";
			break;
		case "new york":
			min = "JFK,EWR,LGA";
			break;
		case "colonia":
			min = "CGN,QKL";
			break;
		
//		case "":
//			min = "";
//			break;
		}
		return min;
	}
	public static String getMin(String s){
		String min = "";
		switch (s.toLowerCase()){
		case "stoccolma":
			min  = "stoc";
			break;
		case "roma":
			min = "rome";
			break;
		case "dublino":
			min = "dub";
			break;
		case "berlino":
			min = "berl";
			break;
		case "amsterdam":
			min = "ams";
			break;
		case "parigi":
			min = "pari";
			break;
		case "barcellona":
			min = "bcn";
			break;
		case "londra":
			min = "lond";
			break;
		case "milano":
			min = "mila";
			break;
		case "eindhoven":
			min = "ein";
			break;
		case "bruxelles":
			min = "brus";
			break;
		case "copenhagen":
			min = "cph";
			break;
		case "praga":
			min = "prg";
			break;
		case "oslo":
			min = "oslo";
			break;
		case "los angeles":
			min = "laxa";
			break;
		case "istanbul":
			min = "ista";
			break;
		case "new york":
			min = "nyca";
			break;
		case "colonia":
			min = "colo";
			break;
		case "":
			min = "";
			break;
		}
		
		return min;
	}
		
}
