package ringraetsel;

import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class TiefensucheTest {

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void testTiefensucheParallelTime() {

	List<List<Integer>> basicMoves = Main.getSingleMoves();

	ZustandFarben start = new ZustandFarben();
	start.mix(10);

	final ZustandFarben ziel = new ZustandFarben();
	ziel.reset();

	Date startZeit = new Date();
	Tiefensuche.tiefensucheNoStore(6, start, new CheckResult<Farbe>() {

	    @Override
	    public boolean checkResult(List<Integer> indexOfMoves, AbstractZustand<Farbe> current) {
		current.differenz(ziel, 5);
		return false;
	    }
	}, basicMoves);
	System.out.println("Zeit singleThread:" + (new Date().getTime() - startZeit.getTime()) / 1000 + " sek");

	startZeit = new Date();
	Tiefensuche.tiefensucheParallel(6, start, new CheckResult<Farbe>() {

	    @Override
	    public boolean checkResult(List<Integer> indexOfMoves, AbstractZustand<Farbe> current) {
		current.differenz(ziel, 5);
		return false;
	    }
	}, basicMoves, 3);
	System.out.println("Zeit multiThread: " + (new Date().getTime() - startZeit.getTime()) / 1000 + " sek");

    }

}
