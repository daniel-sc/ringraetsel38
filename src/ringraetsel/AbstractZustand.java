package ringraetsel;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractZustand<T> {

    public static class Aenderung<S> {
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

    private final T[] rechts;
    private final T[] links;
    private int start_rechts = 0;
    private int start_links = 0;

    public AbstractZustand(T[] rechts, T[] links) {
	super();
	this.rechts = rechts;
	this.links = links;
    }
    
    /**
     * @param anzDrehungen
     * @return
     */
    public void mix(int anzDrehungen){
    	for (int i=0;i<anzDrehungen; i++){
    		
    		int high=19;
    		int low=1;
    		int drehzahl=(int) (Math.random() * (high - low) + low);
    		
    		drehen((i%2==0), drehzahl);
    	}   	
    }

    public T getRechts(int i) {
	return rechts[(start_rechts + i) % 20];
    }

    public T getLinks(int i) {
	return links[(start_links + i) % 20];
    }

    public void setRechts(int i, T t) {
	rechts[(start_rechts + i) % 20] = t;
    }

    public void setLinks(int i, T t) {
	links[(start_links + i) % 20] = t;
    }

    @Override
    public boolean equals(Object obj) {
	if (!(obj instanceof AbstractZustand<?>))
	    return false;
	AbstractZustand<?> z = (AbstractZustand<?>) obj;
	for (int i = 0; i < 20; i++) {
	    if (!rechts[i].equals(z.rechts[i]))
		return false;
	    if (!links[i].equals(z.links[i]))
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
	weite = weite + 20;
	if (rechte_seite) {
	    start_rechts = (start_rechts - weite + 20 + 20) % 20;
	    setLinks(0, getRechts(0));
	    setLinks(15, getRechts(15));
	} else {
	    start_links = (start_links - weite + 20 + 20) % 20;
	    setRechts(0, getLinks(0));
	    setRechts(15, getLinks(15));
	}

    }

    /**
     * rechts dreht im uhrzeigersinn links dreht gegen uhrzeigersinn
     * 
     * @param rechte_seite
     *            Seite erster move
     * @param weiten
     */
    public void drehen(boolean rechte_seite, List<Integer> weiten) {
	if (rechte_seite) {
	    for (int i = 0; i < weiten.size(); i++) {
		drehen(i % 2 == 0, weiten.get(i));
	    }
	} else {
	    for (int i = 0; i < weiten.size(); i++) {
		drehen(i % 2 == 1, weiten.get(i));
	    }
	}
    }

    /**
     * invers zu {@link #drehen(boolean, int)}, d.h. drehen(true, mylist) +
     * zurueckDrehen(true, mylist) fuehrt zum Ursprungszustand.
     * 
     * @param rechte_seite
     * @param weiten
     */
    public void zurueckDrehen(boolean rechte_seite, List<Integer> weiten) {
	if (rechte_seite) {
	    if (weiten.size() % 2 == 0) {
		// start links
		for (int i = 0; i < weiten.size(); i++) {
		    drehen(i % 2 == 1, -weiten.get(weiten.size() - 1 - i));
		}
	    } else {
		// start rechts
		for (int i = 0; i < weiten.size(); i++) {
		    drehen(i % 2 == 0, -weiten.get(weiten.size() - 1 - i));
		}
	    }
	} else {
	    if (weiten.size() % 2 == 1) {
		// start links
		for (int i = 0; i < weiten.size(); i++) {
		    drehen(i % 2 == 1, -weiten.get(weiten.size() - 1 - i));
		}
	    } else {
		// start rechts
		for (int i = 0; i < weiten.size(); i++) {
		    drehen(i % 2 == 0, -weiten.get(weiten.size() - 1 - i));
		}
	    }
	}

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
    public List<Aenderung<T>> vergleichen(AbstractZustand<T> z, int max) {
	List<Aenderung<T>> result = new ArrayList<>();

	for (int i = 0; i < 20; i++) {
	    if (!getRechts(i).equals(z.getRechts(i))) {
		Aenderung<T> a = new Aenderung<T>();
		a.ist = getRechts(i);
		a.war = z.getRechts(i);
		a.index_rechts = i;
		result.add(a);
		if (result.size() == max)
		    return result;
	    }
	}

	for (int i = 1; i < 20; i++) {
	    if (i != 15 && !getLinks(i).equals(z.getLinks(i))) {
		Aenderung<T> a = new Aenderung<T>();
		a.ist = getLinks(i);
		a.war = z.getLinks(i);
		a.index_links = i;
		result.add(a);
		if (result.size() == max)
		    return result;
	    }
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

    @SuppressWarnings("unchecked")
    public AbstractZustand<T> getCopy() {
	try {
	    AbstractZustand<T> result = this.getClass().newInstance();
	    for (int i = 0; i < 20; i++) {
		// result.rechts = new ArrayList<Integer>(rechts);
		// result.rechts.set(i, new Integer(rechts.[i]));
		result.rechts[i] = rechts[i];
		// result.links = new ArrayList<Integer>(links);
		// result.links.set(i, new Integer(links.[i]));
		result.links[i] = links[i];
	    }
	    return result;
	} catch (InstantiationException e) {
	    e.printStackTrace();
	} catch (IllegalAccessException e) {
	    e.printStackTrace();
	}
	return null;
    }

}