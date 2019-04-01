/**
 * 27 feb. 2019
 */
package com.msp;

import java.awt.Color;
import java.awt.Graphics2D;

import com.Engine.Display.Graphics.RenderBatch;
import com.Engine.Display.Graphics.DefaultShapes.Rectangle;

/**
 * @author Boris Mulder
 *
 */
public class Tile extends Rectangle
{
	public static int Width = 24;
	public static int Height = 24;
	
	private int bombIndicatorNumber;
	private boolean active;
	private boolean bomb;
	private boolean flag;
	
	public Color background;
	
	public Tile(int x,int y)
	{
		super(x,y,Width,Height);
		this.active = false;
		this.flag = false;
		bombIndicatorNumber = 0;
	}
	
	@Override
	public void Draw(Graphics2D gfx, RenderBatch batch)
	{
		if(bomb && active)
		{
			gfx.setColor(background);
			gfx.fillRect(x, y, Width, Height);
			
			gfx.setColor(Color.black);
			gfx.fillOval(x, y, Width, Height);
		}
		else
		{
			super.Draw(gfx,batch);
		}
		
		gfx.setColor(Color.DARK_GRAY);
		gfx.drawRect(x, y, Width, Height);
	}
	
	public boolean isBomb()
	{
		return bomb;
	}
	
	public void setBomb()
	{
		background = Color.gray;
		bomb = true;
		bombIndicatorNumber = -1;
	}
	
	public String getBombIndicatorNumber()
	{
		return String.valueOf(bombIndicatorNumber);
	}
	public int getBombIndicatorNumberInt()
	{
		return bombIndicatorNumber;
	}

	public void incrementBombIndicatorNumber()
	{
		if(!isBomb())
		bombIndicatorNumber++;
	}

	public boolean isActive()
	{
		return active;
	}

	public void setActive(boolean active)
	{
		this.active = active;
	}
	
	public int getX()
	{
		return this.x;
	}
	
	public int getY() 
	{
		return this.y;
	}

	/**
	 * @return the flag
	 */
	public boolean isFlag()
	{
		return flag;
	}

	/**
	 * @param flag the flag to set
	 */
	public void setFlag(boolean flag)
	{
		this.flag = flag;
	}
}
