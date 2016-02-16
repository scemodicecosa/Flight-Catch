package flightcatch;

import java.awt.EventQueue;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Point;
import java.awt.ScrollPane;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.imageio.ImageIO;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;

import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import org.jsoup.nodes.Element;

import com.teamdev.jxbrowser.chromium.Browser;
import com.teamdev.jxbrowser.chromium.ContextMenuHandler;
import com.teamdev.jxbrowser.chromium.ContextMenuParams;
import com.teamdev.jxbrowser.chromium.LoggerProvider;
import com.teamdev.jxbrowser.chromium.events.FailLoadingEvent;
import com.teamdev.jxbrowser.chromium.events.FinishLoadingEvent;
import com.teamdev.jxbrowser.chromium.events.FrameLoadEvent;
import com.teamdev.jxbrowser.chromium.events.LoadAdapter;
import com.teamdev.jxbrowser.chromium.events.LoadEvent;
import com.teamdev.jxbrowser.chromium.events.LoadListener;
import com.teamdev.jxbrowser.chromium.events.ProvisionalLoadingEvent;
import com.teamdev.jxbrowser.chromium.events.StartLoadingEvent;
import com.teamdev.jxbrowser.chromium.swing.BrowserView;

import javax.swing.JTextArea;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JSpinner;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.UIManager;
import javax.swing.JCheckBox;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.ItemEvent;
import javax.swing.JTable;
import javax.swing.JTabbedPane;
public class Gui {

	public JFrame frame;
	public JTextArea textArea;
//	public JScrollPane scroll;
	private JTextField from;
	private JTextField to;
	private JTextField when;
//	private DefaultListModel<Flight> listModel;
	private JComboBox<String> monthBox;
	private JComboBox<String> dayBox;
	public int min = 0;
	private JTextField textField;
	private JLabel lblHowLong;
	private JCheckBox chckbxAr;
	private JTable table;
	private DefaultTableModel model;
	public WebBrowser wb;
	public JTabbedPane tabbedPane;
	public Setting setting;
	//	private JButton btnSearch;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
		} catch (Throwable e) {
			e.printStackTrace();
		}
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Gui window = new Gui();
					//					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Gui() {
		LoggerProvider.getChromiumProcessLogger().setLevel(Level.OFF);
		LoggerProvider.getIPCLogger().setLevel(Level.OFF);
		LoggerProvider.getBrowserLogger().setLevel(Level.OFF);
		initialize();
	}
	public JTextArea getJTextArea(){
		return textArea;
	}
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		String[] month = {"January","February","March","April","May","June",
				"July","August","September","October","November","December"};   
		String[] months = {"Jan","Feb","Mar","Apr","May","Jun",
				"Jul","Aug","Sep","Oct","Nov","Dec"};
		String[] asd = {"Gennaio", "Febbraio", "Marzo", "Aprile", "Maggio", "Giugno", 
				"Luglio","Agosto", "Settembre", "Ottobre", "Novembre","Dicembre"};
		String[] day = {"Always","01","02","03","04","05","06","07","08","09","10","11","12","13","14",
				"15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31"};
		ImageIcon ico2;
		ico2 = new ImageIcon("img/search_icon.png");
