import java.io.*;
import java.util.Random;

public class Main {

    public static void main(String[] args) {

        RadixSorter radixSorter = new RadixSorter();
        PeekSorter peekSorter = new PeekSorter();

        testSorter(radixSorter, "RadixSorter");
        testSorter(peekSorter, "PeekSorter");
    }

    public static int[] generateSorted(int size) {
        int[] arr = new int[size];
        for (int i = 0; i < size; i++) {
            arr[i] = i;
        }
        return arr;
    }

    public static int[] generateRandom(int size) {
        int[] arr = new int[size];
        Random rand = new Random();
        for (int i = 0; i < size; i++) {
            int randomNumber = rand.nextInt();
            if (randomNumber < 0) {
                randomNumber = -randomNumber;
            }
            arr[i] = randomNumber;
        }
        return arr;
    }

    public static int[] generateReversed(int size) {
        int[] arr = new int[size];
        for (int i = 0; i < size; i++) {
            arr[i] = size - i;
        }
        return arr;
    }

    public static int[] getOrGenerateData(String fileName, int size, String type) {
        File directory = new File("datasets");
        if (!directory.exists()) {
            directory.mkdirs();
        }

        // Update the file path to include the subfolder
        File file = new File(directory, fileName);

        // If file exists, read data from it
        if(file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                return (int[]) ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        int[] arr = null;
        switch (type) {
            case "sorted":
                arr = generateSorted(size);
                break;
            case "random":
                arr = generateRandom(size);
                break;
            case "reversed":
                arr = generateReversed(size);
                break;
        }

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
            oos.writeObject(arr);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return arr;
    }


    public static void testSorter(Sorter sorter, String sorterName) {

        System.out.println("Testing " + sorterName + ":");

        int[] smallSorted = getOrGenerateData("smallSorted.bin", 1000, "sorted");
        int[] smallRandom = getOrGenerateData("smallRandom.bin", 1000, "random");
        int[] smallReversed = getOrGenerateData("smallReversed.bin", 1000, "reversed");

        int[] mediumSorted = getOrGenerateData("mediumSorted.bin", 10000, "sorted");
        int[] mediumRandom = getOrGenerateData("mediumRandom.bin", 10000, "random");
        int[] mediumReversed = getOrGenerateData("mediumReversed.bin", 10000, "reversed");

        int[] largeSorted = getOrGenerateData("largeSorted.bin", 100000, "sorted");
        int[] largeRandom = getOrGenerateData("largeRandom.bin", 100000, "random");
        int[] largeReversed = getOrGenerateData("largeReversed.bin", 100000, "reversed");

        executeAndPrint(sorter, smallSorted, "smallSorted");
        executeAndPrint(sorter, smallRandom, "smallRandom");
        executeAndPrint(sorter, smallReversed, "smallReversed");

        executeAndPrint(sorter, mediumSorted, "mediumSorted");
        executeAndPrint(sorter, mediumRandom, "mediumRandom");
        executeAndPrint(sorter, mediumReversed, "mediumReversed");

        executeAndPrint(sorter, largeSorted, "largeSorted");
        executeAndPrint(sorter, largeRandom, "largeRandom");
        executeAndPrint(sorter, largeReversed, "largeReversed");

        System.out.println("-------------------------");
    }

    public static long getMemoryUsage() {
        Runtime runtime = Runtime.getRuntime();
        return runtime.totalMemory() - runtime.freeMemory();
    }

    public static void executeAndPrint(Sorter sorter, int[] arr, String arrName) {
        long startTime, endTime, startMemory, endMemory;

        startTime = System.nanoTime();
        startMemory = getMemoryUsage();
        sorter.sort(arr, 0, arr.length - 1);
        endTime = System.nanoTime();
        endMemory = getMemoryUsage();
        System.out.println("Sorted " + arrName + ". Execution time: " + (endTime - startTime) + " nanoseconds. Memory used: " + (endMemory - startMemory) + " bytes.");
    }
}
