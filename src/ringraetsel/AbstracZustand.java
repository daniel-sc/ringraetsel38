package ringraetsel;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstracZustand<T> {

    public class Aenderung<S> {
        Integer index_rechts = null;
        Integer index_links = null;
        Integer index_linear = null;
        S war = null;
        S ist = null;
    
        @Override
        public String toString() {
	    // return index_linear + ": " + war + "->" + ist;
	    return war + "->" + ist;
        }
    }

    protected List<T> rechts = new ArrayList<>(20);
    protected List<T> links = new ArrayList<>(20);

    public AbstracZustand() {
	super();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof AbstracZustand<?>))
            return false;
        AbstracZustand z = (AbstracZustand) obj;
        for (int i = 0; i < 20; i++) {
            if (!rechts.get(i).equals(z.rechts.get(i)))
        	return false;
            if (!links.get(i).equals(z.links.get(i)))
        	return false;
        }
        return true;
    }

    /**
     * rechts dreht im uhrzeigersinn links dreht gegen uhrzeigersinn
     * 
     * @param rechte_seite
     * @param weite
     */
    public void drehen(boolean rechte_seite, int weite) {
        List<T> aktiv;
        List<T> passiv;
        weite = (weite + 20) % 20;
        if (rechte_seite) {
            aktiv = rechts;
            passiv = links;
        } else {
            aktiv = links;
            passiv = rechts;
        }
    
        List<T> backup = new ArrayList<>(aktiv);
    
        for (int i = 0; i < 20; i++) {
            aktiv.set((i + weite) % 20, backup.get(i));
        }
    
        passiv.set(0, aktiv.get(0));
        passiv.set(15, aktiv.get(15));
    }
    
    /**
     * Reset to solved state.
     */
    abstract public void reset();

    /**
     * me=ist, z=war
     * 
     * @param z
     * @param max
     *            does restrict the comparision to maximum 'max' differences
     * @return
     */
    public List<Aenderung> vergleichen(AbstracZustand<T> z, int max) {
        List<Aenderung> result = new ArrayList<>();
    
        List<T> me = getLinear();
        List<T> other = z.getLinear();
    
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

    public List<T> getLinear() {
        List<T> result = new ArrayList<T>(rechts);
        for (int i = 1; i < 20; i++) {
            if (i != 15)
        	result.add(links.get(i));
        }
    
        return result;
    }

    @Override
    public String toString() {
        String result = "";
    
        result += "li:\n" + links;
        result += "\n";
        result += "re:\n" + rechts;
    
        return result;
    }

    /**
     * fills result with values of this object.
     * @param result
     */
    public void fillCopy(AbstracZustand<T> result) {
        for (int i = 0; i < 20; i++) {
            // result.rechts = new ArrayList<Integer>(rechts);
            // result.rechts.set(i, new Integer(rechts.get(i)));
            result.rechts.set(i, rechts.get(i));
            // result.links = new ArrayList<Integer>(links);
            // result.links.set(i, new Integer(links.get(i)));
            result.links.set(i, links.get(i));
        }
    }

}