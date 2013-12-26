package ringraetsel;

import java.util.ArrayList;
import java.util.List;

public class ZustandEindeutigeKugeln {

    List<Integer> rechts = new ArrayList<>(20);
    List<Integer> links = new ArrayList<>(20);

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

    @Override
    public boolean equals(Object obj) {
	if (!(obj instanceof ZustandEindeutigeKugeln))
	    return false;
	ZustandEindeutigeKugeln z = (ZustandEindeutigeKugeln) obj;
	for (int i = 0; i < 20; i++) {
	    if (!rechts.get(i).equals(z.rechts.get(i)))
		return false;
	    if (!links.get(i).equals(z.links.get(i)))
		return false;
	}
	return true;
    }

    public ZustandEindeutigeKugeln() {
	for (int i = 0; i < 20; i++) {
	    rechts.add(0);
	    links.add(0);
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
	List<Integer> aktiv;
	List<Integer> passiv;
	weite = (weite + 20) % 20;
	if (rechte_seite) {
	    aktiv = rechts;
	    passiv = links;
	} else {
	    aktiv = links;
	    passiv = rechts;
	}

	List<Integer> backup = new ArrayList<>(aktiv);

	for (int i = 0; i < 20; i++) {
	    aktiv.set((i + weite) % 20, backup.get(i));
	}

	passiv.set(0, aktiv.get(0));
	passiv.set(15, aktiv.get(15));
    }

    /**
     * me=ist, z=war
     * 
     * @param z
     * @param max
     *            does restrict the comparision to maximum 'max' differences
     * @return
     */
    public List<Aenderung> vergleichen(ZustandEindeutigeKugeln z, int max) {
	List<Aenderung> result = new ArrayList<>();

	List<Integer> me = getLinear();
	List<Integer> other = z.getLinear();

	for (int i = 0; i < me.size(); i++) {
	    if (!me.get(i).equals(other.get(i))) {
		Aenderung a = new Aenderung();
		a.ist = me.get(i);
		a.war = other.get(i);
		a.index_linear = i;
		result.add(a);
		if (result.size() == max)
		    return result;
	    }
	}

	return result;
    }

    public List<Integer> getLinear() {
	List<Integer> result = new ArrayList<Integer>(rechts);
	for (int i = 1; i < 20; i++) {
	    if (i != 15)
		result.add(links.get(i));
	}

	return result;
    }

    public class Aenderung {
	Integer index_rechts = null;
	Integer index_links = null;
	Integer index_linear = null;
	Integer war = -1;
	Integer ist = -1;

	@Override
	public String toString() {
	    return index_linear + ": " + war + "->" + ist;
	}
    }

    @Override
    public String toString() {
	String result = "";

	result += "li:\n" + links;
	result += "\n";
	result += "re:\n" + rechts;

	return result;
    }

    public ZustandEindeutigeKugeln getCopy() {
	ZustandEindeutigeKugeln result = new ZustandEindeutigeKugeln();
	for (int i = 0; i < 20; i++) {
	    // result.rechts = new ArrayList<Integer>(rechts);
	    // result.rechts.set(i, new Integer(rechts.get(i)));
	    result.rechts.set(i, rechts.get(i));
	    // result.links = new ArrayList<Integer>(links);
	    // result.links.set(i, new Integer(links.get(i)));
	    result.links.set(i, links.get(i));
	}
	return result;
    }

}
