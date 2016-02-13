package html;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class newframe extends JFrame {

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					newframe frame = new newframe();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public newframe() {
		System.out.println("NUOVOFRAMEE");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
//		String[] data = {"casa", "mia", "cazzi", "tuoi"};
		JCheckBox[] cb = new JCheckBox[Destinations.destinations.length];
		for (int i = 0; i<cb.length; i++){
			cb[i] = new JCheckBox(Destinations.destinations[i]);
		}
		CheckBoxList list = new CheckBoxList(cb);
		
		add(list);
	}

}
