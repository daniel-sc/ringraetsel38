package ringraetsel;

import java.util.ArrayList;
import java.util.List;

import ringraetsel.ZustandFarben.Farbe;

public class ZustandFarben extends AbstracZustand<Farbe> {

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
	 * ROT:10 BLAU:10 SCHWARZ:9 GELB:9 tatsächlich ROT:10 Schwarz:10 blau:9
	 * GELB:9
	 */

	public void problem() {
		Farbe[] farbenreihe_rechts = new Farbe[] { R, G };
		Farbe[] farbenreihe_links = new Farbe[] { R, G };
		for (int i = 0; i < 20; i++) {
			rechts.set(i, farbenreihe_rechts[i]);
		}
		for (int i = 0; i < 20; i++) {
			links.set(i, farbenreihe_links[i]);
		}
	}

	public void reset() {
		// rechter kreis
		for (int i = 0; i < 5; i++) {
			rechts.set(i, Farbe.G);
		}
		for (int i = 5; i < 15; i++) {
			rechts.set(i, Farbe.S);
		}
		for (int i = 15; i < 16; i++) {
			rechts.set(i, Farbe.R);
		}
		for (int i = 16; i < 20; i++) {
			rechts.set(i, Farbe.G);
		}
		// linker kreis
		for (int i = 0; i < 1; i++) {
			links.set(i, Farbe.G);
		}
		for (int i = 1; i < 10; i++) {
			links.set(i, Farbe.B);
		}
		for (int i = 10; i < 20; i++) {
			links.set(i, Farbe.R);
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
