/**
 * 22 feb. 2019
 */
package com.msp;

import java.awt.Color;
import java.awt.Font;
import java.util.Random;

import com.Engine.Application.GameApplication;
import com.Engine.Display.GameClient;
import com.Engine.Display.Graphics.RenderBatch;
import com.Engine.Display.Graphics.ResourceManager;
import com.msp.util.DisplayBox;
import com.msp.util.MatrixHelper;

/**
 * @author Boris Mulder
 *
 */
public class MineSweeper extends GameApplication
{
	/**
	 * Width of the jframe
	 */
	public static final int CLIENT_WIDTH = 640;
	
	/**
	 * Height of the jframe
	 */
	public static final int CLIENT_HEIGHT = 480;
	
	/**
	 * Space between the tiles
	 */
	private static int Padding = 2;

	/**
	 * Gets added to the start x position of the grid
	 */
	private static int OffsetX;

	/**
	 * Gets added to the start y position of the grid
	 */
	private static int OffsetY;

	/**
	 * Width of the grid
	 */
	private static int MatrixWidth;

	/**
	 * Height of the grid
	 */
	private static int MatrixHeight;

	/**
	 * Amount of bombs to spawn in the game
	 */
	private static int BOMB_COUNT;

	/**
	 * 2d array the tiles in the grid
	 */
	private Tile[][] tileMatrix;

	/**
	 * 2d array of the bombs in the grid
	 */
	private boolean[][] bombMatrix;
	
	/**
	 * When true then all mouse input will be blocked
	 */
	private boolean GAME_OVER;
	
	/**
	 * Indicates that the game has been reset
	 * that way the first mouse click will be ignored
	 */
	private boolean GAME_RESET;
	
	private DisplayBox gameOverBox;

	public MineSweeper()
	{
		super("MineSweeper");
		ResizeAble = false;
	}

	/**
	 * Gets called by the engine once!
	 */
	@Override
	public void LoadResources(ResourceManager manager)
	{
		OffsetX = 0;
		OffsetY = 0;
		
		MatrixWidth = CLIENT_WIDTH / Tile.Width;
		MatrixHeight = CLIENT_HEIGHT / Tile.Height;
		
		BOMB_COUNT = ((MatrixWidth + MatrixHeight));
		
		GAME_OVER = false;
		
		manager.LoadFont("game-over", "Trebuchet MS", Font.BOLD, 24);
		
		int width = 450;
		int height = 150;
		
		gameOverBox = new DisplayBox(width, height,"Game Over, click to restart");
		gameOverBox.setFont("game-over");
		gameOverBox.setAlpha(0.8f);
		gameOverBox.color = Color.DARK_GRAY;
		
		newGame();
	}

	void newGame()
	{
		tileMatrix = new Tile[MatrixHeight][MatrixWidth];
		bombMatrix = new boolean[MatrixHeight][MatrixWidth];
		
		generateTiles();
		generateLevel();
		
		if(GAME_OVER)
		{
			GAME_RESET = true;
		}
		
		GAME_OVER = false;
	}
	
	/**
	 * Generates bomb logic for the game
	 */
	void generateLevel()
	{
		for (int r = 0; r < tileMatrix.length; r++)
		{
			for (int c = 0; c < tileMatrix[r].length; c++)
			{
				Tile tile = tileMatrix[r][c];

				if (tile.isBomb())
				{
					incrementBombIndicator(r, c);
				}
			}
		}
	}

	void incrementBombIndicator(int r, int c)
	{	
		// top left tile
		if (MatrixHelper.isInRange(r - 1, c - 1, MatrixHeight, MatrixWidth))
			tileMatrix[r - 1][c - 1].incrementBombIndicatorNumber();

		// top tile
		if (MatrixHelper.isInRange(r - 1, c, MatrixHeight, MatrixWidth))
			tileMatrix[r - 1][c].incrementBombIndicatorNumber();

		// top right tile
		if (MatrixHelper.isInRange(r - 1, c + 1, MatrixHeight, MatrixWidth))
			tileMatrix[r - 1][c + 1].incrementBombIndicatorNumber();

		// center left tile
		if (MatrixHelper.isInRange(r, c - 1, MatrixHeight, MatrixWidth))
			tileMatrix[r][c - 1].incrementBombIndicatorNumber();

		// center right tile
		if (MatrixHelper.isInRange(r, c + 1, MatrixHeight, MatrixWidth))
			tileMatrix[r][c + 1].incrementBombIndicatorNumber();

		// bottom left tile
		if (MatrixHelper.isInRange(r + 1, c - 1, MatrixHeight, MatrixWidth))
			tileMatrix[r + 1][c - 1].incrementBombIndicatorNumber();

		// bottom tile
		if (MatrixHelper.isInRange(r + 1, c, MatrixHeight, MatrixWidth))
			tileMatrix[r + 1][c].incrementBombIndicatorNumber();

		// bottom right tile
		if (MatrixHelper.isInRange(r + 1, c + 1, MatrixHeight, MatrixWidth))
			tileMatrix[r + 1][c + 1].incrementBombIndicatorNumber();
	}

