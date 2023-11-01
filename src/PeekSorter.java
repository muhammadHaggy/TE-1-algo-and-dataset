
import java.util.Arrays;
import java.util.Random;

public class PeekSorter implements Sorter {

    public static final boolean COUNT_MERGE_COSTS = true;
    /** total merge costs of all merge calls */
    public static long totalMergeCosts = 0;
    /**
     * Merges runs A[l..m-1] and A[m..r] in-place into A[l..r]
     * with Sedgewick's bitonic merge (Program 8.2 in Algorithms in C++)
     * using B as temporary storage.
     * B.length must be at least r+1.
     */
    public static void mergeRuns(int[] A, int l, int m, int r, int[] B) {
        --m;// mismatch in convention with Sedgewick
        int i, j;
        assert B.length >= r+1;
        if (COUNT_MERGE_COSTS) totalMergeCosts += (r-l+1);
        for (i = m+1; i > l; --i) B[i-1] = A[i-1];
        for (j = m; j < r; ++j) B[r+m-j] = A[j+1];
        for (int k = l; k <= r; ++k)
            A[k] = B[j] < B[i] ? B[j--] : B[i++];
    }



    /**
     * Reverse the specified range of the specified array.
     *
     * @param a  the array in which a range is to be reversed
     * @param lo the index of the first element in the range to be
     *           reversed
     * @param hi the index of the last element in the range to be
     *           reversed
     */
    public static void reverseRange(int[] a, int lo, int hi) {
        while (lo < hi) {
            int t = a[lo]; a[lo++] = a[hi]; a[hi--] = t;
        }
    }


    public static int extendWeaklyIncreasingRunLeft(final int[] A, int i, final int left) {
        while (i > left && A[i-1] <= A[i]) --i;
        return i;
    }

    public static int extendWeaklyIncreasingRunRight(final int[] A, int i, final int right) {
        while (i < right && A[i+1] >= A[i]) ++i;
        return i;
    }

    public static int extendStrictlyDecreasingRunLeft(final int[] A, int i, final int left) {
        while (i > left && A[i-1] > A[i]) --i;
        return i;
    }

    public static int extendStrictlyDecreasingRunRight(final int[] A, int i, final int right) {
        while (i < right && A[i+1] < A[i]) ++i;
        return i;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }

    public PeekSorter() {
    }

    @Override
    public void sort(final int[] A, final int left, final int right) {
        peeksort(A, left, right);
    }

    public static void peeksort(final int[] a, final int l, final int r) {
        int n = r - l + 1;
        peeksort(a, l, r, l, r, new int[n]);
    }

    public static void peeksort(int[] A, int left, int right, int leftRunEnd, int rightRunStart, final int[] B) {
        if (leftRunEnd == right || rightRunStart == left) return;

        int mid = left + ((right - left) >> 1);
        if (mid <= leftRunEnd) {
            // |XXXXXXXX|XX     X|
            peeksort(A, leftRunEnd+1, right, leftRunEnd+1,rightRunStart, B);
            mergeRuns(A, left, leftRunEnd+1, right, B);
        } else if (mid >= rightRunStart) {
            // |XX     X|XXXXXXXX|
            peeksort(A, left, rightRunStart-1, leftRunEnd, rightRunStart-1, B);
            mergeRuns(A, left, rightRunStart, right, B);
        } else {
            // find middle run
            final int i, j;
            if (A[mid] <= A[mid+1]) {
                i = extendWeaklyIncreasingRunLeft(A, mid, left == leftRunEnd ? left : leftRunEnd+1);
                j = mid+1 == rightRunStart ? mid : extendWeaklyIncreasingRunRight(A, mid+1, right == rightRunStart ? right : rightRunStart-1);
            } else {
                i = extendStrictlyDecreasingRunLeft(A, mid, left == leftRunEnd ? left : leftRunEnd+1);
                j = mid+1 == rightRunStart ? mid : extendStrictlyDecreasingRunRight(A, mid+1,right == rightRunStart ? right : rightRunStart-1);
                reverseRange(A, i, j);
            }
            if (i == left && j == right) return;
            if (mid - i < j - mid) {
                // |XX     x|xxxx   X|
                peeksort(A, left, i-1, leftRunEnd, i-1, B);
                peeksort(A, i, right, j, rightRunStart, B);
                mergeRuns(A,left, i, right, B);
            } else {
                // |XX   xxx|x      X|
                peeksort(A, left, j, leftRunEnd, i, B);
                peeksort(A, j+1, right, j+1, rightRunStart, B);
                mergeRuns(A,left, j+1, right, B);
            }
        }
    }




    public static void main(String[] args) {

        PeekSorter sorter = new PeekSorter();

        int[] A = {34, 12, 7, 23, 32, 5, 18, 82, 45, 15};
        System.out.println("Before sorting: " + Arrays.toString(A));
        sorter.sort(A);
        System.out.println("After sorting: " + Arrays.toString(A));



    }
}