//		listModel = new DefaultListModel<Flight>();
//		JList<Flight> list = new JList<Flight>(listModel);

		JFrame window =  new JFrame("Skyscanner datte foco");
		window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(0, 0, 704, 700);
		window.getContentPane().add(tabbedPane);
		
		JPanel panel = new JPanel();
		wb = new WebBrowser();
		setting = new Setting(wb, tabbedPane);
		
		tabbedPane.addTab("Search" ,panel);
		tabbedPane.addTab("Settings",setting);
		tabbedPane.addTab("Browser", wb);
		
		
		
		panel.setLayout(null);

		JLabel lblFrom = new JLabel("FROM");
		lblFrom.setBounds(39, 6, 34, 15);
		panel.add(lblFrom);

		JLabel lblDay = new JLabel("DAY");
		lblDay.setBounds(247, 6, 24, 15);
		panel.add(lblDay);

		JLabel lblTo = new JLabel("TO");
		lblTo.setBounds(154, 6, 16, 15);
		panel.add(lblTo);

		JLabel lblMonth = new JLabel("MONTH");
		lblMonth.setBounds(309, 6, 44, 15);
		panel.add(lblMonth);

		JLabel lblYear = new JLabel("YEAR");
		lblYear.setBounds(382, 6, 31, 15);
		panel.add(lblYear);

		JButton btnPrint = new JButton("Print");
		btnPrint.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				for (Flight f:Database.show()){
//					Object[] o = {s.getDeparture(),s.getArrive(),
//							s.getPrice()+" €",s.getDate()
//					};
					model.addRow(f.getObject());
//					table.getColumnModel().getColumn(2).setCellRenderer(new MyTableCellRender(0));
//					table.repaint();
//					listModel.addElement(s);
//					list.setCellRenderer(new MyListCellRender(10));
				}
			}
		});
		btnPrint.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnPrint.setBounds(500, 70, 63, 27);
		panel.add(btnPrint);

		JButton btnClean = new JButton("Clean");
		btnClean.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				table.removeAll();
				for (int i = model.getRowCount() -1; i >= 0;i--)
					model.removeRow(i);
//				tabbedPane.setSelectedIndex(2);
//				wb.browser.loadURL("it.wikipedia.org");
			}
		});
		
		btnClean.setBounds(500, 103, 63, 27);
		panel.add(btnClean);

		from = new JTextField();
		from.setText("roma");
		from.setBounds(16, 33, 77, 27);
		panel.add(from);
		from.setColumns(10);

		to = new JTextField();
		to.setBounds(116, 33, 106, 27);
		panel.add(to);
		to.setColumns(10);

		when = new JTextField();
		when.setBounds(371, 33, 57, 27);
		panel.add(when);
		when.setColumns(10);
		when.setText("2016");
		monthBox = new JComboBox(months);
		monthBox.setBounds(300, 34, 70, 25);
		panel.add(monthBox);
		dayBox = new JComboBox(day);
		dayBox.setBounds(228, 34, 70, 25);
		dayBox.setEditable(true);
		panel.add(dayBox);


		JButton btnSearch = new JButton(ico2);
		btnSearch.setBounds(429, 33, 30, 27);
		btnSearch.setMargin(new Insets(0, 0, 0, 0));
		btnSearch.setOpaque(false);
		btnSearch.setContentAreaFilled(false);
		btnSearch.setBorderPainted(false);
		btnSearch.setBorder(null);
		btnSearch.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
//				textArea.setText("");
				
				String departure = from.getText();
				String arrive = to.getText();

				String date;
//				String month = ""+(monthBox.getSelectedIndex()+1);
//				if (monthBox.getSelectedIndex()+1<10)
//					month = "0"+(monthBox.getSelectedIndex()+1);
				date = Utils.stringDate(dayBox.getSelectedIndex(), monthBox.getSelectedIndex()+1, Integer.parseInt(when.getText()));//dayBox.getSelectedItem()+"-"+month+"-"+when.getText();
//				java.sql.Date day;
				if (arrive.equals(""))
					arrive = null;
				if (departure.equals(""))
					departure = null;
				if (dayBox.getSelectedItem().equals("Always")){
					String[] dates = date.split("-");
					date = dates[1]+"-"+dates[2];

				}
				
				if (chckbxAr.isSelected()){
					Flight[] s = Database.searchAR(departure, arrive, date, Integer.parseInt(textField.getText()));
					for(int i = 0; i<s.length;i++){
						Object[] o = new Object[]{ s[i].getDeparture(),s[i].getArrive(),
								s[i].getPrice()+" €", s[i].getDate()
						};
						model.addRow(o);
						System.out.println("added");
						}
				}
				else{

					if (dayBox.getSelectedItem().equals("Always")){
						Flight[] s = Database.search(departure, arrive, date);
						Price p = new Price(s);
						min = p.getMin();
						for(Flight f:s){
							
							model.addRow(f.getObject());
//							
						}
						System.out.println("DONE");



					}
					else{
						Flight[] s = Database.search(departure, arrive, date);
						System.out.println("dovremmo essere qui "+date);
						for(Flight f: s){
							
							model.addRow(f.getObject());
							
						}

					}
				}
				
