package ringraetsel;

import java.util.List;

public interface CheckResult<T> {
    /**
     * @param indexOfMoves
     * @param current
     * @return {@code true} if the current run should be aborted
     */
    public boolean checkResult(List<Integer> indexOfMoves, AbstractZustand<T> current);
}