/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kgame.core;

/**
 *
 * @author karl ctr kirch
 */
public abstract class MainLoop {

    private final long ticksPerSecond;
    private final long skipTicks;
    private final long maxFrameSkip;
    long nextTick = System.currentTimeMillis();
    int loops;
    float interpolation;
    volatile boolean running = true;

    public abstract void tick(long dt);

    public MainLoop(long ticksPerSecond, long maxFrameSkip) {
        this.ticksPerSecond = ticksPerSecond;
        skipTicks = 1000 / ticksPerSecond;
        this.maxFrameSkip = maxFrameSkip;
    }

    public void stop(){
        running = false;
    }

    public void run(){
        running = true;
        while (running) {
            loops = 0;
            while (System.currentTimeMillis() > nextTick && loops < maxFrameSkip) {
                tick(skipTicks);
                nextTick += skipTicks;
                loops++;
            }
        }
    }
}
