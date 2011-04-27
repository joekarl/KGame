/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kgame.entity.entityTypes;

import java.util.logging.Level;
import java.util.logging.Logger;
import kgame.entity.Entity;
import kgame.geometry.Rect2f;
import kgame.scheduling.MultitaskingScheduler;
import kgame.scheduling.ScheduledProcess;

/**
 *
 * @author karl ctr kirch
 */
public abstract class BaseEntity extends Entity {

    private float x, y, z;
    private float dx, dy, dz;
    private Rect2f bounds = new Rect2f();
    private Rect2f interpolatedBounds = new Rect2f();
    private float width, height;
    private long updateInterval = 100, accumulator;
    float interpolation;
    ScheduledProcess entityUpdateProc;

    {
        final Entity self = this;
        entityUpdateProc = new ScheduledProcess(updateInterval, true) {

            @Override
            public void run() {
                self.update(updateInterval);
            }
        };

        MultitaskingScheduler.defaultScheduler().addProcess(entityUpdateProc);
    }

    public static boolean isColliding(BaseEntity a, BaseEntity b) {
        if (a.getId() == b.getId()) {
            return false;
        }
        
        boolean isColliding = !((a.getInterpolatedBounds().getY1() > b.getInterpolatedBounds().getY2())
                || (a.getInterpolatedBounds().getY2() < b.getInterpolatedBounds().getY1())
                || (a.getInterpolatedBounds().getX1() > b.getInterpolatedBounds().getX2())
                || (a.getInterpolatedBounds().getX2() < b.getInterpolatedBounds().getX1()));

        return isColliding;
    }

    public void handleCollision(BaseEntity collidingEntity) {
    }

    public void killUpdater() {
        entityUpdateProc.setIsDead();
    }

    public Rect2f getInterpolatedBounds() {
        return interpolatedBounds;
    }

    @Override
    public void render(long delta) {
        accumulator += delta;
        interpolation = updateInterval / 1000f * (float) accumulator / (float) updateInterval;
        interpolatedBounds.setX1(bounds.getX1() + interpolation * dx);
        interpolatedBounds.setX2(bounds.getX2() + interpolation * dx);
        interpolatedBounds.setY1(bounds.getY1() + interpolation * dy);
        interpolatedBounds.setY2(bounds.getY2() + interpolation * dy);
    }

    @Override
    public void update(long delta) {
        accumulator = 0;
    }

    public float getInterpolation() {
        return interpolation;
    }

    public long getUpdateInterval() {
        return updateInterval;
    }

    public void setUpdateInterval(long updateInterval) {
        this.updateInterval = updateInterval;
        entityUpdateProc.setRequestedDT(updateInterval);
    }

    private void updateBounds() {
        float halfX = width / 2;
        float halfY = height / 2;
        bounds.setX1(x - halfX);
        bounds.setX2(x + halfX);
        bounds.setY1(y - halfY);
        bounds.setY2(y + halfY);
    }

    public Rect2f getBounds() {
        return bounds;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
        updateBounds();
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
        updateBounds();
    }

    public float getDx() {
        return dx;
    }

    public void setDx(float dx) {
        this.dx = dx;
    }

    public float getDy() {
        return dy;
    }

    public void setDy(float dy) {
        this.dy = dy;
    }

    public float getDz() {
        return dz;
    }

    public void setDz(float dz) {
        this.dz = dz;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
        updateBounds();
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
        updateBounds();
    }

    public float getZ() {
        return z;
    }

    public void setZ(float z) {
        this.z = z;
        updateBounds();
    }
}
