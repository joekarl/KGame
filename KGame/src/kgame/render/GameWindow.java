/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kgame.render;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

/**
 * A window to display the game in LWJGL.
 *
 * @author Kevin Glass
 */
public class GameWindow {

    String title;
    private static GameWindow defaultWindow;

    public static GameWindow defaultWindow() {
        if (defaultWindow == null) {
            defaultWindow = new GameWindow();
        }
        return defaultWindow;
    }

    /**
     * Create a new game window
     */
    private GameWindow() {
        try {
            // find out what the current bits per pixel of the desktop is

            int currentBpp = Display.getDisplayMode().getBitsPerPixel();
            // find a display mode at 800x600

            DisplayMode mode = findDisplayMode(320, 240, currentBpp);

            // if can't find a mode, notify the user the give up

            if (mode == null) {
                Sys.alert("Error", "800x600x" + currentBpp + " display mode unavailable");
                return;
            }

            // configure and create the LWJGL display
            Display.setTitle(title);
            Display.setDisplayMode(mode);
            Display.setFullscreen(false);
            Display.setVSyncEnabled(false);

            Display.create();

            GL11.glMatrixMode(GL11.GL_PROJECTION);
            GL11.glLoadIdentity();
            GL11.glOrtho(0, Display.getDisplayMode().getWidth(), 0, Display.getDisplayMode().getHeight(), -1, 1);
            GL11.glMatrixMode(GL11.GL_MODELVIEW);

        } catch (LWJGLException e) {
            Sys.alert("Error", "Failed: " + e.getMessage());
        }
    }

    public void update() {
        Display.update();
    }

    public void clearBuffers() {
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT | GL11.GL_STENCIL_BUFFER_BIT);
        GL11.glLoadIdentity();
    }

    public boolean isCloseRequested() {
        return Display.isCloseRequested();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
        Display.setTitle(title);
    }

    /**
     * Determine an available display that matches the specified
     * parameters.
     *
     * @param width The desired width of the screen
     * @param height The desired height of the screen
     * @param bpp The desired color depth (bits per pixel) of the screen
     * @return The display mode matching the requirements or null
     * if none could be found
     * @throws LWJGLException Indicates a failure interacting with the LWJGL
     * library.
     */
    private DisplayMode findDisplayMode(int width, int height, int bpp) throws LWJGLException {
        DisplayMode[] modes = Display.getAvailableDisplayModes();
        DisplayMode mode = null;

        for (int i = 0; i < modes.length; i++) {
            if ((modes[i].getBitsPerPixel() == bpp) || (mode == null)) {
                if ((modes[i].getWidth() == width) && (modes[i].getHeight() == height)) {
                    mode = modes[i];
                }
            }
        }

        return mode;
    }

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
        GL11.glOrtho(0, Display.getDisplayMode().getWidth(), 0, Display.getDisplayMode().getHeight(), -1, 1);
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
}
