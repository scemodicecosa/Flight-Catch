package html;

import java.awt.BorderLayout;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;

import javax.swing.*;

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



public class WebBrowser extends JFrame {
	DOMDocument document;
	boolean collect, finish = false;
	LinkedList<LinkedList> prices;
	public static Browser browser;
	String departure, arrive, month;
	BrowserView browserView;
	
	public WebBrowser(String departure, String arrive, String month){
//		infoBox("","");
		this.departure = departure;
		this.arrive = arrive;
		this.month = month;
		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		LoggerProvider.getChromiumProcessLogger().setLevel(Level.OFF);
		LoggerProvider.getIPCLogger().setLevel(Level.OFF);
		LoggerProvider.getBrowserLogger().setLevel(Level.OFF);
		browser = new Browser();
		browserView = new BrowserView(browser);
		
        add(browserView, BorderLayout.CENTER);
        setBounds(300, 300, 600, 600);
        
        setVisible(true);
		browser.loadURL(Utils.createUrl(departure, arrive, month));
		browser.addLoadListener(new LoadAdapter(){
	        	@Override
	        	public void onFinishLoadingFrame(FinishLoadingEvent event){
	        		if (event.isMainFrame()) {
	        				browser = event.getBrowser();
	        				document = browser.getDocument();
	        				System.out.println("loaded");
	        				List<DOMElement> ne = document.findElements(By.className("table-price small"));
	        				System.out.println("price getted");
	        				//controllo se il browser usa la griglia o il grafico
	        				if (ne.size()>0){
	        					System.out.println("[i]- Griglia");
	        					prices = getPriceGrid();
	        				}
	        				else{
	        					System.out.println("[i]- Grafico");
	        					browser.reload();
	        					try {
									Thread.sleep(2000);
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
	        					//prices = getPriceGraphic();
	        				}
	        				
//	        				for ()
	        				
	        				int i = Integer.parseInt(month);
	        				
	        				for (LinkedList<Integer> l: prices){
	        					int day = 1;
	        					System.out.println("Month"+ i);
	        					for (Integer price: l){
	        						String date = Utils.stringDate( day , i, 2016);
	        						if (!Database.isIN(departure, arrive, date)){
	        							//se non presente aggiungere
	        							Database.insert(departure, arrive, price, date);
	        							
	        						}
	        						else{
	        							if (price != Database.getPrice(departure, arrive, date))
	        								System.out.println("Prezzo diverso aggiorno");
	        								//se prezzo diverso aggiornare
	        								Database.update(departure, arrive, price, date);
	        						}	
	        						day++;
	        					}
	        					i++;
	        				}
	                    
	        		}
	        	}
		 });
		
		
		
	}
	public WebBrowser(String URL){
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		LoggerProvider.getChromiumProcessLogger().setLevel(Level.OFF);
		LoggerProvider.getIPCLogger().setLevel(Level.OFF);
		LoggerProvider.getBrowserLogger().setLevel(Level.OFF);
		browser = new Browser();
		BrowserView browserView = new BrowserView(browser);
		
        add(browserView, BorderLayout.CENTER);
        setBounds(50, 50, 1000, 800);
        
        setVisible(true);
		browser.loadURL(URL);
		System.out.println("loaded");
	}
	public LinkedList<LinkedList> getPriceGrid(){
		LinkedList<LinkedList> months = new LinkedList<>();
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		for (int i = 0; i<5;i++){
			try {
        		Thread.sleep(500);
        		
        	} catch (InterruptedException e) {
        		// TODO Auto-generated catch block
        		e.printStackTrace();
        	}
			
			LinkedList<Integer> price = new LinkedList<>();
			List<DOMElement> prices = document.findElements(By.className("table-price small"));
			for (DOMElement d: prices){
//				System.out.println(d.getTextContent());
				
				price.add(Integer.parseInt(d.getTextContent().substring(2)));
			}
			
			months.add(price);
			DOMElement btn_n = document.findElement(By.className("month-selector-next "));
        	btn_n.click();
		}
//		setVisible(false);
		
		infoBox("ATTENZIONE","Anche il ritorno?");
		dispose();
		
//		System.out.println("la finestra è attiva? "+isEnabled());
		
		return months;
		
	}
	
	public LinkedList<LinkedList> getPriceGraphic() {
		List<DOMElement> next = document.findElements(By.className("next"));
		next.get(0).click();
		next.get(0).click();
		LinkedList<LinkedList> month = new LinkedList<>();
		
		for (int i = 0; i<5;i++){
			LinkedList<Integer> prices = new LinkedList<>();
        	try {
        		Thread.sleep(500);
        	} catch (InterruptedException e) {
        		// TODO Auto-generated catch block
        		e.printStackTrace();
        	}
//        	prices.add(document.findElement(By.className("current")).getTextContent());
//        	System.out.print(document.findElement(By.className("current")).getTextContent());
        	System.out.println(document.findElement(By.className("dates")).getTextContent().split(" ")[1]);
        	System.out.println("MESE "+ (i+3));
        	List<DOMElement> price = document.findElements(By.className("primary "));
        	System.out.println(price.size());
        	for (DOMElement l:price)
        		prices.add(Integer.parseInt(l.getTextContent().split("€ ")[1]));
        	next.get(1).click();
        	month.add(prices);
        }
		
		infoBox("","");
		dispose();
		
		//finish = false;
		
		return month;
		
		
	}
	public static void infoBox(String infoMessage, String titleBar)
    {
		final JOptionPane optionPane = new JOptionPane(
			    "The only way to close this dialog is by\n"
			    + "pressing one of the following buttons.\n"
			    + "Do you understand?",
			    JOptionPane.QUESTION_MESSAGE,
			    JOptionPane.YES_NO_OPTION);
		
		int choice = JOptionPane.showConfirmDialog(null, "The only way to close this dialog is by\n"
			    + "pressing one of the following buttons.\n"
			    + "Do you understand?");
		switch (choice){
		case 0:
			browser.loadURL("www.google.it");
			break;
		case 1:
			System.out.println("1");
			break;
		case 2:
			System.out.println("2");
			break;
		
		}

//        JOptionPane.showMessageDialog(null, infoMessage, "InfoBox: " + titleBar, JOptionPane.YES_NO_CANCEL_OPTION);
    }
	
	
}
