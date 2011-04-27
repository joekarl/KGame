/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kgame;

import java.util.Random;
import kgame.scheduling.MultitaskingScheduler;
import kgame.scheduling.ScheduledProcess;
import kgame.core.gameTypes.BasicGame;
import kgame.entity.Entity;
import kgame.entity.EntityManager;
import kgame.entity.entityTypes.BaseEntity;
import kgame.geometry.Rect2f;
import org.lwjgl.opengl.GL11;

/**
 *
 * @author karl ctr kirch
 */
public class Main extends BasicGame {

    String title;

    @Override
    public void update(long dt) {
        EntityManager.defaultManager().cleanDeadEntities();
        MultitaskingScheduler.defaultScheduler().tick(dt);
    }

    @Override
    public void render(long dt) {
        for (Entity entity : EntityManager.defaultManager().getEntities()) {
            GL11.glPushMatrix();
            entity.render(dt);
            GL11.glPopMatrix();
        }
    }

    @Override
    public void init() {
        title = "Test Game";
        setTitle(title);
        ScheduledProcess fpsProc = new ScheduledProcess(1000, true) {

            String fpsString = "%s [%.2f fps]";
            double fps;

            @Override
            public void run() {
                fps = getFps();
                setTitle(String.format(fpsString, title, fps));
            }
        };

        MultitaskingScheduler.defaultScheduler().addProcess(fpsProc);

        TestEntity e = null;
        for (int i = 0; i < 10000; ++i) {
            e = new TestEntity();
            e.setWidth(4);
            e.setHeight(4);
            EntityManager.defaultManager().addEntity(e);
        }

    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Main main = new Main();
    }
}

class TestEntity extends BaseEntity {

    Random r = new Random();
    int maxSpeed = 100;
    float speed = r.nextInt(maxSpeed) - maxSpeed / 2;
    float cr, cg, cb;

    public TestEntity() {
        this.setDx(r.nextInt(maxSpeed) - maxSpeed / 2);
        this.setDy(r.nextInt(maxSpeed) - maxSpeed / 2);
        this.setX(r.nextInt(800));
        this.setY(r.nextInt(600));
        cr = r.nextFloat();
        cg = r.nextFloat();
        cb = r.nextFloat();
        this.setUpdateInterval(r.nextInt(1000));
    }

    @Override
    public void render(long delta) {
        super.render(delta);

        GL11.glColor3f(cr, cg, cb);

        Rect2f bounds = getBounds();
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glVertex2f(bounds.getX1() + getInterpolation() * getDx(), bounds.getY1() + getInterpolation() * getDy());
        GL11.glVertex2f(bounds.getX1() + getInterpolation() * getDx(), bounds.getY2() + getInterpolation() * getDy());
        GL11.glVertex2f(bounds.getX2() + getInterpolation() * getDx(), bounds.getY2() + getInterpolation() * getDy());
        GL11.glVertex2f(bounds.getX2() + getInterpolation() * getDx(), bounds.getY1() + getInterpolation() * getDy());
        GL11.glEnd();
        bounds = null;
    }

    @Override
    public void update(long delta) {
        super.update(delta);
        this.setX(getX() + (delta / 1000f) * getDx());
        this.setY(getY() + (delta / 1000f) * getDy());
        Rect2f bounds = getBounds();
        if (bounds.getX1() <= 0) {
            speed = r.nextInt(maxSpeed);
            this.setDx(speed);
        }
        if (bounds.getX2() >= 800) {
            speed = r.nextInt(maxSpeed);
            this.setDx(-speed);
        }
        if (bounds.getY1() <= 0) {
            speed = r.nextInt(maxSpeed);
            this.setDy(speed);
        }
        if (bounds.getY2() >= 600) {
            speed = r.nextInt(maxSpeed);
            this.setDy(-speed);
        }
    }
}
