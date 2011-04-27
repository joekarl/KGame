/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import junit.framework.Assert;
import kgame.scheduling.MultitaskingScheduler;
import kgame.scheduling.BaseProcess;
import kgame.scheduling.ChainProcess;
import kgame.core.MainLoop;
import kgame.scheduling.ScheduledProcess;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author karl ctr kirch
 */
public class SchedulerTest {

    public SchedulerTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {

    }

    @After
    public void tearDown() {
        MultitaskingScheduler.defaultScheduler().killAllProcesses();
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    @Test
    // <editor-fold defaultstate="collapsed" desc="testSchedulerCall">
    public void testSchedulerCall() {
        MultitaskingScheduler ms = MultitaskingScheduler.defaultScheduler();
        final ValueObject<Boolean> processWasCalled = new ValueObject<Boolean>();
        BaseProcess p = new BaseProcess() {

            @Override
            public void update(long dt) {
                processWasCalled.setValue(Boolean.TRUE);
            }
        };

        ms.addProcess(p);

        ms.tick(0);

        Assert.assertTrue(processWasCalled.getValue());
    }
    // </editor-fold>

    @Test
    // <editor-fold defaultstate="collapsed" desc="testSchedulerCalls">
    public void testSchedulerCalls() {
        MultitaskingScheduler ms = MultitaskingScheduler.defaultScheduler();
        final ValueObject<Integer> processCounter = new ValueObject<Integer>();
        processCounter.setValue(0);
        BaseProcess p = new BaseProcess() {

            @Override
            public void update(long dt) {
                Integer value = processCounter.getValue();
                processCounter.setValue(++value);
            }
        };

        ms.addProcess(p);

        Random r = new Random();
        int expectedCalls = r.nextInt(25);
        for (int i = 0; i < expectedCalls; ++i) {
            ms.tick(0);
        }

        Logger.getAnonymousLogger().log(Level.INFO, "Expected {0} calls. Got {1} calls.",
                new Object[]{expectedCalls, processCounter.getValue()});
        Assert.assertTrue(processCounter.getValue().equals(expectedCalls));
    }
// </editor-fold>

    @Test
    // <editor-fold defaultstate="collapsed" desc="testRemoveSelf">
    public void testRemoveSelf() {
        final MultitaskingScheduler ms = MultitaskingScheduler.defaultScheduler();
        final ValueObject<Boolean> processWasCalled = new ValueObject<Boolean>();
        BaseProcess p = new BaseProcess() {

            @Override
            public void update(long dt) {
                ms.removeProcess(this);
            }

            @Override
            public void kill() {
                processWasCalled.setValue(Boolean.TRUE);
            }
        };

        ms.addProcess(p);

        ms.tick(0);

        Assert.assertTrue(ms.getAllActiveProcesses().isEmpty());

        Assert.assertTrue(processWasCalled.getValue());
    }
    // </editor-fold>

    @Test
    // <editor-fold defaultstate="collapsed" desc="testScheduledProcess">
    public void testScheduledProcess() {
        final MultitaskingScheduler ms = MultitaskingScheduler.defaultScheduler();
        //sim main loop
        final MainLoop mainLoop = new MainLoop(100L, 5L) {

            @Override
            public void tick(long dt) {
                ms.tick(dt);
            }
        };

        //run every .1 second, 100 times
        BaseProcess p;


        for (int i = 0; i < 1000; ++i) {
            
            final ValueObject<Integer> processCounter = new ValueObject<Integer>();
            processCounter.setValue(0);
            p = new ScheduledProcess(100, 100, true) {

                long totalRunTime = System.currentTimeMillis();

                @Override
                public void run() {
                    Integer value = processCounter.getValue();
                    processCounter.setValue(++value);
                }

                @Override
                public void kill() {
                    long time = (System.currentTimeMillis() - totalRunTime);
                    if (time >= 10000 && time > 10000 + 30) {
                        System.out.println("time was too long. time was : " + time);
                    }
                }
            };

            ms.addProcess(p);
        }


        p = new ScheduledProcess(15000, 1) {

            @Override
            public void run() {
                mainLoop.stop();
                Assert.assertTrue(true);
                Logger.getAnonymousLogger().log(Level.INFO, "Process 3: stopping main loop");
            }
        };

        ms.addProcess(p);

        mainLoop.run();

    }
    // </editor-fold>

    @Test
    // <editor-fold defaultstate="collapsed" desc="testChainProcess">
    public void testChainProcess() {
        final MultitaskingScheduler ms = MultitaskingScheduler.defaultScheduler();
        ChainProcess p = new ChainProcess();

        final MainLoop mainLoop = new MainLoop(100L, 5L) {

            @Override
            public void tick(long dt) {
                ms.tick(dt);
            }
        };

        ms.addProcess(p);

        BaseProcess p1 = new ScheduledProcess(5000, 1) {

            @Override
            public void run() {
                Logger.getAnonymousLogger().log(Level.INFO, "Running p1");
            }
        };

        p.addProcess(p1);

        BaseProcess p2 = new ScheduledProcess(5000, 1) {

            @Override
            public void run() {
                Logger.getAnonymousLogger().log(Level.INFO, "Running p2");
            }
        };

        p.addProcess(p2);

        BaseProcess p3 = new ScheduledProcess(100, 100) {

            @Override
            public void kill() {
                Logger.getAnonymousLogger().log(Level.INFO, "Ran p3");
                Logger.getAnonymousLogger().log(Level.INFO, "Stopping main loop now");
                mainLoop.stop();
            }

            @Override
            public void run() {
            }
        };

        p.addProcess(p3);

        mainLoop.run();
    }
    // </editor-fold>

    @Test
    // <editor-fold defaultstate="collapsed" desc="testLoop">
    public void testLoop() {
        final MultitaskingScheduler ms = MultitaskingScheduler.defaultScheduler();
        //sim main loop
        final MainLoop mainLoop = new MainLoop(100L, 5L) {

            @Override
            public void tick(long dt) {
                ms.tick(dt);
            }
        };

        //run every .1 second, 100 times
        BaseProcess p;

        p = new ScheduledProcess(15000, 1) {

            @Override
            public void run() {
                mainLoop.stop();
                Assert.assertTrue(true);
                Logger.getAnonymousLogger().log(Level.INFO, "Process 3: stopping main loop");
            }
        };

        ms.addProcess(p);

        mainLoop.run();

    }
    // </editor-fold>
}
