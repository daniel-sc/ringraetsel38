package ringraetsel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ringraetsel.AbstracZustand.Aenderung;

public class Main {

    @SuppressWarnings("unchecked")
    public static void main(String[] args) {

	Date startZeit = new Date();
	// tiefensuche(6, new ZustandEindeutigeKugeln());

	ZustandFarben start = new ZustandFarben();
	start.problem();
	final ZustandFarben ziel = new ZustandFarben();
	ziel.reset();

	tiefensucheNoStore(7, start, new CheckResult() {

	    @Override
	    public void checkResult(List zugfolge, AbstracZustand current, AbstracZustand start) {
		if (ziel.equals(current)) {
		    System.out.println("YEAY!!");
		    System.out.println(zugfolge);
		}

	    }

	}, ZustandFarben.class);
	
	
	tiefensucheNoStore(6, start, new CheckResult() {

	    @Override
	    public void checkResult(List zugfolge, AbstracZustand current, AbstracZustand start) {
		if (zugfolge.size() == 0)
		    return;
		List<Aenderung> diff = current.vergleichen(start, 7);
		if (diff.size() < 6) {
		    System.out.println();
		    System.out.println("Check minimal (" + diff.size() + "):");
		    System.out.println("zugfolge: " + zugfolge);
		    System.out.println("diff:");
		    System.out.println(diff);
		    // todo: anschauliche ausgabe
		}

	    }

	}, ZustandEindeutigeKugeln.class);
	
	
	
	Date end = new Date();

	System.out.println("TIME: " + (end.getTime() - startZeit.getTime()) / 1000 + " sek");
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
		List<Aenderung> diff = zustand.vergleichen(bak, 10);
		if (diff.size() <= 6 || diff.size() % 2 == 1) {
		    System.out.println("distrechts: " + distRechts);
		    System.out.println(diff);
		}
	    }
	}
    }

    /**
     * @deprecated use {@link #tiefensucheNoStore(int, ZustandEindeutigeKugeln)}
     * @param tiefe
     * @param start
     */
    public static void tiefensuche(int tiefe, ZustandEindeutigeKugeln start) {

	List<Integer> zugfolge = new ArrayList<Integer>();
	List<ZustandEindeutigeKugeln> zustanede = new ArrayList<ZustandEindeutigeKugeln>();

	while (!isDone(zugfolge, tiefe)) {

	    checkMinimalDrehung(zugfolge, zustanede, start);

	    int size = zugfolge.size();
	    if (size < tiefe) {
		ZustandEindeutigeKugeln current = new ZustandEindeutigeKugeln();
		if (size != 0) {
		    zustanede.get(size - 1).fillCopy(current);
		} else {
		    start.fillCopy(current);

		}
		current.drehen(size % 2 == 0, 1);
		zugfolge.add(1);
		zustanede.add(current);
	    } else {
		while (zugfolge.get(size - 1).equals(new Integer(19))) {
		    zugfolge.remove(size - 1);
		    zustanede.remove(size - 1);
		    size--;
		}
		int dist = zugfolge.get(size - 1) + 1;
		zugfolge.remove(size - 1);
		zustanede.remove(size - 1);
		size--;

		ZustandEindeutigeKugeln current = new ZustandEindeutigeKugeln();
		if (size != 0) {
		    zustanede.get(size - 1).fillCopy(current);
		} else {
		    start.fillCopy(current);

		}
		current.drehen(size % 2 == 0, dist);
		zugfolge.add(dist);
		zustanede.add(current);
	    }
	}

    }

    /**
     * checkt ob eine minimale/kleine aenderung stattgefunden hat und gibt diese
     * aus
     * 
     * @param zugfolge
     * @param zustanede
     * @param start
     */
    private static void checkMinimalDrehung(List<Integer> zugfolge, List<ZustandEindeutigeKugeln> zustanede,
	    ZustandEindeutigeKugeln start) {
	if (zugfolge.size() == 0)
	    return;
	else
	    checkMinimalDrehung(zugfolge, zustanede.get(zustanede.size() - 1), start);
    }

    private static void checkMinimalDrehung(List<Integer> zugfolge, ZustandEindeutigeKugeln zustand,
	    ZustandEindeutigeKugeln start) {
	if (zugfolge.size() == 0)
	    return;
	List<Aenderung> diff = zustand.vergleichen(start, 7);
	if (diff.size() < 7) {
	    System.out.println();
	    System.out.println("Check minimal (" + diff.size() + "):");
	    System.out.println("zugfolge: " + zugfolge);
	    System.out.println("diff:");
	    System.out.println(diff);
	    // todo: anschauliche ausgabe
	}
    }

    private static boolean isDone(List<Integer> zugfolge, int tiefe) {
	if (zugfolge.size() != tiefe)
	    return false;
	for (Integer i : zugfolge)
	    if (i < 19)
		return false;
	return true;
    }

    public static <T> void tiefensucheNoStore(int tiefe, AbstracZustand<T> start, CheckResult<T> resultChecker,
	    Class<? extends AbstracZustand<?>> type) {

	List<Integer> zugfolge = new ArrayList<Integer>();

	AbstracZustand<?> current;
	if (type.equals(ZustandFarben.class))
	    current = new ZustandFarben();
	else
	    current = new ZustandEindeutigeKugeln();

	start.fillCopy((AbstracZustand<T>) current);

	while (!isDone(zugfolge, tiefe)) {

	    int size = zugfolge.size();
	    if (size < tiefe) {
		current.drehen(size % 2 == 0, 1);
		zugfolge.add(1);
	    } else {
		while (zugfolge.get(size - 1).equals(new Integer(19))) {
		    int dist = zugfolge.remove(size - 1);
		    current.drehen((size + 1) % 2 == 0, 20 - dist); // TODO
								    // Check?
		    size--;
		}
		int dist = zugfolge.remove(size - 1);
		current.drehen((size + 1) % 2 == 0, 20 - dist); // TODO Check?
		size--;

		current.drehen(size % 2 == 0, dist + 1);
		zugfolge.add(dist + 1);
	    }

	    resultChecker.checkResult(zugfolge, (AbstracZustand<T>) current, start);
	}

    }
}
