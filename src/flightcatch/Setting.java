package flightcatch;

import org.apache.commons.lang3.text.WordUtils;
import org.jdesktop.swingx.*;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;

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
	private JTextField from_man;
	private JTextField to_man;
	public JLabel lblCity, lblIATA;
	public MessageConsole mc;
	public String[] months = {"Jan","Feb","Mar","Apr","May","Jun",
			"Jul","Aug","Sep","Oct","Nov","Dec"};
	public CheckBoxList list;

	/**
	 * Create the panel.
	 */
	public Setting(WebBrowser wb, JTabbedPane tb) {

		this.wb = wb;
		this.tb = tb;
		setLayout(null);
		setBounds(0, 0, 704, 700);
		//		Browser browser = BrowserFactory.create();
		Destinations d = new Destinations();
		cb = new JCheckBox[d.city.size()];
//		String [] cd = new String[cb.length];
		for (int i = 0; i<cb.length; i++){
			cb[i] = new JCheckBox(d.city.get(i));
//			System.out.println(d.getMin(d.city.get(i)));
//			cd[i] = Destinations.destinations[i];
		}

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(0, 43, 364, 438);
		add(scrollPane);
		list = new CheckBoxList(cb);
		
		scrollPane.setViewportView(list);

		JLabel lblSelezionaUnaO = new JLabel("Seleziona una o più città da aggiornare");
		lblSelezionaUnaO.setBounds(12, 12, 352, 19);
		add(lblSelezionaUnaO);

		String[] n_days = new String[]{
				"1","2","3","4","5","6","7"
		};

		ndayBox= new JComboBox(n_days);
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

				//Check if manual option is selected


				//For every city check price

				for(String sr: dest){
					arrive = sr.toLowerCase();
					departure = "roma";
					for (int j = 0; j<2;j++){
						
						wb.browser.loadURL(Utils.manualGoogleURL(d.getMin(departure), d.getMin(arrive), month));
//						arrive = arrive.toLowerCase();
//						departure = departure.toLowerCase();
						//Wait until it completely loads the page
						while(wb.browser.isLoading()){
						}
						mc.redirectOut(Color.CYAN);
						System.out.println("[!] - Page Loaded "+departure +"-"+arrive );
						System.out.println("[!] - Start Grabbing Prices");
						long start = System.currentTimeMillis();
						LinkedList<LinkedList> price = wb.getGoogle(ndayBox.getSelectedIndex()+1);//wb.getPrices(ndayBox.getSelectedIndex()+1 ,1);
						long end = System.currentTimeMillis();
						System.out.println("[!] - Price Grabbed In " +(end-start)+"ms");
						int m = monthBox.getSelectedIndex()+1;

						//Iterate lists of prices from different month
						
						for (LinkedList<Integer> l :price){
							mc.redirectOut(Color.RED);
							System.out.println("[i] - Prices of "+months[m-1]);
							int day = 1;
							//							for (int i: l){
							int max = Utils.howManyDays(m);
							for(int i = 0;i<max;i++){
								String date = Utils.stringDate(day,m,2016);
								String log = "[!] - Price Not Changed";
								mc.redirectOut(Color.WHITE);
								int pr = l.get(i);
								//if is not in database, add
								if (!Database.isIN(departure, arrive, date)){
									Database.insert(departure, arrive, pr, date);
									//									System.out.println("[!] - First Insert Price");
									log = "[!] - First Insert Price";
									mc.redirectOut(Color.GREEN);
								}
								else{
									//If different price, update
									if (Database.getPrice(departure, arrive, date) != pr){
										//										System.out.println("[!] - Update Price");
										log = "[!] - Update Price";
										mc.redirectOut(Color.YELLOW);
										Database.update(departure, arrive, pr, date);
									}

								}
								System.out.println(log+" of "+ date+" "+l.get(i)+ " €");
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
				//Ended

				tb.setSelectedIndex(1);
				mc.redirectOut(Color.MAGENTA);
				System.out.println("[!] - Prices downloaded and inserted correctly!");
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




		monthBox = new JComboBox(months);
		monthBox.setBounds(424, 83, 57, 24);
		monthBox.setSelectedIndex(2);
		add(monthBox);


		JScrollPane scrollConsole = new JScrollPane();
		scrollConsole.setBounds(376, 119, 316, 213);
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

		mc = new MessageConsole(console);
		mc.redirectOut(Color.WHITE);
		mc.redirectErr(Color.RED, null);
		mc.setMessageLines(10000);
//		System.out.println("prova");

		from_man = new JTextField();
		from_man.setBounds(376, 365, 114, 30);
		add(from_man);
		from_man.setColumns(10);
		from_man.setVisible(true);

		to_man = new JTextField();
		to_man.setBounds(496, 365, 114, 30);
		add(to_man);
		to_man.setColumns(10);
		to_man.setVisible(true);

		lblCity = new JLabel("City");
		lblCity.setBounds(376, 348, 70, 15);
		add(lblCity);
		lblCity.setVisible(true);


		lblIATA = new JLabel("IATA code");
		lblIATA.setBounds(496, 348, 70, 15);
		add(lblIATA);
		
		JButton btnAdd = new JButton("ADD");
		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				d.add(WordUtils.capitalize(from_man.getText()), to_man.getText().toUpperCase());
				from_man.setText("");
				to_man.setText("");
				System.out.println("[!]- Airport added!");
				System.out.println("[!]- Restart the application!");
				/*cb = new JCheckBox[d.city.size()];
				for (int i = 0; i<cb.length; i++){
					cb[i] = new JCheckBox(d.city.get(i));
				}
				list = new CheckBoxList(cb);
				list.repaint();*/
				
			}
		});
		btnAdd.setBounds(622, 365, 63, 28);
		add(btnAdd);
		lblIATA.setVisible(true);




	}
	public JTextPane getJText(){
		return console;
	}
}

