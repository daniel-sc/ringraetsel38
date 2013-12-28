package ringraetsel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import ringraetsel.AbstractZustand.Aenderung;

public class Main {

    public static void main(String[] args) {

	Date startZeit = new Date();

	// solutionToOurProblem();
	// List<List<Integer>> basicMoves = getBasicMoves();
	// System.out.println(basicMoves.size());
	ZustandFarben start = new ZustandFarben();
	start.mix(10);

	ZustandFarben ziel = new ZustandFarben();
	ziel.reset();

	List<Integer> result = solveGreedy(start, ziel);

	System.out.println("L�sung (" + result.size() + "): " + result);

	// tiefensucheNoStore(5, start.fillCopy(new ZustandFarben()), new
	// CheckResult<Farbe>() {
	//
	// @Override
	// public void checkResult(List<Integer> indexOfMoves,
	// AbstractZustand<Farbe> current) {
	// if (current.equals(ziel)) {
	// System.out.println("YEAY!!!");
	// System.out.println("indexOfMoves: " + indexOfMoves);
	// printDiff(basicMoves, indexOfMoves, current.vergleichen(ziel, 4));
	// }
	// // List<Aenderung<Farbe>> diff = current.vergleichen(ziel, 4);
	// // if (diff.size() < 4) {
	// // printDiff(basicMoves, indexOfMoves, diff);
	// // }
	// }
	// }, basicMoves);

	Date end = new Date();

	System.out.println("TIME: " + (end.getTime() - startZeit.getTime()) / 1000 + " sek");
    }

    @SuppressWarnings("unused")
    private static void solutionToOurProblem() {
	final ZustandFarben ziel = new ZustandFarben();
	ziel.reset();
	final ZustandFarben start = new ZustandFarben();
	start.problem();
	Integer[] firstMoves = new Integer[] { 1, 15, 5, 5, 14, 15, 15, 5, 5, 19, 15, 5, 5, 16, 15, 15, 5, 5, 9, 15,
		15, 5, 16 };
	System.out.println("firstMoves.length=" + firstMoves.length);
	int i = 0;
	for (Integer move : firstMoves) {
	    start.drehen((i++ % 2) == 0, move);
	}
	System.out.println("DIFF: " + start.vergleichen(ziel, 5));

	Integer[] secondMoves = new Integer[] { 11, 5, 5, 15, 4, 5, 5, 15, 15, 9, 5, 15, 15, 16, 5, 5, 15, 15, 16, 5,
		15, 15, 9 };
	System.out.println("secondMoves.length=" + secondMoves.length);
	i++;
	for (Integer move : secondMoves) {
	    start.drehen((i++ % 2) == 0, move);
	}
	System.out.println("DIFF: " + start.vergleichen(ziel, 5));
    }

    @SuppressWarnings("unused")
    private static List<List<Integer>> getBasicMoves() {
	final List<List<Integer>> basicMoves = new ArrayList<List<Integer>>();

	final List<List<Integer>> moves = getSingleMoves();

	final ZustandEindeutigeKugeln start = new ZustandEindeutigeKugeln();

	// tiefe:5 -> 0 sek, 74 elemente
	// tiefe:6 -> 9 sek, 84? elemente
	// tiefe:7 -> 199 sek, 1558 elemente
	Tiefensuche.tiefensucheNoStore(5, start.getCopy(), new CheckResult<Integer>() {

	    @Override
	    public boolean checkResult(List<Integer> indexOfMoves, AbstractZustand<Integer> current) {
		if (indexOfMoves.size() == 0)
		    return false;
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
		return false;
	    }

	}, moves);
	return basicMoves;
    }

    private static List<List<Integer>> getSingleMoves() {
	final List<List<Integer>> moves = new ArrayList<List<Integer>>();
	for (int i = 0; i < 20; i++) {
	    moves.add(Collections.singletonList(new Integer(i)));
	}
	return moves;
    }

