/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kgame.core.gameTypes;

import java.util.logging.Level;
import java.util.logging.Logger;
import kgame.core.MainLoop;
import kgame.core.TimeCounter;
import kgame.entity.EntityManager;
import kgame.render.GameWindow;
import kgame.scheduling.MultitaskingScheduler;

/**
 *
 * @author karl ctr kirch
 */
public abstract class BasicGame {

    private String gameTitle = "Game";
    private GameWindow g;
    private TimeCounter fpsCounter;
    private BasicGame game = this;

    public abstract void update(long dt);

    public abstract void render(long dt);

    public void init() {
    }

    public BasicGame() {
        initGraphics();
        initGame();
        //g.enterOrtho();
        MainLoop mainLoop = new MainLoop(100, 5) {

            @Override
            public void tick(long dt) {
                EntityManager.defaultManager().cleanDeadEntities();
                MultitaskingScheduler.defaultScheduler().tick(dt);
            }

            @Override
            public void render(long dt) {
                g.clearBuffers();

                game.render(dt);

                fpsCounter.tick();

                g.update();
                if (g.isCloseRequested()) {
                    this.stop();
                }
            }
        };

        mainLoop.run();
    }

    private void initGraphics() {
        g = GameWindow.defaultWindow();
        g.setTitle(gameTitle);
    }

    private void initGame() {
        fpsCounter = new TimeCounter();
        init();
    }

    public void setTitle(String title) {
        g.setTitle(title);
    }

    public double getFps() {
        return fpsCounter.getCountsPerSecond();
    }
}
