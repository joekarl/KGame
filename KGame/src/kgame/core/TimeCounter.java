/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package kgame.core;

import org.lwjgl.Sys;

/**
 *
 * @author karl ctr kirch
 */
public class TimeCounter {
    long startTime,currentTime;
    double countsPerSecond;
    int count;

    public double getCountsPerSecond() {
        return countsPerSecond;
    }

    public void tick(){
        if(startTime == 0){
            startTime = (Sys.getTime() * 1000) / Sys.getTimerResolution();
        }
        currentTime = (Sys.getTime() * 1000) / Sys.getTimerResolution();
        count++;
        if(currentTime - startTime >= 500){
            countsPerSecond = count<<1;
            startTime = 0;
            currentTime = 0;
            count = 0;
        }
    }
}
