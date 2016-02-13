package html;

import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.util.Calendar;

import javax.swing.JTextField;

import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Utils {
	
	///COMPLETAMENTE INUTILE
	public static void downloadData(String url){
		try{
		File file = new File ("/home/leonardo/Scrivania/skyscann/");
    	FileUtils.cleanDirectory(file);
    	Runtime runtime = Runtime.getRuntime();
    	runtime.exec("/usr/bin/firefox -new-window " + url);
    	Robot r = new Robot();
    	File f = new File ( "/home/leonardo/Scrivania/skyscann/AutoSave_4.htm");
    	while (!f.exists())
    	{
    		//System.out.println("il file non c'e'");
    	}  
    	r.keyPress(KeyEvent.VK_ALT);
    	r.keyPress(KeyEvent.VK_F4);
    	r.keyRelease(KeyEvent.VK_ALT);
    	r.keyRelease(KeyEvent.VK_F4);
		} catch (Exception e){
			System.out.println(e);
		}
	}
	
	////COMPLETAMENTE INUTILE
	public static int[] getPrice(String path){
		int [] month = new int [31];
		int count = 0;
		try{
			File input = new File(path);
			Document doc = Jsoup.parse(input, "UTF-8", "http://example.com/");
			Elements elementi_div = doc.getElementsByTag("div");
			for (Element e: elementi_div){
				if (e.text().length()>0)
					if (Character.isDigit(e.text().charAt(0)) && e.text().contains("€ ")){
						count++;
						String[] arr = e.text().split(" ");
						month[Integer.parseInt(arr[0])-1] = Integer.parseInt(arr[2].replace(".", ""));
					}
			}
		}
		catch(Exception e){
			System.out.println(e);
		}
		if (count == 0){
			System.out.println("Non e' stato scaricato il file");
			//getPrice(path);
		}

		return month;
	}
	
	
	public static String createUrl(String departure, String arrive, String month){
		Destinations arr = new Destinations(arrive);
		Destinations dep = new Destinations(departure);
		String s = "http://www.skyscanner.it/trasporti/voli/"+dep.min+"/"+arr.min+"/voli-economici-da-"
				+dep.max+"-per-"+arr.max+".html?adults=1&children=0&infants=0&cabinclass=economy&rtn=0"
						+ "&preferdirects=false&outboundaltsenabled=false&inboundaltsenabled=false&oym=16"+month;
		
		
		return s;
	}
	
	///INUTILE
	public static Date newDate(int day, int month, int year){
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.MONTH, month-1);
		cal.set(Calendar.DAY_OF_MONTH, day);
		java.util.Date utildate = cal.getTime();
		java.sql.Date date = new java.sql.Date(utildate.getTime());
		return date;
		
	}
	
	///INUTILE
	public static Date newDate(String d){
		String[] s = d.split("-");
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, Integer.parseInt(s[0]));
		cal.set(Calendar.MONTH, Integer.parseInt(s[1])-1);
		cal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(s[2]));
		java.util.Date utildate = cal.getTime();
		java.sql.Date date = new java.sql.Date(utildate.getTime());
		return date;
		
	}
	
	public static String toUrlDate(String date){
		String[] s = date.split("-");
		String da = s[2].substring(2, 4)+s[1]+s[0];
		return da;
		
	}
	
	public static String getSingleUrl(String departure, String arrive, String date){
		Destinations arr = new Destinations(arrive);
		Destinations dep = new Destinations(departure);
		String s = "http://www.skyscanner.it/trasporti/voli/"
				+ dep.min+"/"+arr.min + "/"+toUrlDate(date)
				+ "/adults=1&children=0&infants=0&cabinclass=economy&rtn=0&preferdirects=false&out"
				+ "boundaltsenabled=false&inboundaltsenabled=false#results";
		return s;
		
	}
	
	
	///COMPLETAMENTE INUTILE
	public static void openPage(String url){
		Runtime runtime = Runtime.getRuntime();
    	try {
			runtime.exec("/usr/bin/firefox -new-window " + url);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public static int extractPrice(String s){
		if (s.length()<5)
			return 0;
		return Integer.parseInt(s.substring(s.indexOf(", ")+2, s.indexOf("€,")));
	}
	
	public static String nextMonth(String s){
		if (s.length()!=7)
			return s;
		else{
			String ss[] = s.split("-");
			String res = ss[0]+"-";
			int i = Integer.parseInt(ss[1]);
			i++;
			if (i<10)
				res+="0"+i;
			else
				res+=i;
			return res;
		}
		
		
		
	}
	public static void sort(Flight[] s){
		int i, t;
		Flight temp;
		for (t = 0; t<s.length; t++){
			i = getLowerIndex(t,s);
			temp = s[t];
			s[t] = s[i];
			s[i] = temp;
			
		}
		
	}
	public static int getLowerIndex(int t, Flight[] s){
		int min = 100000, i, ris = 0;
		for (i = t; i<s.length;i++){
			int temp = Integer.parseInt(s[i].getDate().split("-")[2]);
			if (temp<min){
				ris = i;
				min = temp;
			}
		}
		return ris;
	}
	public static String stringDate(int day, int month, int year){
		String s = "";
		if (day<10)
			s+=("0"+day);
		else
			s+=day;
		s+= "-";
		if (month<10)
			s+=("0"+month);
		else
			s+=month;
		s+= "-";
		
		s+=year;
		
		return s;
		
	}
	
}