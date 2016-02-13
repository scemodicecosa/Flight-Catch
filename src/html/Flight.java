package html;

public class Flight {
	private String departure;
	private String arrive;
	private int price;
	private String date;
	
	public Flight(String departure, String arrive, int price, String date){
		this.departure = departure;
		this.arrive = arrive;
		this.price = price;
		this.date = date;
	}
	public Flight (String s){
		String[] arr = s.split(" -> ");
		this.departure = arr[0];
		arr = arr[1].split(", ");
		this.arrive = arr[0];
		this.price = Integer.parseInt(arr[1].substring(0, arr[1].length()-1));
		this.date = arr[2];
	}
	public String getDeparture(){
		return departure;
	}
	public String getArrive(){
		return arrive;
	}
	public int getPrice(){
		return price;
	}
	public String getDate(){
		return date;
	}
	public String toString(){
		String s = departure+" -> "+arrive+", "+price+"€, "+date;
		return s;
	}
	public Object[] getObject(){
		return new Object[]{
				departure,arrive,price+" €",date
		};
	}
}
