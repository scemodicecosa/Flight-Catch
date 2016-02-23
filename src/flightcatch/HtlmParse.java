package flightcatch;

import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.File;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import com.teamdev.jxbrowser.chromium.Browser;
import com.teamdev.jxbrowser.chromium.LoggerProvider;
import com.teamdev.jxbrowser.chromium.dom.By;
import com.teamdev.jxbrowser.chromium.dom.DOMDocument;
import com.teamdev.jxbrowser.chromium.dom.DOMElement;
import com.teamdev.jxbrowser.chromium.events.FailLoadingEvent;
import com.teamdev.jxbrowser.chromium.events.FinishLoadingEvent;
import com.teamdev.jxbrowser.chromium.events.FrameLoadEvent;
import com.teamdev.jxbrowser.chromium.events.LoadAdapter;
import com.teamdev.jxbrowser.chromium.events.LoadEvent;
import com.teamdev.jxbrowser.chromium.events.LoadListener;
import com.teamdev.jxbrowser.chromium.events.ProvisionalLoadingEvent;
import com.teamdev.jxbrowser.chromium.events.StartLoadingEvent;
import com.teamdev.jxbrowser.chromium.swing.BrowserView;

import javax.swing.*;
import java.awt.*;

import com.teamdev.jxbrowser.chromium.swing.BrowserView;

