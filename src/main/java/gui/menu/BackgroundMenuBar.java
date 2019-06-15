package main.java.gui.menu;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JMenuBar;

/**
 * Clase BackGround MenuBar empleada en la interfaz visual de
 * {@link main.java.gui.ApplicationWindow ApplicationWindow} con fines estéticos
 * 
 * @author Mirza Ojeda Veira
 *
 */
public class BackgroundMenuBar extends JMenuBar {

	private static final long serialVersionUID = 1L;
	Color bgColor = Color.WHITE;

	public void setColor(Color color) {
		bgColor = color;
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.setColor(bgColor);
		g2d.fillRect(0, 0, getWidth() - 1, getHeight() - 1);

	}
}