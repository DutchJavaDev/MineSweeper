/**
 * 1 mrt. 2019
 */
package com.msp.util;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;

import com.Engine.Display.Graphics.RenderBatch;
import com.Engine.Display.Graphics.DefaultShapes.BaseShape;
import com.Engine.Utils.Constants;
import com.msp.MineSweeper;

/**
 * @author Boris Mulder
 *
 */
public class DisplayBox extends BaseShape
{
	private final String Message;
	private final int Width;
	private final int Height;
	
	
	private boolean stringSize = false;
	private int textWidth;
	private float alpha;
	private String fontName;
	
	/**
	 * Will Always center the box on the screen
	 */
	public DisplayBox(int width, int height, String text)
	{
		super(0, 0);
		alpha = 1f;
		Message = text;
		Width = width;
		Height = height;
		
		x = (MineSweeper.CLIENT_WIDTH / 2) - Width / 2;
		y = (MineSweeper.CLIENT_HEIGHT / 2) - Height / 2;
	}

	@Override
	public void Draw(Graphics2D gfx, RenderBatch batch)
	{
		if(!stringSize)
		{
			textWidth = batch.GetStringWidth(fontName, Message);
			stringSize = true;
		}
		
		
		gfx.setColor(color);
		gfx.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, Constants.Clamp(alpha, 0.0f, 1.0f)));
		gfx.fillRect(x, y, Width, Height);
		
		batch.setAlpha(1);
		batch.setColor(Color.red);
		batch.DrawString(Message, x+(((x + Width) / 4))-textWidth/4 , y + ((y + Height) / 2) / 2, fontName);
	}
	
	public void setAlpha(float a)
	{
		alpha = a;
	}
	
	public void setFont(String font)
	{
		fontName = font;
	}
}
