/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package kgame.entity;

/**
 *
 * @author karl ctr kirch
 */
public abstract class Entity {

    static int nextId = 0;

    private int id = nextId++;

    boolean dead;

    public int getId(){
        return id;
    }

    public boolean isDead() {
        return dead;
    }

    public void setDead(boolean dead) {
        this.dead = dead;
    }

    public abstract void render(long delta);
    public abstract void update(long delta);
}
