package html;

import javax.swing.JPanel;
import javax.swing.ListModel;
import javax.swing.UIDefaults;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

import com.teamdev.jxbrowser.chromium.Browser;
//import com.teamdev.jxbrowser.chromium.BrowserFactory;

import javax.swing.BorderFactory;
import javax.swing.ComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JLabel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.LinkedList;
import java.util.List;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JTextField;
import java.awt.Color;
import java.awt.Font;
import javax.swing.JTextPane;

public class Setting extends JPanel {
	private JTextPane console;
	private JComboBox monthBox;
	public WebBrowser wb;
	public JTabbedPane tb;
	public JCheckBox[] cb;
	public JComboBox<String> ndayBox ;
	public String[] month_n =
		{"01","02","03","04","05","06"
		,"07","08","09","10","11","12"};
	
	/**
	 * Create the panel.
	 */
	public Setting(WebBrowser wb, JTabbedPane tb) {
		
		this.wb = wb;
		this.tb = tb;
		setLayout(null);
		setBounds(0, 0, 704, 700);
//		Browser browser = BrowserFactory.create();
		
		cb = new JCheckBox[Destinations.destinations.length];
		for (int i = 0; i<cb.length; i++){
			cb[i] = new JCheckBox(Destinations.destinations[i]);
		}
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(0, 43, 364, 438);
		add(scrollPane);
		CheckBoxList list = new CheckBoxList(cb);
		scrollPane.setViewportView(list);
		
		JLabel lblSelezionaUnaO = new JLabel("Seleziona una o più città da aggiornare");
		lblSelezionaUnaO.setBounds(12, 12, 352, 19);
		add(lblSelezionaUnaO);
		
		String[] n_days = new String[]{
				"1","2","3","4","5","6","7"
		};
		
		ndayBox= new JComboBox<>(n_days);
		ndayBox.setBounds(376, 83, 45, 24);
		add(ndayBox);
		
		JButton btnUpdateSelected = new JButton();
		
		
		/******** LISTENER UPDATE BUTTON ********/
		
		btnUpdateSelected.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				console.setText("Attendere il download dei voli....\n");
				console.update(console.getGraphics());
				List<String> dest = new LinkedList<String>();
				ListModel s = list.getModel();
				tb.setSelectedIndex(2);
				String arrive;
				String departure;
				String month = month_n[monthBox.getSelectedIndex()];
				System.out.println(month);
				
				//Check which city is checked
				
				for (int i = 0; i<s.getSize();i++){
					JCheckBox c = (JCheckBox) s.getElementAt(i);
					if (c.isSelected()){
						dest.add(c.getText());
					}
				}
				
				//For every city check price
				
				for(String sr: dest){
					arrive = sr.toLowerCase();
					departure = "roma";
					for (int j = 0; j<2;j++){ 
						wb.browser.loadURL(Utils.createUrl(departure, arrive, month));

						//Wait until it completely loads the page
						while(wb.browser.isLoading()){
						}

						System.out.println("[!] - Page Loaded");
						System.out.println("[!] - Start Grabbing Prices");

						LinkedList<LinkedList> price = wb.getPrices(ndayBox.getSelectedIndex()+1 ,1);

						System.out.println("[!] - Stop Grabbing Prices");
						int m = ndayBox.getSelectedIndex()+1;
						for (LinkedList<Integer> l :price){
							System.out.println("[i] - Prices of month "+ m);
							int day = 1;
							for (int i: l){
								String date = Utils.stringDate(day,m,2016);
								
								//if is not in database, add
								
								if (!Database.isIN(departure, arrive, date)){
									Database.insert(departure, arrive, i, date);
//									System.out.println("[!] - Insert Price");
								}
								else{
									//If different price, update
									if (Database.getPrice(departure, arrive, date) != i){
//										System.out.println("[!] - Update Price");
										Database.update(departure, arrive, i, date);
									}
									
								}
//								System.out.println(i+ " €");
								day++;
							}
							m++;
						}
						if (j==0){
//							j+= JOptionPane.showConfirmDialog(wb, "Vuoi scaricare il ritorno?");
							String temp = departure;
							departure = arrive;
							arrive = temp;
						}
					}
				}
			}
		});
		btnUpdateSelected.setText("Update");
		btnUpdateSelected.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnUpdateSelected.setBounds(496, 83, 86, 24);
		add(btnUpdateSelected);
		
		
		
		JLabel lblQuantiMesiVuoi = new JLabel("Quanti mesi vuoi aggiornare?");
		lblQuantiMesiVuoi.setBounds(379, 47, 215, 15);
		add(lblQuantiMesiVuoi);
		
		String[] month = {"Jan","Feb","Mar","Apr","May","Jun",
				"Jul","Aug","Sep","Oct","Nov","Dec"};
		
		
		monthBox = new JComboBox(month);
		monthBox.setBounds(424, 83, 57, 24);
		monthBox.setSelectedIndex(2);
		add(monthBox);
		

		JScrollPane scrollConsole = new JScrollPane();
		scrollConsole.setBounds(376, 119, 301, 213);
		add(scrollConsole);
		console = new JTextPane();
		scrollConsole.setViewportView(console);
		Color bgColor = Color.BLACK;
		UIDefaults defaults = new UIDefaults();
		defaults.put("TextPane[Enabled].backgroundPainter", bgColor);
		console.putClientProperty("Nimbus.Overrides", defaults);
		console.putClientProperty("Nimbus.Overrides.InheritDefaults", true);
		console.setBackground(bgColor);
		console.setForeground(Color.WHITE);
//		textPane.setBackground(Color.red);
		
		/*MessageConsole mc = new MessageConsole(console);
		mc.redirectOut();
		mc.redirectErr(Color.RED, null);
		mc.setMessageLines(100);
		*/
		JButton btnDontPressMe = new JButton("Don't press me");
		
		btnDontPressMe.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				wb.browser.loadURL("www.skyscanner.it");
			}
		});
		btnDontPressMe.setBounds(424, 384, 170, 25);
		add(btnDontPressMe);
		
		
		
		
	}
	public JTextPane getJText(){
		return console;
	}
}

