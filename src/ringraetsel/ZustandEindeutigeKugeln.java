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
	    setRechts(i, new Integer(counter++));
	}
	for (int i = 1; i < 20; i++) {
	    if (i != 15) {
		setLinks(i, new Integer(counter++));
	    }
	}
	setLinks(15, new Integer(getRechts(15)));
	setLinks(0, new Integer(getRechts(0)));

    }

    public ZustandEindeutigeKugeln() {
	super(new Integer[20], new Integer[20]);

	reset();
    }

}
