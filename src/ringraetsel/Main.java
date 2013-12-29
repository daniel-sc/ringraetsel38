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
	 start.mix(999);
//	ZustandFarben start = userInput();
	System.out.println(start);

	ZustandFarben ziel = new ZustandFarben();
	ziel.reset();

//	System.out.println("Diff: " + start.differenz(ziel, 38));
//	start.drehen(true, Arrays.asList(new Integer[] { 0, 0, 14 }));
//	System.out.println("Diff: " + start.differenz(ziel, 38));

	List<Integer> result = GreedySearch.solveGreedyParallel(start, ziel);
	System.out.println("Lösung (" + result.size() + "): " + result);

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

    static List<List<Integer>> getSingleMoves() {
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

	    } else {
		i++;
	    }
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
