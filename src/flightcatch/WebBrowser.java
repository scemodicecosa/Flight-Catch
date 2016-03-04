package flightcatch;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Robot;
import java.awt.ScrollPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;

import javax.swing.*;

import com.teamdev.jxbrowser.chromium.Browser;
import com.teamdev.jxbrowser.chromium.ContextMenuHandler;
import com.teamdev.jxbrowser.chromium.ContextMenuParams;
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



public class WebBrowser extends JPanel {
	Browser browser;
	BrowserView view;
	DOMDocument document;
	ScrollPane scroll;
	JButton reload, back, forward;
	JTextField url;
	
	public WebBrowser(){
		JPopupMenu.setDefaultLightWeightPopupEnabled(false);
		setBounds(0,0,704,700);
		setLayout(null);
		browser = new Browser();
		view = new BrowserView(browser);
		
		/******* Scroll ********/
		
		scroll = new ScrollPane();
		scroll.setLocation(4, 31);
		scroll.setSize(690,673);
		scroll.add(view);
		add(scroll);
		
		/******** Text Field with URL ********/
		
		url = new JTextField("https://www.google.it/flights/#search;f=FCO,CIA,IRT,XRJ;t=LAX;d=2016-03-01;r=2016-03-05;tt=o;q=flight");
		url.setBounds(111, 0, 581, 30);
		url.addKeyListener(new KeyAdapter(){

			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ENTER){
//					System.out.println("premuto enter");
					browser.loadURL(url.getText());
				}
			}
			
		});
		add(url);
		
		/******** Buttons ********/
		
		ImageIcon back_img = new ImageIcon("img/back.png");
		ImageIcon forward_img = new ImageIcon("img/forward.png");
		ImageIcon refresh_img = new ImageIcon("img/refresh.png");
		
		reload = new JButton(refresh_img);
		reload.setBounds(75,0,30,30);
		reload.setOpaque(false);
		reload.setContentAreaFilled(false);
		add(reload);
		
		forward = new JButton(forward_img);
		forward.setOpaque(false);
		forward.setContentAreaFilled(false);
		forward.setBounds(39, 0, 30, 30);
		add(forward);
		
		back = new JButton(back_img);
		back.setBounds(5, 0, 30, 30);
		back.setOpaque(false);
		back.setContentAreaFilled(false);
		add(back);
		
		forward.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (browser.canGoForward())
					browser.goForward();
			}
		});
		back.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (browser.canGoBack())
					browser.goBack();
			}
		});
		reload.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				browser.reload();
			}
			
		});
		
		/******** Load Url and create onLoadListener ********/
		
		
		browser.loadURL(url.getText());
		browser.addLoadListener(new LoadAdapter(){
			
			
			
			@Override
			public void onProvisionalLoadingFrame(ProvisionalLoadingEvent arg0) {
				// TODO Auto-generated method stub
//				System.out.println("sto caricando");
				url.setText(browser.getURL());
//				
			}

			@Override
			public void onFinishLoadingFrame(FinishLoadingEvent arg0) {
				// TODO Auto-generated method stub
				document = browser.getDocument();
//				getGoogle(2);
				
				
			}


		});
		browser.setContextMenuHandler(new MyContextMenuHandler(view, this));
		
	}
	
	/******** Return a list with prices taken from grid page ********/
	
	public LinkedList<Integer> getPriceGrid(){
		
		LinkedList<Integer> prices = new LinkedList<>();
		
		List<DOMElement> price_el = document.findElements(By.className("table-price small"));
		for (DOMElement p: price_el)
			prices.add(Integer.parseInt(p.getTextContent().split("€ ")[1]));
		
		return prices;
		
	}
	
	/******** Return a list with prices taken from graphic page ********/
	
	public LinkedList<Integer> getPriceGraphic(){
		LinkedList<Integer> prices = new LinkedList<>();
		
		List<DOMElement> price = document.findElements(By.className("primary "));
    	for (DOMElement l:price)
    		prices.add(Integer.parseInt(l.getTextContent().split("€ ")[1]));
		
		
		return prices;		
	}
	
	/******* Get prices from different month, switch between graphic layout and grid layout ********/
	
	public LinkedList<LinkedList> getPrices(int howMany, int isGrid){
		
		LinkedList<LinkedList> prices = new LinkedList<>();
		
		switch(isGrid){
		case 0: //Is graphic
			for (int i = 0; i<howMany;i++){
				try {
	        		Thread.sleep(1000);
	        	} catch (InterruptedException e) {
	        		// TODO Auto-generated catch block
	        		e.printStackTrace();
	        	}
				LinkedList<Integer> price = getPriceGraphic();
				prices.add(price);
				
				//Get next button and click it
				
				List<DOMElement> next = document.findElements(By.className("next"));
				next.get(1).click();
				
			}
			
			break;
		case 1: //Is grid
			
			for (int i = 0; i<howMany;i++){
				try {
	        		Thread.sleep(1000);
	        	} catch (InterruptedException e) {
	        		// TODO Auto-generated catch block
	        		e.printStackTrace();
	        	}
				
				while(browser.isLoading()){
					
				}
				
				LinkedList<Integer> price = getPriceGrid();
				prices.add(price);
				
				//Click next month
				
				DOMElement btn_n = document.findElement(By.className("month-selector-next "));
				try{
					btn_n.click();
				} catch(NullPointerException e){
					JOptionPane.showConfirmDialog(scroll, "Riprova");
					
				}
				
			}
			break;
		}
		
		return prices;
	}
	
	/******* Get price from google flight *******/
	
	public LinkedList<Integer> getGooglePrice(){
		LinkedList<Integer> price = new LinkedList<>();
		document = browser.getDocument();
		List<DOMElement> el = document.findElements(By.className("AGUW54C-p-f"));
		for (DOMElement e:el){
			price.add(Integer.parseInt(e.getTextContent().replace(".", "").substring(0, e.getTextContent().length() -2 )));
		}
		
		return price;
	}
	
	/******* Get price for more months ********/
	
	public LinkedList<LinkedList> getGoogle(int howMany){
		LinkedList <LinkedList> l = new LinkedList<>();

		//Click on giorno end then save next button
		document = browser.getDocument();
//		List<DOMElement> dom_el = document.findElements(By.className("AGUW54C-p-m datePickerNextButton AGUW54C-c-b"));
		List<DOMElement> el = document.findElements(By.className("AGUW54C-I-d AGUW54C-I-o"));
		List<DOMElement> ela = document.findElements(By.className("AGUW54C-I-n AGUW54C-zb-a"));
		
//		dom_el.get(0).click();
//		el.get(0).click();
//		el.get(0).click();
		
		/******** Move mouse and click on the calendar *******/
		Point p = url.getLocationOnScreen();
		Point m = new Point();
		m.setLocation(p.getX()+55, p.getY()+273); 
		ela.get(0).click();
		try {
			Robot r = new Robot();
			r.mouseMove((int) p.getX()+258, (int) p.getY()+193);
			r.mousePress(InputEvent.BUTTON1_MASK);
			r.mouseRelease(InputEvent.BUTTON1_MASK);
			r.mouseMove((int) p.getX()+55, (int) p.getY()+273);///con cookie cambiare a 273 | 253 se senza cookie
			r.mousePress(InputEvent.BUTTON1_MASK);
			r.mouseRelease(InputEvent.BUTTON1_MASK);
		} catch (AWTException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		for (int i = 0; i<howMany;i++){
			//PAUSA
			try {
				Thread.sleep(000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			while(browser.isLoading()){
			}
			LinkedList<Integer> price = getGooglePrice();
			do{
				price = getGooglePrice();
			}
			while(price.size()==0);
			
			l.add(price);
			
			//Next-Button
			document = browser.getDocument();
			DOMElement dom_el = document.findElement(By.className("AGUW54C-p-m datePickerNextButton AGUW54C-c-b"));
			dom_el.click();
//			dom_el.click();
			
		}
		
		
		
		
		
		
//		System.out.println("[!] - JPanel "+p.getX()+" "+p.getY());
//		System.out.println("[!] - Mouse "+m.getX()+" "+m.getY());
		
		return l;
	}
	
	
	/******** Class that handle right click ********/
	
	public static class MyContextMenuHandler implements ContextMenuHandler{
		private JComponent component;
		private WebBrowser wb;
		
		MyContextMenuHandler(JComponent parentComponent, WebBrowser wb){
			this.component = parentComponent;
			this.wb = wb;
		}
		@Override
		public void showContextMenu(ContextMenuParams arg0) {
			// TODO Auto-generated method stub
			JPopupMenu popMenu = new JPopupMenu();
			Browser browser = arg0.getBrowser();
			
			popMenu.add(createMenuItem("Print link", new Runnable(){
				public void run() {
					//String s = browser.getDocument().getDocumentElement().getInnerHTML();
					//System.out.println(s);
//					DOMDocument document = browser.getDocument();
//					LinkedList<Integer> l = wb.getGooglePrice();
//					System.out.println(l.size());
//					for(int el:l){
//						System.out.println(el);
//					}
//					wb.getGoogle(2);
					Point p = wb.url.getLocationOnScreen();
					Point m = MouseInfo.getPointerInfo().getLocation();
					System.out.println("[!] - URL "+p.getX()+" "+p.getY());
					System.out.println("[!] - Mouse "+m.getX()+" "+m.getY());
					
				}
			}));
			
			popMenu.add(createMenuItem("Grab",new Runnable(){
				public void run(){
					System.out.println("[!] -Start grabbing prices! ...");
					LinkedList<LinkedList> price = wb.getPrices(3,1);
					for (LinkedList<Integer> l :price)
						for (int i: l)
							System.out.println(i);
					System.out.println("[!] -Stopped grabbing prices! ...");
				}
			}));

			popMenu.add(createMenuItem("Reload", new Runnable() {
				public void run() {
					browser.reload();
				}
			}));
			
			if (browser.canGoBack())
			popMenu.add(createMenuItem("Go back", new Runnable(){
				public void run(){
					browser.goBack();
				}
			}));
			
			if (browser.canGoForward())
				popMenu.add(createMenuItem("Go Forward", new Runnable(){
					public void run(){
						browser.goForward();
					}
				}));
			
			Point location = arg0.getLocation();
			SwingUtilities.invokeLater(new Runnable() {
	            public void run() {
	                popMenu.show(component, location.x, location.y);
	            }
	        });
		}
		private static JMenuItem createMenuItem(String title, final Runnable action) {
	         JMenuItem reloadMenuItem = new JMenuItem(title);
	         reloadMenuItem.addActionListener(new ActionListener() {
	             public void actionPerformed(ActionEvent e) {
	                 action.run();
	             }
	         });
	         return reloadMenuItem;
	     }
	}
}




	/*DOMDocument document;
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
*/