	/**
	 * Generates all the bombs
	 */
	void generateTiles()
	{
		Random rand = new Random();
		// bomb gen
		for (int index = 0; index < BOMB_COUNT; index++)
		{
			int bomX = rand.nextInt(MatrixWidth);
			int bomY = rand.nextInt(MatrixHeight);
			
			if(bombMatrix[bomY][bomX])
			{
				while(!bombMatrix[bomY][bomX])
				{
					bomX = rand.nextInt(MatrixWidth);
					bomY = rand.nextInt(MatrixHeight);
					
					bombMatrix[bomY][bomX] = true;
				}
			}
			else
			{
				bombMatrix[bomY][bomX] = true;
			}
			
			
		}

		int x = OffsetX;
		int y = OffsetY;

		// tile gen
		for (int r = 0; r < tileMatrix.length; r++)
		{
			for (int c = 0; c < tileMatrix[r].length; c++)
			{
				Tile tile = new Tile(x, y);
				if (bombMatrix[r][c])
				{
					tile.setBomb();
				}
				tile.color = Color.green;

				tileMatrix[r][c] = tile;

				x += Tile.Width + Padding;
			}
			x = OffsetX;
			y += Tile.Height + Padding;
		}

	}

	/**
	 * Gets called by the engine when the user presses a mouse button
	 */
	@Override
	public void mousePressed(ApplicationEvent button, int x, int y)
	{
		if(GAME_OVER)
		{
			// Reset
			newGame();
		}
	}

	/**
	 * Gets called by the engine when the user releases a mouse button
	 */
	@Override
	public void mouseReleased(ApplicationEvent button, int xpos, int ypos)
	{
		// This way when the game has been reset the first click will be ignored
		if(GAME_RESET)
		{
			GAME_RESET = false;
			return;
		}
		
		// Checks if the mouse click is inside the grid
		if ((xpos >= OffsetX && xpos <= OffsetX + (MatrixWidth * (Tile.Width + Padding)))
				&& (ypos >= OffsetY && ypos <= OffsetY + (MatrixHeight * (Tile.Height + Padding))) && !GAME_OVER)
		{
			// gets the 2d matrix position of the clicked tile
			int xIndex = (int) Math.round((xpos - OffsetX) / (Tile.Width + Padding));
			int yIndex = (int) Math.round((ypos - OffsetY) / (Tile.Height + Padding));

			if ((yIndex >= 0 && yIndex <= MatrixHeight - 1) && (xIndex >= 0 && xIndex <= MatrixWidth - 1)
					&& button == ApplicationEvent.MOUSE_LEFT)
			{
				Tile clickedTile = tileMatrix[yIndex][xIndex];

				if (clickedTile.isBomb())
				{
					// Game Over
					revealBombs();
					clickedTile.background = Color.red;
					GAME_OVER = true;
					
					return;
				} else if (!clickedTile.isActive())
				{
					if(clickedTile.getBombIndicatorNumberInt() == 0)
					{
						clickedTile.setActive(true);
						revealTilesRecursive(yIndex, xIndex);
					}
					else
					{
						clickedTile.setActive(true);
					}
				}

			}
			else if ((yIndex >= 0 && yIndex <= MatrixHeight - 1) && (xIndex >= 0 && xIndex <= MatrixWidth - 1)
					&& button == ApplicationEvent.MOUSE_RIGHT)
			{
				Tile clickedTile = tileMatrix[yIndex][xIndex];
				
				if(!clickedTile.isActive())
					clickedTile.setFlag(!clickedTile.isFlag());
			}
		}

	}
	
	void revealBombs()
	{
		for(int c = 0; c < tileMatrix.length; c++)
		{
			for(int r = 0; r < tileMatrix[c].length; r++)
			{
				Tile tile = tileMatrix[c][r];
				
				if(tile.isBomb())
				{
					tile.setActive(true);
				}
			}
		}
	}
	
