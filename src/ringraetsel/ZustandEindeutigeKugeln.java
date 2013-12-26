package ringraetsel;

import ringraetsel.AbstracZustand;


public class ZustandEindeutigeKugeln extends AbstracZustand<Integer> {

    

    /*
     * 1. Schnittpunkt: rechts[0]=links[0]
     * 
     * 2. Schnittpunkt: rechts[15]=links[15]
     * 
     * ROT:10 BLAU:10 SCHWARZ:9 GELB:9
     */

    public void reset() {
	Integer counter = 0;
	for (int i = 1; i < 11; i++) {
	    rechts.set(i, new Integer(counter++));
	}
	for (int i = 11; i < 20; i++) {
	    rechts.set(i, new Integer(counter++));
	}
	links.set(15, new Integer(rechts.get(15)));

	for (int i = 16; i < 26; i++) {
	    links.set(i % 20, new Integer(counter++));
	}
	rechts.set(0, new Integer(links.get(0)));

	for (int i = 26; i < 26 + 9; i++) {
	    links.set(i % 20, new Integer(counter++));
	}
    }

    public ZustandEindeutigeKugeln() {
	for (int i = 0; i < 20; i++) {
	    rechts.add(0);
	    links.add(0);
	}

	reset();
    }
    

}
