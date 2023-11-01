import java.util.Arrays;

/**
 * @author Sebastian Wild (wild@uwaterloo.ca)
 */
public interface Sorter {
    /** Sorts A[left..right] (both endpoints inclusive) */
    void sort(int[] A, int left, int right);

    default void sort(int[] A) {
        sort(A, 0, A.length - 1);
    }

}