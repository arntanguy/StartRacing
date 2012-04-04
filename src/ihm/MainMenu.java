package ihm;

import java.awt.Window;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class MainMenu extends JFrame {
	private static final long serialVersionUID = 1L;
	private JLabel title;
	private JButton play;
	private JButton help;
	private JButton options;
	private JButton quit;
	
	private JPanel centerPanel;
	
	MainMenu() {
		super(StringStore.GAME_TITLE);
		
		/* Instantiation des objets Java */
		createObjects();
		
		/* Mise en place des layout */
		mergeComponents();
		
		/* Param�trage */
		parameter();
		
		this.setVisible(true);
	}
	
	private void createObjects() {
		title = new JLabel(StringStore.MAIN_MENU_TITLE);
		play = new JButton(StringStore.MAIN_MENU_PLAY);
		help = new JButton(StringStore.MAIN_MENU_HELP);
		options = new JButton(StringStore.MAIN_MENU_OPTION);
		quit = new JButton(StringStore.MAIN_MENU_QUIT);
		
		centerPanel = new JPanel();
	}
	
	private void mergeComponents() {
		centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.PAGE_AXIS));
		
		centerPanel.add(title);
		centerPanel.add(new JPanel());
		centerPanel.add(play);
		centerPanel.add(help);
		centerPanel.add(options);
		centerPanel.add(quit);
		
		this.setContentPane(centerPanel);
	}
	
	private void parameter() {
		this.setResizable(false);
		this.setSize(860, 640);
	}
	
}
