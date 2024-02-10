import javax.swing.JFrame;

public class Frame extends JFrame {
	
	Frame() {
		
		this.add(new Panel());
		this.setTitle("Cho'Gath's revenge");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		this.pack();
		this.setVisible(true);
		this.setLocationRelativeTo(null);
		
		
	}
	
}
