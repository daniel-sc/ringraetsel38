package ringraetsel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;

public class Tiefensuche {

    private static final class StartTiefensuche<T> implements Callable<Boolean> {
	private final Integer firstIndex;
	private final ExecutorService e;
	private final CheckResult<T> resultChecker;
	private final List<List<Integer>> moves;
	private final List<Integer> move;
	private final AbstractZustand<T> current;
	private final int tiefe;
	private final List<Future<?>> tasks;

	private StartTiefensuche(Integer firstIndex, ExecutorService e, CheckResult<T> resultChecker,
		List<List<Integer>> moves, List<Integer> move, AbstractZustand<T> current, int tiefe,
		List<Future<?>> tasks) {
	    this.firstIndex = firstIndex;
	    this.e = e;
	    this.resultChecker = resultChecker;
	    this.moves = moves;
	    this.move = move;
	    this.current = current;
	    this.tiefe = tiefe;
	    this.tasks = tasks;
	}

	public Boolean call() throws Exception {

	    AbstractZustand<T> threadCurrent = current.getCopy();
	    threadCurrent.drehen(true, move);
	    boolean stopThread = resultChecker.checkResult(Collections.singletonList(firstIndex), threadCurrent);
	    if (stopThread) {
		stopThreads(e, tasks);
		return Boolean.TRUE;
	    }
	    tiefensucheNoStore(tiefe - 1, threadCurrent, new CheckResult<T>() {

		@Override
		public boolean checkResult(List<Integer> indexOfMoves, AbstractZustand<T> current) {
		    ArrayList<Integer> newIndexOfMoves = new ArrayList<Integer>(indexOfMoves.size() + 1);
		    newIndexOfMoves.add(firstIndex);
		    newIndexOfMoves.addAll(indexOfMoves);
		    boolean stopThread = resultChecker.checkResult(newIndexOfMoves, current);
		    if (stopThread) {
			stopThreads(e, tasks);
		    }
		    return true;
		}

	    }, moves);
	    return Boolean.FALSE;
	}
    }

    public static <T> void tiefensucheParallel(final int tiefe, final AbstractZustand<T> current,
	    final CheckResult<T> resultChecker, final List<List<Integer>> moves, int threads) {
	if (tiefe == 0)
	    return;
	final ExecutorService e = Executors.newFixedThreadPool(threads);
	// List<Callable<Boolean>> tiefensuchen = new
	// ArrayList<Callable<Boolean>>();
	final List<Future<?>> tasks = new ArrayList<>();
	ExecutorCompletionService<Boolean> service = new ExecutorCompletionService<Boolean>(e);

	for (int i = 0; i < moves.size(); i++) {
	    Future<Boolean> task = service.submit(new StartTiefensuche<T>(i, e, resultChecker, moves, moves.get(i),
		    current, tiefe, tasks));
	    tasks.add(task);
	}

	try {

	    for (int i = 0; i < moves.size(); i++) {
		service.take();
	    }

	    // System.out.println("start tasks..");
	    // List<Future<Boolean>> futures = e.invokeAll(tiefensuchen);
	    // System.out.println("startet tasks.");

	    // if (!e.awaitTermination(1, TimeUnit.SECONDS)) {
	    // System.out.println("shutting down..");
	    // e.shutdown();
	    // }

	    // while (!e.awaitTermination(1, TimeUnit.SECONDS)) {
	    // System.out.println("Terminate" + new Date() + e.isShutdown() +
	    // e.isTerminated());
	    // // Thread.sleep(10000);
	    // boolean done = true;
	    // System.out.println("tasks: " + tasks.size());
	    // for (Future<?> f : tasks) {
	    // if (!f.isDone())
	    // done = false;
	    // }
	    // if (done) {
	    // System.out.println("all tasks are done..");
	    // return;
	    // }
	    // }
	} catch (InterruptedException e1) {
	    e1.printStackTrace();
	}
	System.out.println("ended tiefensuche parallel.");

    }

    private static void stopThreads(ExecutorService e, List<Future<?>> tasks) {
	System.out.println("Stopping all threads..");
	// e.shutdownNow();
	for (Future<?> t : tasks) {
	    t.cancel(true);
	}
	System.out.println("stopped? " + e.isTerminated() + e.isShutdown());

    }

    /**
     * die erste drehung ist immer nach rechts
     * 
     * @param tiefe
     * @param current
     *            will be used and modified!
     * @param resultChecker
     *            will be called each iteration
     * @param moves
     */
    public static <T> void tiefensucheNoStore(int tiefe, AbstractZustand<T> current, CheckResult<T> resultChecker,
	    List<List<Integer>> moves) {

	List<Integer> indexOfMoves = new ArrayList<>();

	int noOfMoves = 0;

	while (!Tiefensuche.isDone(indexOfMoves, tiefe, moves.size())) {

	    int size = indexOfMoves.size();
	    if (size < tiefe) {
		current.drehen(noOfMoves % 2 == 0, moves.get(0)); // rechts
								  // faengts an
		noOfMoves += moves.get(0).size();
		indexOfMoves.add(0);
	    } else {
		while (size > 0 && indexOfMoves.get(size - 1).equals(moves.size() - 1)) {
		    int oldindex = indexOfMoves.remove(size - 1);
		    List<Integer> oldmove = moves.get(oldindex);
		    current.zurueckDrehen((noOfMoves - oldmove.size()) % 2 == 0, oldmove);

		    noOfMoves -= oldmove.size();
		    size--;
		}

		// zurueckdrehen
		int oldindex = indexOfMoves.remove(size - 1);
		List<Integer> oldmove = moves.get(oldindex);
		current.zurueckDrehen((noOfMoves - oldmove.size()) % 2 == 0, oldmove);

		noOfMoves -= oldmove.size();
		size--;

		// naechsten move drehen
		List<Integer> newmove = moves.get(oldindex + 1);

		current.drehen(noOfMoves % 2 == 0, newmove);
		indexOfMoves.add(oldindex + 1);
		noOfMoves += newmove.size();
		size++;
	    }

	    boolean stop = resultChecker.checkResult(indexOfMoves, current);
	    if (stop) {
		return;
	    }
	}

    }

    private static boolean isDone(List<Integer> zugIndizes, int tiefe, int anzZuege) {
	// System.out.println("tiefe=" + tiefe + ", anzZuege=" + anzZuege +
	// ", zugIndizes=" + zugIndizes);
	if (zugIndizes.size() != tiefe)
	    return false;
	for (Integer i : zugIndizes) {
	    if (i < anzZuege - 1) {
		return false;
	    }
	}
	return true;
    }

}
