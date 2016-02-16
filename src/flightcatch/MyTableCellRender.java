package flightcatch;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

public class MyTableCellRender extends DefaultTableCellRenderer {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public int min;
	
	
	public MyTableCellRender(int min)	{
		this.min = min;
		System.out.println("invocato");
	}


	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {
//		System.out.println("entrato");
		Component cellComponent = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		setText(value.toString());
		String s = (String) value;
		s = s.substring(0, s.length()-2);
		Color c = cellComponent.getBackground();
		Color mine = Color.decode("#F2F2F2");
//		System.out.println(c.toString()+"riga numero :" +row);
		if (Integer.parseInt(s)<=min){
//			table.setBackground(Color.GREEN);
			cellComponent.setBackground(Color.GREEN);
		}
		else
			if (row%2 == 0){
				cellComponent.setBackground(Color.WHITE);
			}
			else{
				cellComponent.setBackground(mine);
			}
//			table.setBackground(c);
			
		table.repaint();
		return this;
	}
	
}
