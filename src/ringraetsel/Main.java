package ringraetsel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import ringraetsel.AbstractZustand.Aenderung;

public class Main {

    public static void main(String[] args) {

	Date startZeit = new Date();

	final List<List<Integer>> basicMoves = getBasicMoves();

	System.out.println("anzahl basic moves: " + basicMoves.size());

	final ZustandFarben ziel = new ZustandFarben();
	ziel.reset();
	final ZustandFarben start = new ZustandFarben();
	start.problem();
	Integer[] firstMoves = new Integer[] { 1, 15, 5, 5, 14, 15, 15, 5, 5, 19, 15, 5, 5, 16, 15, 15, 5, 5, 9, 15,
		15, 5, 16 };
	int i = 0;
	for (Integer move : firstMoves) {
	    start.drehen((i++ % 2) == 0, move);
	}
	System.out.println("DIFF: " + start.vergleichen(ziel, 5));

	tiefensucheNoStore(5, start.fillCopy(new ZustandFarben()), new CheckResult<Farbe>() {

	    @Override
	    public void checkResult(List<Integer> indexOfMoves, AbstractZustand<Farbe> current) {
		if (current.equals(ziel)) {
		    System.out.println("YEAY!!!");
		    System.out.println("indexOfMoves: " + indexOfMoves);
		    printDiff(basicMoves, indexOfMoves, current.vergleichen(ziel, 4));
		}
//		List<Aenderung<Farbe>> diff = current.vergleichen(ziel, 4);
//		if (diff.size() < 4) {
//		    printDiff(basicMoves, indexOfMoves, diff);
//		}
	    }
	}, basicMoves);

	Date end = new Date();

	System.out.println("TIME: " + (end.getTime() - startZeit.getTime()) / 1000 + " sek");
    }

    private static List<List<Integer>> getBasicMoves() {
	final List<List<Integer>> basicMoves = new ArrayList<List<Integer>>();

	final List<List<Integer>> moves = new ArrayList<List<Integer>>();
	for (int i = 1; i < 20; i++) {
	    moves.add(Collections.singletonList(new Integer(i)));
	}

	final ZustandEindeutigeKugeln start = new ZustandEindeutigeKugeln();

	tiefensucheNoStore(5, start.fillCopy(new ZustandEindeutigeKugeln()), new CheckResult<Integer>() {

	    @Override
	    public void checkResult(List<Integer> indexOfMoves, AbstractZustand<Integer> current) {
		if (indexOfMoves.size() == 0)
		    return;
		int max_diff = 4;
		List<Aenderung<Integer>> diff = current.vergleichen(start, max_diff + 1);
		if (diff.size() < max_diff + 1) {
		    // printDiff(moves, indexOfMoves, diff);
		    List<Integer> basicMove = new ArrayList<Integer>();
		    for (Integer index : indexOfMoves) {
			basicMove.add(moves.get(index).get(0));
		    }
		    basicMoves.add(basicMove);
		}
	    }

	}, moves);
	return basicMoves;
    }

    /**
     * @param z
     * @param distRechts
     *            >=0; <=19
     * @param distsLinks
     */
    public static void johannes_min1(ZustandEindeutigeKugeln z, int distRechts, int distsLinks) {
	z.drehen(true, distRechts);
	z.drehen(false, distsLinks);
	z.drehen(true, distsLinks);
	z.drehen(false, distRechts);
    }

    public static void johannes() {
	ZustandEindeutigeKugeln zustand = new ZustandEindeutigeKugeln();
	ZustandEindeutigeKugeln bak = new ZustandEindeutigeKugeln();
	zustand.fillCopy(bak);

	johannes_min1(zustand, -5, 5);

	System.out.println("änderung:");
	System.out.println(zustand.vergleichen(bak, 10));
    }

    public static void daniel() {
	for (int distLinks = 1; distLinks < 20; distLinks++) {
	    for (int distRechts = 1; distRechts < 20; distRechts++) {
		ZustandEindeutigeKugeln zustand = new ZustandEindeutigeKugeln();
		ZustandEindeutigeKugeln bak = new ZustandEindeutigeKugeln();
		zustand.fillCopy(bak);
		johannes_min1(zustand, distRechts, distLinks);
		List<Aenderung<Integer>> diff = zustand.vergleichen(bak, 10);
		if (diff.size() <= 6 || diff.size() % 2 == 1) {
		    System.out.println("distrechts: " + distRechts);
		    System.out.println(diff);
		}
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

    public static <T> void tiefensucheNoStore(int tiefe, AbstractZustand<T> current, CheckResult<T> resultChecker) {
	List<List<Integer>> moves = new ArrayList<List<Integer>>();
	for (int i = 1; i < 20; i++) {
	    moves.add(Collections.singletonList(new Integer(i)));
	}
	tiefensucheNoStore(tiefe, current, resultChecker, moves);
    }

    /**
     * @param tiefe
     * @param start
     *            will be used and modified!
     * @param resultChecker
     * @param moves
     * @param type
     */
    public static <T> void tiefensucheNoStore(int tiefe, AbstractZustand<T> current, CheckResult<T> resultChecker,
	    List<List<Integer>> moves) {

	List<List<Integer>> zugfolgen = new ArrayList<List<Integer>>();
	List<Integer> indexOfMoves = new ArrayList<>();

	int noOfMoves = 0;

	while (!isDone(indexOfMoves, tiefe, moves.size())) {

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

	    resultChecker.checkResult(indexOfMoves, current);
	}

    }

    private static void printDiff(final List<List<Integer>> moves, List<Integer> indexOfMoves, List<?> diff) {
	System.out.println();
	System.out.println("Check minimal (diffsize=" + diff.size() + ") no of moves=" + indexOfMoves.size() + ":");
	System.out.println("indexOfMoves: " + indexOfMoves);
	System.out.println("All moves: ");
	for (Integer i : indexOfMoves) {
	    System.out.print(moves.get(i) + ", ");
	}
	System.out.println("<<");
	System.out.println("diff:");
	System.out.println(diff);
    }

}
