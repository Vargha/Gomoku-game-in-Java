import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.JPanel;


public class gomPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	public int bg = 1;									// the number of current background
	public char board[][] = new char [14][14];			// array of placement of rocks black and white
	public void paint(Graphics g){
		super.paint(g);
	BufferedImage img = null;
	try {
		String bgAddress = "bg/bg" + bg + ".jpg";
		img = ImageIO.read(new File(bgAddress));
	} catch (Exception e) { e.printStackTrace(); }
	
		g.drawImage(img, 0, 0, this);
		for(int i=0;i<15; i++)
		{
			g.drawLine(i*(getWidth()/15), 0, i*(getWidth()/15), getHeight()); //vertical lines
			g.drawLine(0, i*(getHeight()/15), getWidth(), i*(getHeight()/15));  //horizontal lines
		}
		for (int r=0; r<14; r++)
		{
			for (int c=0; c<14; c++)
			{
				if (board[r][c] == 'w')
				{
					g.setColor(new Color (255, 255, 255));
					g.fillOval(((c+1)*30)-10, ((r+1)*30)-10, 20, 20);
					
				}
				else if (board[r][c] == 'b')
				{
					g.setColor(new Color (70, 70, 70));
					g.fillOval(((c+1)*30)-10, ((r+1)*30)-10, 20, 20);
				}
			}
		}
	}
	public gomPanel() {

	}
	//\\ Change Background on each call //\\
		public void changeBackground(){
			if (bg == 5) bg = 1;
			else bg++;
			repaint();
			return;
		}

}