	void revealTilesRecursive(int r,int c)
	{
		// top left tile
		if (MatrixHelper.isInRange(r - 1, c - 1, MatrixHeight, MatrixWidth))
		{
			if(!tileMatrix[r - 1][c - 1].isActive() && !tileMatrix[r - 1][c - 1].isBomb()) {
				tileMatrix[r - 1][c - 1].setActive(true);
				
				if(tileMatrix[r - 1][c - 1].getBombIndicatorNumberInt() == 0)
				{
					revealTilesRecursive(r - 1 , c - 1);
				}
				else
					return;
			}
		}

		// top tile
		if (MatrixHelper.isInRange(r - 1, c, MatrixHeight, MatrixWidth))
		{
			if(!tileMatrix[r - 1][c].isActive() && !tileMatrix[r - 1][c].isBomb()) {
				tileMatrix[r - 1][c].setActive(true);
				
				if(tileMatrix[r - 1][c].getBombIndicatorNumberInt() == 0)
				{
					revealTilesRecursive(r - 1 , c);
				}
				else
					return;
			}
		}

		// top right tile
		if (MatrixHelper.isInRange(r - 1, c + 1, MatrixHeight, MatrixWidth))
		{
			if(!tileMatrix[r - 1][c + 1].isActive() && !tileMatrix[r - 1][c + 1].isBomb()) {
				tileMatrix[r - 1][c + 1].setActive(true);
				
				if(tileMatrix[r - 1][c + 1].getBombIndicatorNumberInt() == 0)
				{
					revealTilesRecursive(r - 1 , c + 1);
				}
				else
					return;
			}
		}

		// center left tile
		if (MatrixHelper.isInRange(r, c - 1, MatrixHeight, MatrixWidth))
		{
			if(!tileMatrix[r][c - 1].isActive() && !tileMatrix[r][c - 1].isBomb()) {
				tileMatrix[r][c - 1].setActive(true);
				
				if(tileMatrix[r][c - 1].getBombIndicatorNumberInt() == 0)
				{
					revealTilesRecursive(r, c - 1);
				}
				else
					return;
			}
		}

		// center right tile
		if (MatrixHelper.isInRange(r, c + 1, MatrixHeight, MatrixWidth))
		{
			if(!tileMatrix[r][c + 1].isActive() && !tileMatrix[r][c + 1].isBomb()) {
				tileMatrix[r][c + 1].setActive(true);
				
				if(tileMatrix[r][c + 1].getBombIndicatorNumberInt() == 0)
				{
					revealTilesRecursive(r, c + 1);
				}
				else
					return;
			}
		}

		// bottom left tile
		if (MatrixHelper.isInRange(r + 1, c - 1, MatrixHeight, MatrixWidth))
		{
			if(!tileMatrix[r + 1][c - 1].isActive() && !tileMatrix[r + 1][c - 1].isBomb()) {
				tileMatrix[r + 1][c - 1].setActive(true);
				
				if(tileMatrix[r + 1][c - 1].getBombIndicatorNumberInt() == 0)
				{
					revealTilesRecursive(r + 1, c - 1);
				}
				else
					return;
			}
		}

		// bottom tile
		if (MatrixHelper.isInRange(r + 1, c, MatrixHeight, MatrixWidth))
		{
			if(!tileMatrix[r + 1][c].isActive() && !tileMatrix[r + 1][c].isBomb()) {
				tileMatrix[r + 1][c].setActive(true);
				
				if(tileMatrix[r + 1][c].getBombIndicatorNumberInt() == 0)
				{
					revealTilesRecursive(r + 1, c);
				}
				else
					return;
			}
		}

		// bottom right tile
		if (MatrixHelper.isInRange(r + 1, c + 1, MatrixHeight, MatrixWidth))
		{
			if(!tileMatrix[r + 1][c + 1].isActive() && !tileMatrix[r + 1][c + 1].isBomb()) {
				tileMatrix[r + 1][c + 1].setActive(true);
				
				if(tileMatrix[r + 1][c + 1].getBombIndicatorNumberInt() == 0)
				{
					revealTilesRecursive(r + 1, c + 1);
				}
				else
					return;
			}
		}
	}
	
	/**
	 * Gets called by the engine to update the game
	 */
	@Override
	protected void UpdateGame()
	{
	}

	/**
	 * Gets called by the engine to render the game
	 */
	@Override
	protected void RenderGame(RenderBatch batch)
	{
		batch.Begin();
		for (int r = 0; r < tileMatrix.length; r++)
		{
			for (int c = 0; c < tileMatrix[r].length; c++)
			{
				Tile tile = tileMatrix[r][c];
				
				// Fixe's a bug when the game resets
				if(tile == null) continue;
				
				batch.DrawShape(tile);

				if (tile.isActive() && !tile.isFlag())
				{
					batch.setColor(Color.black);
					
					batch.DrawString(tile.getBombIndicatorNumber(), tile.getX() + Tile.Width / 2 - Tile.Width / 4 + Padding,
							tile.getY() + Tile.Height / 2 + (Tile.Height / 4) + Padding / 2);
				}
				else if(tile.isFlag())
				{
					batch.setColor(Color.red);
					batch.DrawString("**", tile.getX() + Tile.Width / 2 - Tile.Width / 4 + Padding,
							tile.getY() + Tile.Height / 2 + (Tile.Height / 4) + Padding / 2);
				}
			}
		}
		
		if(GAME_OVER)
		{
			batch.DrawShape(gameOverBox);
		}
			
		batch.End();
	}

	public static void main(String[] args)
	{
		// New instance of the game
		new GameClient(new MineSweeper(), CLIENT_WIDTH, CLIENT_HEIGHT);
	}

}
