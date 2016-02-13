package html;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

public class MyListCellRender extends JLabel implements ListCellRenderer{
	int min;
	public MyListCellRender(int min) {
        setOpaque(true);
        this.min = min;
    }

	@Override
	public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected,
			boolean cellHasFocus) {
		setText(value.toString());
		list.setBackground(isSelected ?
                list.getSelectionBackground() : getBackground());
        list.setForeground(isSelected ?
                list.getSelectionForeground() : getForeground());
		
		//inserisci stringa   (possibile cambio font e bla bla bla)
//		System.out.println(value.toString());
		if (Utils.extractPrice(value.toString()) <=min) {
			//System.out.println("green");
			setBackground(Color.GREEN);
		}
        else setBackground(Color.WHITE);
		list.repaint();
		// TODO Auto-generated method stub
		return this;
	}

}
