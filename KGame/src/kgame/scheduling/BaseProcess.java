/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package kgame.scheduling;

/**
 *
 * @author karl ctr kirch
 */
public abstract class BaseProcess {

    static int nextId = 0;

    private int id = nextId++;


    private boolean isDead = false;

    public final int getId(){
        return id;
    }

    public void setIsDead(){
        isDead = true;
    }

    public final boolean getIsDead(){
        return isDead;
    }

    public abstract void update(long dt);

    public void kill(){
        
    }
}
