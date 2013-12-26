package ringraetsel;

import java.util.ArrayList;
import java.util.List;

import ringraetsel.ZustandFarben.Farbe;

public class ZustandFarben extends AbstracZustand<Farbe>{

    public enum Farbe {
	R, B, S, G;
    }

    List<Farbe> rechts = new ArrayList<Farbe>(20);
    List<Farbe> links = new ArrayList<Farbe>(20);

    /*
     * 1. Schnittpunkt: rechts[0]=links[0]
     * 
     * 2. Schnittpunkt: rechts[15]=links[15]
     * 
     * ROT:10 BLAU:10 SCHWARZ:9 GELB:9
     */

    public void reset() {
	for (int i = 1; i < 11; i++) {
	    rechts.set(i, Farbe.R);
	}
	for (int i = 11; i < 20; i++) {
	    rechts.set(i, Farbe.S);
	}
	links.set(15, Farbe.S);

	for (int i = 16; i < 26; i++) {
	    links.set(i % 20, Farbe.B);
	}
	rechts.set(0, Farbe.B);

	for (int i = 26; i < 26 + 9; i++) {
	    links.set(i % 20, Farbe.G);
	}
    }

    public ZustandFarben() {
	for (int i = 0; i < 20; i++) {
	    rechts.add(Farbe.B);
	    links.add(Farbe.B);
	}

	reset();
    }

    @Override
    public String toString() {
	String result = "";

	result += "li:\n" + links;
	result += "\n";
	result += "re:\n" + rechts;

	return result;
    }

}
