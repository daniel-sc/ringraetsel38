package ringraetsel;

import java.util.List;

public interface CheckResult<T> {
    public void checkResult(List<Integer> zugfolge, AbstracZustand<T> current,
	    AbstracZustand<T> start);
}