package ringraetsel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import ringraetsel.AbstractZustand.Aenderung;

public class Main {

    public static void main(String[] args) {

	Date startZeit = new Date();
	// tiefensuche(6, new ZustandEindeutigeKugeln());

	// tiefensucheNoStore(6, start, new CheckResult() {
	//
	// @Override
	// public void checkResult(List zugfolge, AbstracZustand current,
	// AbstracZustand start) {
	// if (ziel.equals(current)) {
	// System.out.println("YEAY!!");
	// System.out.println(zugfolge);
	// }
	//
	// }
	//
	// }, ZustandFarben.class);

	final List<List<Integer>> basicMoves = getBasicMoves();

	System.out.println("anzahl basic moves: " + basicMoves.size());

	final ZustandFarben start = new ZustandFarben();
	start.problem();
	final ZustandFarben ziel = new ZustandFarben();
	ziel.reset();

	tiefensucheNoStore(4, start.fillCopy(new ZustandFarben()), new CheckResult<Farbe>() {

	    @Override
	    public void checkResult(List<Integer> indexOfMoves, AbstractZustand<Farbe> current) {
		if (current.equals(ziel)) {
		    System.out.println("YEAY!!!");
		    System.out.println("indexOfMoves: " + indexOfMoves);
		}
		List<Aenderung<Farbe>> diff = current.vergleichen(ziel, 4);
		if (diff.size() < 4) {
		    System.out.println();
		    System.out.println("Check minimal (" + diff.size() + ") [" + indexOfMoves.size() + "]:");
		    System.out.println("indexOfMoves: " + indexOfMoves);
		    System.out.println("diff:");
		    System.out.println(diff);
		}
	    }
	}, basicMoves);

	// tiefensucheNoStore(6, start, new CheckResult() {
	//
	// @Override
	// public void checkResult(List zugfolge, AbstracZustand current,
	// AbstracZustand start) {
	// if (zugfolge.size() == 0)
	// return;
	// List<Aenderung> diff = current.vergleichen(start, 7);
	// if (diff.size() < 6) {
	// System.out.println();
	// System.out.println("Check minimal (" + diff.size() + "):");
	// System.out.println("zugfolge: " + zugfolge);
	// System.out.println("diff:");
	// System.out.println(diff);
	// // todo: anschauliche ausgabe
	// }
	//
	// }
	//
	// }, ZustandEindeutigeKugeln.class);

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
		    // System.out.println();
		    // System.out.println("Check minimal (" + diff.size() +
		    // ") [" + indexOfMoves.size() + "]:");
		    // System.out.println("indexOfMoves: " + indexOfMoves);
		    // System.out.println("diff:");
		    // System.out.println(diff);
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

}
