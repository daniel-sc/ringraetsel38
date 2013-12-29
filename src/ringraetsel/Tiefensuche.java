package ringraetsel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class Tiefensuche {

    private static final class StartTiefensuche<T> implements Callable<List<Integer>> {
	private final Integer firstIndex;
	private final CheckResult<T> resultChecker;
	private final List<List<Integer>> moves;
	private final List<Integer> move;
	private final int tiefe;
	private final AbstractZustand<T> threadCurrent;

	private StartTiefensuche(Integer firstIndex, CheckResult<T> resultChecker, List<List<Integer>> moves,
		List<Integer> move, AbstractZustand<T> current, int tiefe) {
	    this.firstIndex = firstIndex;
	    this.resultChecker = resultChecker;
	    this.moves = moves;
	    this.move = move;
	    this.tiefe = tiefe;
	    this.threadCurrent = current.getCopy();
	}

	public List<Integer> call() throws Exception {

	    threadCurrent.drehen(true, move);
	    List<Integer> indexOfMoves0 = new ArrayList<Integer>();
	    indexOfMoves0.add(firstIndex);
	    indexOfMoves0.add(0);

	    boolean stopThread = resultChecker.checkResult(Collections.singletonList(firstIndex), threadCurrent);
	    if (stopThread) {
		return Collections.singletonList(firstIndex);
	    }

	    return tiefensucheNoStore(tiefe - 1, threadCurrent, new CheckResult<T>() {

		@Override
		public boolean checkResult(List<Integer> indexOfMoves, AbstractZustand<T> current) {
		    if (Thread.interrupted())
			return true;
		    return resultChecker.checkResult(indexOfMoves, current);
		}

	    }, moves, indexOfMoves0);
	}
    }

    public static <T> List<Integer> tiefensucheParallel(final int tiefe, final AbstractZustand<T> current,
	    final CheckResult<T> resultChecker, final List<List<Integer>> moves, int threads) {
	if (tiefe == 0)
	    return null;
	final ExecutorService e = Executors.newFixedThreadPool(threads);
	ExecutorCompletionService<List<Integer>> service = new ExecutorCompletionService<List<Integer>>(e);

	final List<Future<?>> tasks = new ArrayList<>();
	for (int i = 0; i < moves.size(); i++) {
	    Future<List<Integer>> task = service.submit(new StartTiefensuche<T>(i, resultChecker, moves, moves.get(i),
		    current, tiefe));
	    tasks.add(task);
	}

	try {

	    for (int i = 0; i < moves.size(); i++) {
		try {
		    List<Integer> result = service.take().get();
		    // System.out.println("took: " + result);
		    if (result != null) {
			for (Future<?> task : tasks) {
			    task.cancel(true);
			}
			shutdownThreads(e);
			System.out.println("ended tiefensuche parallel with result.");
			return result;

		    }
		} catch (ExecutionException e1) {
		    e1.printStackTrace();
		}
	    }

	    shutdownThreads(e);
	    System.out.println("ended tiefensuche parallel.");
	    return null;

	} catch (InterruptedException e1) {
	    e1.printStackTrace();
	    return null;
	}
    }

    private static void shutdownThreads(final ExecutorService e) throws InterruptedException {
	// System.out.println("waiting for termination..");
	// if (!e.awaitTermination(100, TimeUnit.MILLISECONDS)) {
	System.out.println("doing shutdown..");
	e.shutdown();
	if (!e.awaitTermination(100, TimeUnit.MILLISECONDS)) {
	    System.out.println("shutdown now..");
	    e.shutdownNow();
	    if (!e.awaitTermination(100, TimeUnit.MILLISECONDS)) {
		System.err.println("COULD NOT TERMINATE!");
	    }
	}
	// }
    }

    public static <T> List<Integer> tiefensucheNoStore(int tiefe, AbstractZustand<T> current,
	    CheckResult<T> resultChecker, List<List<Integer>> moves) {
	return tiefensucheNoStore(tiefe, current, resultChecker, moves, new ArrayList<Integer>());
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
     * @param indexOfMoves
     *            already performed moves
     * @return move (=indexes of moves) for which resultChecker returned true,
     *         or {@code null} if none was found.
     */
    public static <T> List<Integer> tiefensucheNoStore(int tiefe, AbstractZustand<T> current,
	    CheckResult<T> resultChecker, List<List<Integer>> moves, List<Integer> indexOfMoves) {
	// note: indexOfMoves might be bigger than noOfMoves!
	int noOfMoves = 0;
	final int startSize = indexOfMoves.size();
	final Integer allMoves = moves.size() - 1;

	while (!Tiefensuche.isDone(indexOfMoves, startSize, tiefe, moves.size())) {

	    int size = indexOfMoves.size();
	    if (size - startSize < tiefe) {
		current.drehen(noOfMoves % 2 == 0, moves.get(0)); // rechts
								  // faengts an
		noOfMoves += moves.get(0).size();
		indexOfMoves.add(0);
	    } else {
		while (size - startSize > 0 && indexOfMoves.get(size - 1).equals(allMoves)) {
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
		if (indexOfMoves.size() > startSize)
		    return indexOfMoves;
		else
		    return null;
	    }
	}
	return null;
    }

    /**
     * @param zugIndizes
     * @param startSize
     *            only elements from index 'startSize' on in zugIndizes are
     *            considered
     * @param tiefe
     * @param anzZuege
     * @return
     */
    private static boolean isDone(List<Integer> zugIndizes, int startSize, int tiefe, int anzZuege) {
	// System.out.println("tiefe=" + tiefe + ", anzZuege=" + anzZuege +
	// ", zugIndizes=" + zugIndizes);
	if (zugIndizes.size() != tiefe + startSize)
	    return false;
	for (int i = startSize; i < zugIndizes.size(); i++) {
	    if (zugIndizes.get(i) < anzZuege - 1) {
		return false;
	    }
	}
	return true;
    }

}
