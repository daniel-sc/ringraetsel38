package ringraetsel;

import java.util.List;

public interface CheckResult<T> {
    public void checkResult(List<Integer> indexOfMoves, AbstractZustand<T> current);
}