    public static ZustandFarben userInput() {
	BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
	try {
	    System.out.println("Rechte Seite (im Uhrzeigersinn):");
	    String rechts = "";

	    do {
		rechts = reader.readLine();
	    } while (rechts.length() != 20);

	    System.out.println("Linke Seite (gegen den Uhrzeigersinn):");

	    String links = "";
	    do {
		links = reader.readLine();
	    } while (links.length() != 20);

	    ZustandFarben result = new ZustandFarben();
	    for (int i = 0; i < 20; i++) {
		result.setRechts(i, Farbe.valueOf(rechts.toUpperCase().substring(i, i + 1)));
		result.setLinks(i, Farbe.valueOf(links.toUpperCase().substring(i, i + 1)));
	    }
	    return result;
	} catch (IOException e) {
	    e.printStackTrace();
	    return null;
	}
    }

    static boolean isDone(List<Integer> zugIndizes, int tiefe, int anzZuege) {
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

    /**
     * Loest start zu ziel mit einem 'greedy' ansatz
     * 
     * @param z
     * @return list of moves, starting right
     */
    private static <T> List<Integer> solveGreedy(AbstractZustand<T> start, final AbstractZustand<T> ziel) {
	List<List<Integer>> basicMoves = /* getBasicMoves() */getSingleMoves();
	List<Integer> result = new ArrayList<Integer>();

	final SolveGreedyContext context = new SolveGreedyContext();

	context.currentDiff = start.vergleichen(ziel, 38).size();
	int tiefe = 1;
	while (context.currentDiff > 0) {

	    context.indexOfMovesBest = null;
	    System.out.println("Differenz: " + context.currentDiff + ", Tiefe: " + tiefe + ", Z�ge bisher: "
		    + result.size());
	    Tiefensuche.tiefensucheNoStore(tiefe, start.getCopy(), new CheckResult<T>() {

		@Override
		public boolean checkResult(List<Integer> indexOfMoves, AbstractZustand<T> current) {
		    List<Aenderung<T>> diff = current.vergleichen(ziel, context.currentDiff);
		    if (diff.size() < context.currentDiff) {
			System.out.println("indexOfMoves: " + indexOfMoves);
			System.out.println("currentDiff(old): " + context.currentDiff);
			System.out.println("currentDiff(new): " + diff.size());

			context.indexOfMovesBest = new ArrayList<Integer>(indexOfMoves);
			context.currentDiff = diff.size();
			return true;
		    }
		    return false;

		}
	    }, basicMoves);

	    if (context.indexOfMovesBest != null) {
		List<Integer> currentMoves = new ArrayList<Integer>();
		for (Integer i : context.indexOfMovesBest) {
		    start.drehen(result.size() % 2 == 0, basicMoves.get(i));
		    result.addAll(basicMoves.get(i));
		    currentMoves.addAll(basicMoves.get(i));
		}

		// da die tiefensuche immer mit rechts startet muss hier ggf.
		// ein nullzug simuliert werden:
		if (result.size() % 2 == 1) {
		    result.add(0);
		}
		System.out.println("CHECK-Diff: " + start.vergleichen(ziel, 38).size());
		System.out.println("NEXT MOVES (start rechts): " + currentMoves);
		tiefe = 1;
		context.indexOfMovesBest = null;
	    } else {
		tiefe++;
	    }

	}

	zuegeKuerzen(result);
	return result;

    }

    private static class SolveGreedyContext {
	int currentDiff;
	List<Integer> indexOfMovesBest;
    }

    public static void zuegeKuerzen(List<Integer> zuege) {
	Integer integerNull = new Integer(0);
	int i = 1;
	while (i < zuege.size()) {
	    if (zuege.get(i).equals(integerNull)) {
		zuege.remove(i);
		if (zuege.size() > i) {
		    zuege.set(i - 1, (zuege.get(i - 1) + zuege.get(i)) % 20);
		    zuege.remove(i);
		}
	    }
	    i++;
	}
    }

    @SuppressWarnings("unused")
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
