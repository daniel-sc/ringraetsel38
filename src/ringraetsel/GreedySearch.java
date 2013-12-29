package ringraetsel;

import java.util.ArrayList;
import java.util.List;

public class GreedySearch {

    private static class SolveGreedyContext {
        int currentDiff;
        List<Integer> indexOfMovesBest;
    }

    public GreedySearch() {
	// TODO Auto-generated constructor stub
    }

    /**
     * Loest start zu ziel mit einem 'greedy' ansatz
     * 
     * @param z
     * @return list of moves, starting right
     */
    public static <T> List<Integer> solveGreedy(AbstractZustand<T> start, final AbstractZustand<T> ziel) {
        List<List<Integer>> basicMoves = /* getBasicMoves() */Main.getSingleMoves();
        List<Integer> result = new ArrayList<Integer>();
    
        final GreedySearch.SolveGreedyContext context = new GreedySearch.SolveGreedyContext();
    
        context.currentDiff = start.vergleichen(ziel, 38).size();
        int tiefe = 1;
        while (context.currentDiff > 0) {
    
            context.indexOfMovesBest = null;
            System.out.println("Differenz: " + context.currentDiff + ", Tiefe: " + tiefe + ", Züge bisher: "
        	    + result.size());
            Tiefensuche.tiefensucheNoStore(tiefe, start.getCopy(), new CheckResult<T>() {
    
        	@Override
        	synchronized public boolean checkResult(List<Integer> indexOfMoves, AbstractZustand<T> current) {
        	    // List<Aenderung<T>> diff = current.vergleichen(ziel,
        	    // context.currentDiff);
        	    // synchronized (context) {
        	    int diff = current.differenz(ziel, context.currentDiff);
    
        	    if (diff < context.currentDiff) {
        		// System.out.println("indexOfMoves: " +
        		// indexOfMoves);
        		// System.out.println("currentDiff(old): " +
        		// context.currentDiff);
        		// System.out.println("currentDiff(new): " +
        		// diff.size());
        		// System.out.println(current);
        		context.indexOfMovesBest = new ArrayList<Integer>(indexOfMoves);
        		context.currentDiff = diff;
        		return true;
        	    }
        	    // }
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
        	// System.out.println(start);
        	System.out.println("NEXT MOVES (start rechts): " + currentMoves);
        	tiefe = 1;
        	context.indexOfMovesBest = null;
            } else {
        	tiefe++;
            }
    
        }
    
        Main.zuegeKuerzen(result);
        Main.zuegeKuerzen(result);
        return result;
    
    }

    /**
     * Loest start zu ziel mit einem 'greedy' ansatz
     * 
     * @param z
     * @return list of moves, starting right
     */
    public static <T> List<Integer> solveGreedyParallel(AbstractZustand<T> start, final AbstractZustand<T> ziel) {
        List<List<Integer>> basicMoves = /* getBasicMoves() */Main.getSingleMoves();
        List<Integer> result = new ArrayList<Integer>();
    
        final GreedySearch.SolveGreedyContext context = new GreedySearch.SolveGreedyContext();
    
        context.currentDiff = start.vergleichen(ziel, 38).size();
        int tiefe = 1;
        while (context.currentDiff > 0) {
    
            context.indexOfMovesBest = null;
            System.out.println("Differenz: " + context.currentDiff + ", Tiefe: " + tiefe + ", Züge bisher: "
        	    + result.size());
            Tiefensuche.tiefensucheParallel(tiefe, start.getCopy(), new CheckResult<T>() {
    
        	@Override
        	synchronized public boolean checkResult(List<Integer> indexOfMoves, AbstractZustand<T> current) {
        	    int diff = current.differenz(ziel, context.currentDiff);
    
        	    if (diff < context.currentDiff) {
        		context.indexOfMovesBest = new ArrayList<Integer>(indexOfMoves);
        		context.currentDiff = diff;
        		return true;
        	    }
        	    return false;
    
        	}
            }, basicMoves, 3);
    
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
        	// System.out.println(start);
        	System.out.println("NEXT MOVES (start rechts): " + currentMoves);
        	tiefe = 1;
        	context.indexOfMovesBest = null;
            } else {
        	tiefe++;
            }
    
        }
    
        Main.zuegeKuerzen(result);
        Main.zuegeKuerzen(result);
        return result;
    
    }

}
