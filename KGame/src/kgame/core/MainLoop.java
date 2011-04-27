/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kgame.core;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.lwjgl.Sys;

/**
 *
 * @author karl ctr kirch
 */
public abstract class MainLoop {

    private final long skipTicks;
    private final long maxFrameSkip;
    long nextTick, lastRender, currentTime;
    int loops;
    float interpolation;
    volatile boolean running = true;

    public abstract void tick(long dt);

    public void render(long dt) {
    }

    public MainLoop(long ticksPerSecond, long maxFrameSkip) {
        skipTicks = 1000L / ticksPerSecond;
        this.maxFrameSkip = maxFrameSkip;
    }

    public void stop() {
        running = false;
    }

    public void run() {
        running = true;
        nextTick = getTime();
        while (running) {
            loops = 0;
            while (getTime() > nextTick && loops < maxFrameSkip) {
                tick(skipTicks);
                nextTick += skipTicks;
                loops++;
            }
            currentTime = getTime();
            render(currentTime - lastRender);
            lastRender = currentTime;
        }
    }

    private long getTime() {
        return (Sys.getTime() * 1000) / Sys.getTimerResolution();
    }
}
