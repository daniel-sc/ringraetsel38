package ringraetsel;

import java.util.ArrayList;
import java.util.List;

public class Zustand {

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

    @Override
    public boolean equals(Object obj) {
	if (!(obj instanceof Zustand))
	    return false;
	Zustand z = (Zustand) obj;
	for (int i = 0; i < 20; i++) {
	    if (!rechts.get(i).equals(z.rechts.get(i)))
		return false;
	    if (!links.get(i).equals(z.links.get(i)))
		return false;
	}
	return true;
    }

    public Zustand() {
	for (int i = 0; i < 20; i++) {
	    rechts.add(Farbe.B);
	    links.add(Farbe.B);
	}

	reset();
    }

    /**
     * rechts dreht im uhrzeigersinn links dreht gegen uhrzeigersinn
     * 
     * @param rechte_seite
     * @param weite
     */
    public void drehen(boolean rechte_seite, int weite) {
	List<Farbe> aktiv;
	List<Farbe> passiv;
	if (rechte_seite) {
	    aktiv = rechts;
	    passiv = links;
	} else {
	    aktiv = links;
	    passiv = rechts;
	}

	List<Farbe> backup = new ArrayList<>(aktiv);

	for (int i = 0; i < 20; i++) {
	    aktiv.set((i + weite) % 20, backup.get(i));
	}

	passiv.set(0, aktiv.get(0));
	passiv.set(15, aktiv.get(15));
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