import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class HtlmParse {
	public static void main(String[] safsda){
		Database.update("roma", "barcellona", 47, Utils.stringDate(1, 3, 2016));
//		System.out.println(Calendar.month.values()[2]);
//		Flight f = new Flight("londra", "roma", 34, "2016-01-02");
//		String s = "londra -> roma, 35€, 2016-02-01";
//		Flight f2 = new Flight(s);
//		LaunchB();
//		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
//		String log =  LocalDateTime.now().format(formatter);
//		System.out.println(log);
//		Database.insert("amsterdam", "roma", 10, "oggi");
		/*try {
			
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/flights","root","rotfl");
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery("select * from flight");
			while (rs.next())
				System.out.println(rs.getString("departure")+" "+rs.getString("arrive")+" "+rs.getInt("price")+" "+rs.getString("date"));
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println(Utils.createGoogleURL("Roma", "amsterdam", "03"));
		System.out.println(Utils.stringDate(1, 4, 2016));
		int j = 10;
		int i = 10;
		boolean b = false;
		if (b = i == j){
			System.out.println("yea");
		}
		else
			System.out.println("not yea");
//		System.out.println(f2.toString());
		java.util.logging.Logger.getLogger("com.gargoylesoftware").setLevel(java.util.logging.Level.OFF);*/
	}

	public static void LaunchB(){
		LoggerProvider.getChromiumProcessLogger().setLevel(Level.OFF);
		LoggerProvider.getIPCLogger().setLevel(Level.OFF);
		LoggerProvider.getBrowserLogger().setLevel(Level.OFF);
		Browser browser = new Browser();
		BrowserView browserView = new BrowserView(browser);
		JFrame frame = new JFrame("hello world");
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.add(browserView, BorderLayout.CENTER);
        frame.setSize(600, 600);
        frame.setVisible(true);
        
//        browser.loadURL("https://www.google.it/flights/#search;f=FCO,CIA,IRT,XRJ;t=MXP,LIN,BGY,IPR,XIK;d=2016-02-28;r=2016-03-03;tt=o");
		browser.loadURL(Utils.createUrl("Roma", "Berlino", "04"));/*"http://www.skyscanner.it/trasporti/voli/rome/ams/voli-economici-da-roma-per-amsterda"
				+ "m-schiphol.html?adults=1&children=0&infants=0&cabinclass=economy&rtn=0&preferdirects=fal"
				+ "se&outboundaltsenabled=false&inboundaltsenabled=false&oym=1603");
        */
        browser.addLoadListener(new LoadAdapter(){
        	@Override
        	public void onFinishLoadingFrame(FinishLoadingEvent event){
        		if (event.isMainFrame()) {
                    Browser browser = event.getBrowser();
                    DOMDocument document = browser.getDocument();
                    System.out.println("document = " + document);
//                    DOMDocument document = browser.getDocument();
                    System.out.println("FINITO DI CARICARE");
                    List<DOMElement> list = document.findElements(By.className("next"));

                    list.get(0).click();
                    list.get(0).click();
//                  
                    for (int i = 0; i<0;i++){
                    	try {
                    		Thread.sleep(500);
                    	} catch (InterruptedException e) {
                    		// TODO Auto-generated catch block
                    		e.printStackTrace();
                    	}
                    	System.out.print(document.findElement(By.className("current")).getTextContent());
                    	System.out.println(document.findElement(By.className("dates")).getTextContent().split(" ")[1]);
                    	System.out.println("MESE "+ (i+3));
                    	List<DOMElement> price = document.findElements(By.className("primary "));
                    	System.out.println(price.size());
                    	for (DOMElement l:price)
                    		System.out.println(l.getTextContent().split("€ ")[1]+ " €");
                    	list.get(1).click();
                    }
                    
                    System.out.println();
                    System.out.println("CARICO.....");
                }
        	}
        });
	}
	
	
	
	
	
	
	
	
	
	public static int[][] getPriceReturn(String path){
		int [][] day = new int [31][2];
		try{
			File input = new File("/home/leonardo/Scrivania/skyscann/AutoSave_3.htm");
			Document doc = Jsoup.parse(input, "UTF-8", "http://example.com/");
			Elements elementi_div = doc.getElementsByTag("div");
			for (Element e: elementi_div){
				if (e.text().length()>0)
					if (/*isDecimal(e.text())*/ Character.isDigit(e.text().charAt(0)) && e.text().contains("€ ")){
						String[] arr = e.text().split(" ");
						day[Integer.parseInt(arr[0])-1][1] = Integer.parseInt(arr[2]);
					}
			}
			
		}
		catch(Exception e){
			System.out.println(e);
		}

		return day;
	}
	public static int[][] getPrice(String path){
		int [][] day = new int [31][2];
		try{
			File input = new File("/home/leonardo/Scrivania/skyscann/AutoSave_3.htm");
			Document doc = Jsoup.parse(input, "UTF-8", "http://example.com/");
			Elements elementi_div = doc.getElementsByTag("div");
			for (Element e: elementi_div){
				if (e.text().length()>0)
					if (/*isDecimal(e.text())*/ Character.isDigit(e.text().charAt(0)) && e.text().contains("€ ")){
						String[] arr = e.text().split(" ");
						day[Integer.parseInt(arr[0])-1][0] = Integer.parseInt(arr[2]);
					}
			}
			//for (int[] i :day)
				//System.out.println(i[0]+"-"+i[1]);
			
		}
		catch(Exception e){
			System.out.println(e);
		}

		return day;
	}
	public static boolean isDecimal(String s){
		if (s.length()>2)
		for (int i = 2; i<s.length();i++){
			if (!Character.isDigit(s.charAt(i)))
				return false;
		}
		return true;
	}
	public static void downloadData(String url){
		try{
		File file = new File ("/home/leonardo/Scrivania/skyscann/");
    	FileUtils.cleanDirectory(file);
    	//String url = "http://www.skyscanner.it/trasporti/voli/rome/dub/voli-economici-da-roma-per-dublinoa.html?adults=1&children=0&infants=0&cabinclass=economy&rtn=0&preferdirects=false&outboundaltsenabled=false&inboundaltsenabled=false&oym=1605";
    	Runtime runtime = Runtime.getRuntime();
    	Process p = runtime.exec("/usr/bin/firefox -new-window " + url);
    	Robot r = new Robot();
    	//r.delay(3000);
    	File f = new File ( "/home/leonardo/Scrivania/skyscann/AutoSave_4.htm");
    	while (!f.exists())
    	{
    		//System.out.println("il file non c'e'");
    	}
    	
//    	p.destroy();  
    	r.keyPress(KeyEvent.VK_ALT);
    	r.keyPress(KeyEvent.VK_F4);
    	r.keyRelease(KeyEvent.VK_ALT);
    	r.keyRelease(KeyEvent.VK_F4);
		} catch (Exception e){
			System.out.println(e);
		}
	}
}
