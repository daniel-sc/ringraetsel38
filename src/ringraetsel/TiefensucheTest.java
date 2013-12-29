package ringraetsel;

import static org.junit.Assert.*;

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

	ZustandFarben start = new ZustandFarben();
	start.mix(10);

	ZustandFarben ziel = new ZustandFarben();
	ziel.reset();

	Date startZeit = new Date();
	List<Integer> result = GreedySearch.solveGreedy(start.getCopy(), ziel.getCopy());
	System.out.println("Zeit singleThread:" + (new Date().getTime() - startZeit.getTime()) / 1000 + " sek");
	System.out.println("result.size = " + result.size());

	startZeit = new Date();
	GreedySearch.solveGreedyParallel(start.getCopy(), ziel.getCopy());
	System.out.println("Zeit multiThread: " + (new Date().getTime() - startZeit.getTime()) / 1000 + " sek");
	System.out.println("result.size = " + result.size());

    }

}
