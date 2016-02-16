package flightcatch;


import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;

public class Database {
	
	public static void insert(String departure, String arrive, int price, String date){
		
		
		try {
			//CONNESSIONE
//			System.out.println("Connecting to database...");
			Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/flights","root","rotfl");
			//STATMENT
//			System.out.println("Creating statment...");
			PreparedStatement st = conn.prepareStatement("insert into flight"
					+ "(id,departure,arrive,price,date,log)"
					+ "values (NULL,?,?,?,?,?)");
			st.setString(1, departure);
			st.setString(2, arrive);
			st.setInt(3, price);
			st.setString(4, date);
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
			String log =  LocalDateTime.now().format(formatter);
			st.setString(5, log);
//			System.out.println("Executing query...");
			st.executeUpdate();
//			System.out.println("Inserted correctly!");
			conn.close();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public static void update(String departure, String arrive, int price, String date){
		try {
			Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/flights","root","rotfl");
			Statement st = conn.createStatement();
			
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM--yyyy HH:mm");
			String log =  LocalDateTime.now().format(formatter);
			
			String sql = "UPDATE flight SET price = '"+price+"' WHERE date = '"+date+"' AND arrive = '"+arrive
					+"' AND departure = '"+departure+"' AND log ='"+log+"'";
			st.executeUpdate(sql);
			conn.close();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static Flight[] show() {

		String s = "";
		Flight[] ss = null;
		int counter = 0;
		try {
			//System.out.println("Connecting to database...");
			Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/flights","root","rotfl");
			//STATMENT
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery("SELECT * FROM flight  ORDER BY price");
			while(rs.next())
				counter++;
			ss = new Flight[counter];
			counter = 0;
			rs = st.executeQuery("SELECT * FROM flight  ORDER BY price");
			while(rs.next()){
				String date = rs.getString("date");
				int price = rs.getInt("price");
				String departure = rs.getString("departure");
				String arrive = rs.getString("arrive");
				ss[counter] = new Flight(departure,arrive,price,date);
				counter++;
				
			}
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ss;
		
	}
	public static boolean isIN(String departure, String arrive, String date){
		Connection conn;
		try {
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/flights","root","rotfl");
			//STATMENT
			Statement st = conn.createStatement();
//			ResultSet rs = st.executeQuery("SELECT * FROM flight WHERE  arrive = 'barcellona'");
//			System.out.println("SELECT * FROM flight WHERE arrive = '"+arrive+"' AND date = '"+date+"'");
			ResultSet rs = st.executeQuery("SELECT * FROM flight WHERE arrive = '"+arrive+"' AND date = '"+date+"'"+
						" AND departure = '"+departure+"'");
			boolean ris = rs.next();
			conn.close();
			return ris;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return true;
		}
		
		
	}
	public static int getPrice(String departure, String arrive, String date){
		int p = 0;
		try{
			Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/flights","root","rotfl");
			String sql = "SELECT price FROM flight WHERE  arrive = ? and departure = ? AND date = ?";
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, arrive);
			st.setString(2, departure);
			st.setString(3, date);
			ResultSet rs = st.executeQuery();
			if (rs.next())
				p = rs.getInt("price");
			conn.close();
		}
		catch (SQLException e){
			e.printStackTrace();
		}
		return p;
		
	}
	public static Flight[] search(String departure, String arrive, String date){
		String st = "", sql = "";
		int count = 0,min = 10000;
		Flight[] f = null;
		
		sql = "SELECT * FROM flight WHERE departure LIKE ? AND arrive LIKE ? AND date LIKE ?";
		
		try {
			Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/flights","root","rotfl");
			PreparedStatement ps = conn.prepareStatement(sql);
			if (departure != null)
				ps.setString(1, departure);
			else
				ps.setString(1, "%");
			if (arrive != null)
				ps.setString(2, arrive);
			else
				ps.setString(2, "%");
			if (date != null)	{
				ps.setString(3, "%"+date);
//				System.out.println(date);
			}
			
			else
				ps.setString(3, "%");
//			ps.toString();
			ResultSet rs = ps.executeQuery();
			while (rs.next()){
				count++;
			}
			f = new Flight[count];
			rs = ps.executeQuery();
			count = 0;
			while (rs.next()){
				String dates = rs.getString("date");
				int price = rs.getInt("price");
				String departures = rs.getString("departure");
				String arrives = rs.getString("arrive");
				f[count] = new Flight(departures,arrives,price,dates);
				count++;
				
			}
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
		
		
		
		Utils.sort(f);
		return f;
	}
//	public static void Update(String departure, String arrive, String)
	/*public static void Update(int months, String departure, String arrive, String month, JTextPane console) throws BadLocationException{
		int i = 0;
		Log log = new Log(console);
		if (Integer.parseInt(month)<10)
			month = "0"+month;
		long start = System.currentTimeMillis();
		while (i<months){
		
		try {
			String url = Utils.createUrl(departure, arrive, month);
			String url_back = Utils.createUrl(arrive, departure, month);
			String s = "/home/leonardo/Scrivania/skyscann/AutoSave_3.htm";
			int[] day, day_back;
			int attempt = 0;
			do {
				Utils.downloadData(url);
				day = Utils.getPrice(s);
				attempt ++;
				if (Gui.isZero(day)){
					log.logConsole("Non è stato scaricato il file! Riprovo..");
				}
			}while(Gui.isZero(day));
			System.out.println("Grabbed price from departure of "+month+" month with "+attempt+ " attempt");
			log.logConsole("Grabbed price from "+departure+" to "+ arrive+" of "+month+" month with "+attempt+ " attempt");
			attempt = 0;
			do{
				Utils.downloadData(url_back);
				day_back = Utils.getPrice(s);
				attempt++;
				if (Gui.isZero(day_back)){
					log.logConsole("Non è stato scaricato il file! Riprovo..");
				}
			}while (Gui.isZero(day_back));
			System.out.println("Grabbed price from arrive of "+month+" month with " + attempt+ " attempt");
			log.logConsole("Grabbed price from "+arrive+" to "+ departure+" of "+month+" month with "+attempt+ " attempt");

			for (int j = 0; j<day.length;j++){
				java.sql.Date date = Utils.newDate(j+1, Integer.parseInt(month), 2016);
				if (day[j] != 0){
					if (!Database.isIN(departure,arrive, date)){
						Database.insert(departure,arrive,day[j],date);
					}
					else {
						if (day[j] == Database.getPrice(departure, arrive, date)){
							System.out.println("PREZZO UGUALE, NON AGGIORNO");
//							log.logConsole("Prezzo uguale, non aggiorno");
							//							log.getConsole().repaint();
						}
						else{
							Database.update(departure, arrive, day[j], date);
							System.out.println("Già presente, effettua update");
//							log.logConsole("Effettuo update");
							//							log.getConsole().repaint();
						}
					}
				}
				if (day_back[j] != 0){
					if (!Database.isIN(arrive,departure, date)){
						Database.insert(arrive, departure, day_back[j], date);
					}
					else{
						if (day_back[j] == Database.getPrice(arrive, departure, date)){
							System.out.println("PREZZO UGUALE, NON AGGIORNO");
//							log.logConsole("Prezzo uguale, non aggiorno");
							//							log.getConsole().repaint();
						}
						else{
							Database.update(arrive,departure, day_back[j], date);
							System.out.println("Già presente, effettua update");
//							log.logConsole("Effettuo update");
							//							log.getConsole().repaint();
						}
					}
				}
			}
			month = month.charAt(0)+""+(Integer.parseInt(month.charAt(1)+"")+1);
			i++;
			
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		}
		long end = System.currentTimeMillis();
		System.out.println("tempo trascorso: "+(end-start)+"ms");
		log.logConsole("tempo trascorso: "+(end-start)+"ms");
	
	}
	public static void updateAll(String month, int months, String[] dest, JTextPane console){
		for (int i = 0; i<dest.length;i++){
			try {
//				Update(months, "roma", dest[i],month, console );
			} catch (BadLocationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}*/
	///INUTILE
	public static int getMin(String[] s) {
		// TODO Auto-generated method stub
		return Integer.parseInt(s[s.length-1]);
	}
	
	public static Flight[] searchAR(String departure, String arrive, String date, int days){
		int min = 10000;
		ArrayList<Flight> arr = new ArrayList<Flight>();
		int temp_min, i = 0, k;
		
		Flight [] A = search(departure, arrive, date);
		Flight [] R = search(arrive, departure, date);
		Flight [] r_next = search(arrive, departure, Utils.nextMonth(date));
		PriceAR p = new PriceAR(A, R, r_next, days);
		A = p.getA();
		R = p.getR();
		for (i = 0; i<A.length;i++){
			if (A[i] != null && R[i] != null){
				temp_min = A[i].getPrice() +R[i].getPrice();
				if (temp_min < min){
					min = temp_min;
					arr.clear();
					arr.add(A[i]);
					arr.add(R[i]);
				}
				else if (temp_min == min){
					arr.add(A[i]);
					arr.add(R[i]);
				}
			}
		}
		Flight[] s = new Flight[arr.size()];	
		arr.toArray(s);
		return s;
	}
	
	
	
	
	
	
	
}
