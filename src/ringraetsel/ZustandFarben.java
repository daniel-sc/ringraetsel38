package ringraetsel;

import static ringraetsel.Farbe.B;
import static ringraetsel.Farbe.G;
import static ringraetsel.Farbe.R;
import static ringraetsel.Farbe.S;

public class ZustandFarben extends AbstractZustand<Farbe> {

    /*
     * 1. Schnittpunkt: rechts[0]=links[0]
     * 
     * 2. Schnittpunkt: rechts[15]=links[15]
     * 
     * ROT:10 BLAU:10 SCHWARZ:9 GELB:9 tatsächlich ROT:10 Schwarz:10 blau:9
     * GELB:9
     */

    public void problem() {
	Farbe[] farbenreihe_rechts = new Farbe[] { G, G, G, G, G, S, S, S, S, S, S, S, S, S, S, R, B, G, G, B };
	Farbe[] farbenreihe_links = new Farbe[] { G, G, B, B, B, B, G, B, B, B, R, R, R, R, R, R, R, R, R, R };
	for (int i = 0; i < 20; i++) {
	    setRechts(i, farbenreihe_rechts[i]);
	}
	for (int i = 0; i < 20; i++) {
	    setLinks(i, farbenreihe_links[i]);
	}
    }

    public void reset() {
	// rechter kreis
	for (int i = 0; i < 5; i++) {
	    setRechts(i, Farbe.G);
	}
	for (int i = 5; i < 15; i++) {
	    setRechts(i, Farbe.S);
	}
	for (int i = 15; i < 16; i++) {
	    setRechts(i, Farbe.R);
	}
	for (int i = 16; i < 20; i++) {
	    setRechts(i, Farbe.G);
	}
	// linker kreis
	for (int i = 0; i < 1; i++) {
	    setLinks(i, Farbe.G);
	}
	for (int i = 1; i < 10; i++) {
	    setLinks(i, Farbe.B);
	}
	for (int i = 10; i < 20; i++) {
	    setLinks(i, Farbe.R);
	}

    }

    public ZustandFarben() {
	super(new Farbe[20], new Farbe[20]);

	reset();
    }



}
