package flightcatch;

import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;

import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.awt.event.ActionEvent;

public class DBViewer extends JPanel{
	private JTextArea textQuery;
	private JTable textResult;
	public JButton btnExe;
	public JScrollPane scrollQue;
	private JScrollPane scrollRes;
	public String query = "";
	public Connection conn; //= DriverManager.getConnection("jdbc:mysql://localhost:3306/flights","root","rotfl");
	public Statement st; // = conn.createStatement();
	public JTable table;
	public DefaultTableModel model;
	
	
	public DBViewer() {
		setBounds(0,0,704,700);
		setLayout(null);
		
		btnExe = new JButton("EXECUTE");
		btnExe.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				
				query = textQuery.getText();
				LinkedList<LinkedList> res = executeQuery();
				int col = res.get(0).size();
				model = new DefaultTableModel(){
					@Override
					public boolean isCellEditable(int row, int column){
						return false;
					}
				};
				for (int i = model.getRowCount() -1; i >= 0;i--)
					model.removeRow(i);
				if (col == 2){
					model.addColumn("City");
					model.addColumn("Min");
					textResult.setModel(model);
				}
				else if (col == 5){
					model.addColumn("Departure");
					model.addColumn("Arrive");
					model.addColumn("Price");
					model.addColumn("When");
					model.addColumn("Log");
					textResult.setModel(model);
				}
				System.out.println("Size of list = "+col);
				
				for (LinkedList<String> l:res){
					Object[] o = l.toArray();
					model.addRow(o);
				}
//				model.addRow(new Object[] {"casa","via"});
//				textResult.repaint();
//				for(String s:res)
//					System.out.println(s);
					
//					textResult.append(s+"\n");
				
			}
		});
		btnExe.setBounds(551, 131, 117, 25);
		add(btnExe);
		
		scrollQue = new JScrollPane();
		scrollQue.setBounds(33, 27, 501, 132);
		add(scrollQue);
		
		scrollRes = new JScrollPane();
		scrollRes.setBounds(35, 196, 498, 386);
		add(scrollRes);
		
		textQuery = new JTextArea();
		scrollQue.setViewportView(textQuery);
		textQuery.setColumns(10);
		
		
		
		textResult = new JTable();
		scrollRes.setViewportView(textResult);
		
		
	}
	public LinkedList<LinkedList> executeQuery(){
		LinkedList<LinkedList> res = new LinkedList<>();
		
		try {
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/flights","root","rotfl");
			st = conn.createStatement();
			if (query.toLowerCase().contains("insert") || query.toLowerCase().contains("update") || query.toLowerCase().contains("delete")){
				//update
				int rs = st.executeUpdate(query);
				LinkedList<String> s = new LinkedList<>();
				s.add(rs+"");
				res.add(s); 
				return res;
			}
			else{
				ResultSet rs = st.executeQuery(query);
				int counter = 0;
				while(rs.next()){
					counter++;
				}
				rs = st.executeQuery(query);


				ResultSetMetaData metadata = rs.getMetaData();
				int columnCount = metadata.getColumnCount();


				String r = "";
				while(rs.next()){
					LinkedList<String> s = new LinkedList<>();
					for (int i = 2; i<= columnCount;i++){
						s.add(rs.getString(i));
					}
					res.add(s);
					
					System.out.println(r);
				}
				
				return res;
			}
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			LinkedList<String> s = new LinkedList<>();
			s.add("Error");
			res.add(s);
			return res;
		}
		
		
		
		
		
		
	}
}
