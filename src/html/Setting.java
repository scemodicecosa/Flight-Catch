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
import javax.swing.JLabel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.LinkedList;
import java.util.List;
import javax.swing.JScrollPane;
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

	/**
	 * Create the panel.
	 */
	public Setting() {
		
		setLayout(null);
		setBounds(0, 0, 704, 700);
//		Browser browser = BrowserFactory.create();
		
		JCheckBox[] cb = new JCheckBox[Destinations.destinations.length];
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
		
		JComboBox ndayBox = new JComboBox(n_days);
		ndayBox.setBounds(376, 83, 45, 24);
		add(ndayBox);
		
		JButton btnUpdateSelected = new JButton();
		btnUpdateSelected.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				console.setText("Attendere il download dei voli....\n");
				console.update(console.getGraphics());
				List<String> dest = new LinkedList<String>();
				ListModel s = list.getModel();
				WebBrowser w= new WebBrowser("roma", "londra", "03");
				
				System.out.println();
				
				System.out.println(w.isVisible());
//				WebBrowser w2= new WebBrowser("roma", "amsterdam", "03");
//				w.initiate();
				/*for (int i = 0; i<s.getSize();i++){
					JCheckBox c = (JCheckBox) s.getElementAt(i);
					if (c.isSelected()){
						dest.add(c.getText());
//						System.out.println(c.getText()+" is selected");
					}
				}
				String[] destinazioni = new String[dest.size()];
				for (int i = 0; i<destinazioni.length;i++){
					destinazioni[i] = dest.get(i);
				}
				String n = (String) ndayBox.getSelectedItem();
				
				//Database.updateAll((monthBox.getSelectedIndex()+1)+"",Integer.parseInt(n), destinazioni,console );*/
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
		btnDontPressMe.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				Log log = new Log(console);
				log.clearConsole();
				//					log.logConsole("Scemooooo");
			}
		});
		btnDontPressMe.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnDontPressMe.setBounds(424, 384, 170, 25);
		add(btnDontPressMe);
		
		
		
		
	}
	public JTextPane getJText(){
		return console;
	}
}

