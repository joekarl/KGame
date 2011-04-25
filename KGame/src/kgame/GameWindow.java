/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package kgame;



import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;

/**
 * A window to display the game in LWJGL.
 *
 * @author Kevin Glass
 */
public class GameWindow {
	/** The list of game states currently registered */
	/** The current state being rendered */

	/**
	 * Create a new game window
	 */
	public GameWindow() {
		try {
			// find out what the current bits per pixel of the desktop is

			int currentBpp = Display.getDisplayMode().getBitsPerPixel();
			// find a display mode at 800x600

			DisplayMode mode = findDisplayMode(800, 600, currentBpp);

			// if can't find a mode, notify the user the give up

			if (mode == null) {
				Sys.alert("Error", "800x600x"+currentBpp+" display mode unavailable");
				return;
			}

			// configure and create the LWJGL display

			Display.setTitle("Asteroids Tutorial");
			Display.setDisplayMode(mode);
			Display.setFullscreen(false);



			Display.create();

			// initialise the game states

			//init();
		} catch (LWJGLException e) {
			e.printStackTrace();
			Sys.alert("Error", "Failed: "+e.getMessage());
		}
	}


	/**
	 * Get the current time in milliseconds based on the LWJGL
	 * high res system clock.
	 *
	 * @return The time in milliseconds based on the LWJGL high res clock
	 */
	private long getTime() {
		return (Sys.getTime() * 1000) / Sys.getTimerResolution();
	}

	/**
	 * Determine an available display that matches the specified
	 * paramaters.
	 *
	 * @param width The desired width of the screen
	 * @param height The desired height of the screen
	 * @param bpp The desired colour depth (bits per pixel) of the screen
	 * @return The display mode matching the requirements or null
	 * if none could be found
	 * @throws LWJGLException Indicates a failure interacting with the LWJGL
	 * library.
	 */
	private DisplayMode findDisplayMode(int width, int height, int bpp) throws LWJGLException {
		DisplayMode[] modes = Display.getAvailableDisplayModes();
		DisplayMode mode = null;

		for (int i=0;i<modes.length;i++) {
			if ((modes[i].getBitsPerPixel() == bpp) || (mode == null)) {
				if ((modes[i].getWidth() == width) && (modes[i].getHeight() == height)) {
					mode = modes[i];
				}
			}
		}

		return mode;
	}

	/**
	 * Add a game state to this window. This state can be used via
	 * its unique name.
	 *
	 * @param state The state to be added
	 * @see GameState.getName()
	 */

	/**
	 * Enter the orthographic mode by first recording the current state,
	 * next changing us into orthographic projection.
	 */
	public void enterOrtho() {
		// store the current state of the renderer

		GL11.glPushAttrib(GL11.GL_DEPTH_BUFFER_BIT | GL11.GL_ENABLE_BIT);
		GL11.glPushMatrix();
		GL11.glLoadIdentity();
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glPushMatrix();

		// now enter orthographic projection

		GL11.glLoadIdentity();
		GL11.glOrtho(0, 800, 600, 0, -1, 1);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glDisable(GL11.GL_LIGHTING);
	}

	/**
	 * Leave the orthographic mode by restoring the state we store
	 * in enterOrtho()
	 *
	 * @see enterOrtho()
	 */
	public void leaveOrtho() {
		// restore the state of the renderer

		GL11.glPopMatrix();
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glPopMatrix();
		GL11.glPopAttrib();
	}

	/**
	 * The entry point into our game. This method is called when you
	 * execute the program. Its simply responsible for creating the
	 * game window
	 *
	 * @param argv The command line arguments provided to the program
	 */

}