//				table.getColumnModel().getColumn(2).setCellRenderer(new MyTableCellRender(min));
//				table.repaint();
			}
		});
		btnSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});

		panel.add(btnSearch);


		chckbxAr = new JCheckBox("A/R");
		chckbxAr.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (chckbxAr.isSelected()){
					panel.add(textField);
					panel.add(lblHowLong);
					window.repaint();

					System.out.println("Rimosso");
				}
				else{
					panel.remove(textField);
					panel.remove(lblHowLong);
					window.repaint();
				}

			}
		});
		chckbxAr.setBounds(472, 37, 42, 18);
		panel.add(chckbxAr);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(16, 72, 456, 431);
		panel.add(scrollPane);
		model = new DefaultTableModel(){
			@Override
			public boolean isCellEditable(int row, int column){
				return false;
			}
		};
		model.addColumn("from");
		model.addColumn("to");
		model.addColumn("price");
		model.addColumn("when");
		table = new JTable(model);
		//		model.addRow(new Object[]{"roma","londra","35€","2016-01-12"});
		//		model.addRow(new Object[]{"roma","londra","40€","2016-01-12"});
		scrollPane.setViewportView(table);
//		table.getColumnModel().getColumn(2).setCellRenderer(new MyTableCellRender(50));
		//		tabbedPane.add("casa", window);

		lblHowLong = new JLabel("How long?");
		lblHowLong.setBounds(513, 6, 70, 15);


		textField = new JTextField();
		textField.setBounds(519, 33, 44, 27);
		//panel.add(textField);
		textField.setColumns(10);
//		panel.add(textField);
//		panel.add(lblHowLong);
		ImageIcon ico = new ImageIcon("img/switch.png");
		JButton btnSwitch = new JButton(ico);
		btnSwitch.setContentAreaFilled(false);
		btnSwitch.setBorderPainted(false);
		btnSwitch.setBorder(null);
		btnSwitch.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				String temp = from.getText();
				from.setText(to.getText());
				to.setText(temp);
			}
		});
		btnSwitch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnSwitch.setBounds(92, 33, 24, 26);
		panel.add(btnSwitch);
		table.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent evt) {
				JTable list = (JTable)evt.getSource();
				if (evt.getClickCount() == 2) {
					// Double-click detected
					//		        	String dep = 
					String departure = (String) model.getValueAt(table.getSelectedRow(), 0);
					String arrive = (String) model.getValueAt(table.getSelectedRow(), 1);
					String date = (String) model.getValueAt(table.getSelectedRow(), 3);
					String url = Utils.getSingleUrl(departure, arrive, date);
					tabbedPane.setSelectedIndex(2);
					wb.browser.loadURL(url);
//					WebBrowser w = new WebBrowser(url);
					//Utils.openPage(url);
				}
				list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
				list.repaint();

			}
		});
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setBounds(100, 100, 704, 600);
		window.getContentPane().setLayout(null);
		//	        window.getContentPane().add(scroll);

		JButton btnAggiorna = new JButton("Aggiorna");
		btnAggiorna.setBounds(390, 26, 98, 25);
		//window.setSize(500, 500);
		window.setVisible(true);
		window.setLocationRelativeTo(null);

	}
	
	
	
	public static boolean isZero(int[] price){
		int j = 0;
		for (int i:price){
			if (i == 0)
				j++;
		}
		if (j>20)
			return true;
		return false;
	}
}
