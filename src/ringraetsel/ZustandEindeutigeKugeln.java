package ringraetsel;

import ringraetsel.AbstractZustand;

public class ZustandEindeutigeKugeln extends AbstractZustand<Integer> {

    /*
     * 1. Schnittpunkt: rechts[0]=links[0]
     * 
     * 2. Schnittpunkt: rechts[15]=links[15]
     * 
     * ROT:10 BLAU:10 SCHWARZ:9 GELB:9
     */

    public void reset() {
	Integer counter = 0;
	for (int i = 0; i < 20; i++) {
	    rechts.set(i, new Integer(counter++));
	}
	for (int i = 1; i < 20; i++) {
	    if (i != 15) {
		links.set(i, new Integer(counter++));
	    }
	}
	links.set(15, new Integer(rechts.get(15)));
	links.set(0, new Integer(rechts.get(15)));

    }

    public ZustandEindeutigeKugeln() {
	for (int i = 0; i < 20; i++) {
	    rechts.add(0);
	    links.add(0);
	}

	reset();
    }

}
