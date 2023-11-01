import java.util.Arrays;
import java.util.Random;

public class RadixSorter implements Sorter {

    @Override
    public void sort(int[] A, int left, int right) {
        // Cari angka terbesar untuk mengetahui jumlah digit.
        int max = A[left];
        for (int i = left + 1; i <= right; i++) {
            if (A[i] > max) {
                max = A[i];
            }
        }

        // Proses setiap digit menggunakan counting sort.
        for (int exp = 1; max / exp > 0; exp *= 10) {
            countingSortByDigit(A, left, right, exp);
        }
    }

    private void countingSortByDigit(int[] A, int left, int right, int exp) {
        int n = right - left + 1;
        int[] output = new int[n];
        int[] count = new int[10];

        Arrays.fill(count, 0);

        // Simpan kemunculan pada count[]
        for (int i = left; i <= right; i++) {
            count[(A[i] / exp) % 10]++;
        }

        // Ubah count[i] sehingga count[i] mengandung
        // posisi sebenarnya dari digit pada output[]
        for (int i = 1; i < 10; i++) {
            count[i] += count[i - 1];
        }

        // Hitung array output
        for (int i = right; i >= left; i--) {
            output[count[(A[i] / exp) % 10] - 1] = A[i];
            count[(A[i] / exp) % 10]--;
        }

        // Salin array output ke A[], sehingga A[] sekarang
        // berisi angka yang terurut berdasarkan angka sekarang ini
        for (int i = left; i <= right; i++) {
            A[i] = output[i - left];
        }
    }


    public static void main(String[] args) {

        RadixSorter sorter = new RadixSorter();

        int[] A = {3, 7, 8, 5, 2, 1, 9, 5, 4};
        System.out.println("Before sorting: " + Arrays.toString(A));
        sorter.sort(A);
        System.out.println("After sorting: " + Arrays.toString(A));
    }

}
