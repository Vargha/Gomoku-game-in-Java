import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.border.LineBorder;
import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JButton;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingConstants;
import java.awt.Component;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;


public class GomFrame extends JFrame {
	private static final long serialVersionUID = 1L;
	
	private JPanel contentPane;
	int top=-1;										// stack pointer
	int lastRow[] = new int[200];                   // holds last position of row so back can happen
	int lastCol[] = new int[200];                   // holds last position of col
	int row, col;									// place of last move
	char color = 'w';								// we always start the first move White
	gomPanel panel = new gomPanel();				// set the panel accessible from outside
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GomFrame frame = new GomFrame();
					frame.setVisible(true);
				
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public GomFrame() {
		setResizable(false);
		
		setBackground(Color.LIGHT_GRAY);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 466, 540);
		
		JMenuBar menuBar = new JMenuBar();						// create a JMenuBar
		setJMenuBar(menuBar);									// set the JMenuBar
		JMenu system = new JMenu ("System");					// initialize system
		menuBar.add(system);									// add system to the menu
		JMenuItem quit = new JMenuItem("Quit");					// Create a new Jmenu item "Quit"
		quit.addMouseListener(new MouseAdapter() {				// When quit gets clicked
			@Override
			public void mouseReleased(MouseEvent arg0) {
				System.exit(0);									// Quit the game				
			}
		});
		
		
		JMenuItem changeBG = new JMenuItem("Change Background");// Create a new Jmenu item "Change Background"
		changeBG.addMouseListener(new MouseAdapter() {				// When quit gets clicked
			@Override
			public void mouseReleased(MouseEvent arg0) {
				panel.changeBackground();								// call the method for changing the background				
			}
		});
		system.add (changeBG);	
		system.add (quit);										// add quit to the system_menu
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		for (int i=0; i<14; i++)								// Go through all rows and columns
		{
			for (int j=0; j<14; j++)
			{
				panel.board[i][j] = 'A';						// initialize the whole array as Available
			}
		}
		panel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent arg0) {
				if(arg0.getX()<20 || arg0.getX()>429 || arg0.getY()<20 || arg0.getY()>429)
					return;
				row = convertToRow(arg0.getY());				// get the valid row based on Y position
				col = convertToCol(arg0.getX());				// get the valid column based on X position
				
				if (isVal(row, col) == false )					// if the block is not available
					return;										
				storeMove(row, col);							// store the move in the array
				panel.repaint();								// paint the move
				if (checkWin(row, col) == true)
					showWin();
			}
		});
		panel.setBackground(new Color(240, 240, 240));
		panel.setBorder(new LineBorder(new Color(0, 0, 0)));
		
		JButton btnRestart = new JButton("Restart");
		btnRestart.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				for (int i=0; i<14; i++)								// Go through all rows and columns
				{
					for (int j=0; j<14; j++)
					{
						panel.board[i][j] = 'A';						// reset the whole array as Available
					}
				}
				top=-1;
				panel.repaint();
			}
		});
		
		JButton btnBack = new JButton("Back");
		btnBack.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(top<0)
					return;
				back();
				
				panel.repaint();
			}
		});
		
		JButton btnQuit = new JButton("Quit");
		btnQuit.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				System.exit(0);
			}
		});
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addComponent(panel, GroupLayout.DEFAULT_SIZE, 450, Short.MAX_VALUE)
					.addGap(0))
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(104)
					.addComponent(btnRestart)
					.addGap(18)
					.addComponent(btnBack, GroupLayout.PREFERRED_SIZE, 73, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addComponent(btnQuit, GroupLayout.DEFAULT_SIZE, 73, Short.MAX_VALUE)
					.addGap(95))
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addComponent(panel, GroupLayout.PREFERRED_SIZE, 450, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED, 28, Short.MAX_VALUE)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addComponent(btnQuit, Alignment.TRAILING)
						.addComponent(btnBack, Alignment.TRAILING)
						.addComponent(btnRestart, Alignment.TRAILING)))
		);
		gl_contentPane.linkSize(SwingConstants.VERTICAL, new Component[] {btnRestart, btnBack, btnQuit});
		contentPane.setLayout(gl_contentPane);
		
		
	}
	void back(){
		panel.board[lastRow[top]][lastCol[top]] = 'A';
		top--;
		if(color=='w'){
			color = 'b';
		}
		else
			color = 'w';
	}
	//\\ Convert the Y value to row //\\
	int convertToRow(int myY){
		if(myY%30 <15){
			return ((myY/30)-1);
		}
		return (myY/30);
	}
	//\\ Convert the Y value to col //\\
	int convertToCol(int myX){
		if(myX%30 <15){
			return ((myX/30)-1);
		}
		return (myX/30);
	}
	//\\ Check if the block is available to store a move on it//\\
	boolean isVal(int myRow, int myCol){
		if (panel.board[myRow][myCol] != 'A')
			return false;						// if not available return flase
		else
			return true;						// if available return true
	}
	//\\Store the moves in board//\\
	private void storeMove (int row, int col){
		
		panel.board[row][col] = color;
		if (color=='w')	color = 'b';			// change the color to black 
		else			color = 'w';			// change the color to white
		lastRow[top+1] = row;					//keep track of row of last move
		lastCol[top+1] = col;                 //keep track of col of last move
		top++;
	}
	//\\ check if it's a win move //\\
	public boolean checkWin(int row, int col){
		if (checkHWin(row, col) == true)		return true;	// if it's horizontal win		
		if (checkVWin(row, col) == true)		return true;	// if it's vertical win
		if (checkForDiagWin(row, col) == true)	return true;	// if it's forward diagonal win
		if (checkBackDiagWin(row, col) == true)	return true;	// if it's backward diagonal win
		return false;											// if it's not a win move return false
	}
	//\\ show the winning dialog //\\
	void showWin(){
		if (color == 'w')
			JOptionPane.showMessageDialog(new JFrame(), "Congradulations! Black won this game!", "Dialog", JOptionPane.WARNING_MESSAGE);
		else
			JOptionPane.showMessageDialog(new JFrame(), "Congradulations! White  won this game!", "Dialog", JOptionPane.WARNING_MESSAGE);
		for (int i=0; i<14; i++)								// Go through all rows and columns
		{
			for (int j=0; j<14; j++)
			{
				panel.board[i][j] = 'A';						// reset the whole array as Available
			}
		}
		top=-1;
		panel.repaint();
	}
	//\\ if it's horizontal win //\\
	boolean checkHWin(int row, int col){
		int sum = 1;						// number of same vertical colors
		while (col>0 && (panel.board[row][col]==panel.board[row][col-1]))
		{
			col--;
		}
		while (col<=12 && panel.board[row][col]==panel.board[row][col+1])
		{
			col++;
			sum++;
		}
		if (sum >= 5)
			return true;
		return false;
	}
	//\\ if it's vertical win //\\
	boolean checkVWin(int row, int col){
		int sum = 1;						// number of same vertical colors
		while (row>0 && (panel.board[row][col]==panel.board[row-1][col]))
		{
			row--;
		}
		while (row<=12 && panel.board[row][col]==panel.board[row+1][col])
		{
			row++;
			sum++;
		}
		if (sum >= 5)
			return true;
		return false;
	}
	//\\ if it's forward diagonal win //\\
	boolean checkForDiagWin(int row, int col){
		int sum = 1;						// number of same vertical colors
		while (row>0 && col<13 && (panel.board[row][col]==panel.board[row-1][col+1]))	// moving from bottom-left to top-right
		{
			row--;
			col++;
		}
		while (row<=12 && col>=1 && panel.board[row][col]==panel.board[row+1][col-1])	// moving from top-right to bottom-left
		{
			row++;
			col--;
			sum++;
		}
		if (sum >= 5)
			return true;
		return false;
	}
	//\\ if it's backward diagonal win //\\
	boolean checkBackDiagWin(int row, int col){	
		int sum = 1;						// number of same vertical colors
		while (row>0 && col>0 && (panel.board[row][col]==panel.board[row-1][col-1]))	// moving from bottom-right to top-left
		{
			row--;
			col--;
		}
		while (row<=12 && col<=12 && panel.board[row][col]==panel.board[row+1][col+1])	// moving from top-left to bottom-right
		{
			row++;
			col++;
			sum++;
		}
		if (sum >= 5)
			return true;
		return false;
	}
}
