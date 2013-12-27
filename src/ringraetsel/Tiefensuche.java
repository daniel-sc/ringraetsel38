package ringraetsel;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Tiefensuche {

    public static <T> void tiefensucheParallel(int tiefe, final AbstractZustand<T> current,
	    CheckResult<T> resultChecker, List<List<Integer>> moves, int threads) {
	if (tiefe == 0)
	    return;
	ExecutorService e = Executors.newFixedThreadPool(threads);
	e.execute(new Runnable() {

	    @Override
	    public void run() {
		AbstractZustand<T> threadCurrent = current.getCopy();
	    }
	});

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

	List<List<Integer>> zugfolgen = new ArrayList<List<Integer>>();
	List<Integer> indexOfMoves = new ArrayList<>();

	int noOfMoves = 0;

	while (!Main.isDone(indexOfMoves, tiefe, moves.size())) {

	    int size = zugfolgen.size();
	    if (size < tiefe) {
		current.drehen(noOfMoves % 2 == 0, moves.get(0)); // rechts
								  // faengts an
		zugfolgen.add(moves.get(0));
		noOfMoves += moves.get(0).size();
		indexOfMoves.add(0);
	    } else {
		while (size > 0 && indexOfMoves.get(size - 1).equals(moves.size() - 1)) {
		    List<Integer> oldmove = zugfolgen.remove(size - 1);
		    indexOfMoves.remove(size - 1);
		    current.zurueckDrehen((noOfMoves - oldmove.size()) % 2 == 0, oldmove);

		    noOfMoves -= oldmove.size();
		    size--;
		}

		// zurueckdrehen
		List<Integer> oldmove = zugfolgen.remove(size - 1);
		int oldindex = indexOfMoves.remove(size - 1);
		current.zurueckDrehen((noOfMoves - oldmove.size()) % 2 == 0, oldmove);

		noOfMoves -= oldmove.size();
		size--;

		// naechsten move drehen
		List<Integer> newmove = moves.get(oldindex + 1);

		current.drehen(noOfMoves % 2 == 0, newmove);
		indexOfMoves.add(oldindex + 1);
		zugfolgen.add(newmove);
		noOfMoves += newmove.size();
		size++;
	    }

	    boolean stop = resultChecker.checkResult(indexOfMoves, current);
	    if (stop) {
		return;
	    }
	}

    }

}
