package ringraetsel;

import java.util.List;

public interface CheckResult {
    public void checkMinimalDrehung(List<Integer> zugfolge, ZustandEindeutigeKugeln zustand,
	    ZustandEindeutigeKugeln start);
}