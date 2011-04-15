/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kgame.core;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author karl ctr kirch
 */
public abstract class MainLoop {

    private final long skipTicks;
    private final long maxFrameSkip;
    long nextTick;
    long NANOSECOND = 1000000L;
    int loops;
    float interpolation;
    volatile boolean running = true;

    public abstract void tick(long dt);

    public MainLoop(long ticksPerSecond, long maxFrameSkip) {
        skipTicks = 1000L * NANOSECOND / ticksPerSecond;
        this.maxFrameSkip = maxFrameSkip;
    }

    public void stop(){
        running = false;
    }

    public void run(){
        running = true;
        nextTick = System.nanoTime();
        while (running) {
            loops = 0;
            while (System.nanoTime() > nextTick && loops < maxFrameSkip) {
                tick(skipTicks);
                nextTick += skipTicks;
                loops++;
            }
            
            try {
                Thread.sleep(1);
            } catch (InterruptedException ex) {
                Logger.getLogger(MainLoop.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
