import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Knapsack {

    public static final int SCALE = 10;

    public static void main(String[] args) throws Exception {
        KnapsackInput knapsackInput = readFile(new File("input.txt"));
        final int NUMBER_OF_ITEMS = knapsackInput.getNumberOfItems();
        final int MAXIMUM_CAPACITY = 3 * SCALE;

        int[] weight = getWeights(knapsackInput);
        int[] value = getValues(knapsackInput);

      
        int[][] opt = new int[NUMBER_OF_ITEMS + 1][MAXIMUM_CAPACITY + 1];
        boolean[][] sol = new boolean[NUMBER_OF_ITEMS + 1][MAXIMUM_CAPACITY + 1];

        for (int n = 1; n <= NUMBER_OF_ITEMS; n++) {
            for (int w = 1; w <= MAXIMUM_CAPACITY; w++) {

                int option1 = opt[n - 1][w];

                int option2 = Integer.MIN_VALUE;
                if (weight[n] <= w)
                    option2 = value[n] + opt[n - 1][w - weight[n]];

                opt[n][w] = Math.max(option1, option2);
                sol[n][w] = (option2 > option1);
            }
        }

        
        boolean[] take = new boolean[NUMBER_OF_ITEMS + 1];
        for (int n = NUMBER_OF_ITEMS, w = MAXIMUM_CAPACITY; n > 0; n--) {
            if (sol[n][w]) {
                take[n] = true;
                w = w - weight[n];
            } else {
                take[n] = false;
            }
        }

        
        KnapsackOutput knapsackOutput = processOutput(weight, value, take);
        writeFile(new File("output.txt"), knapsackOutput);
    }

    private static KnapsackOutput processOutput(int[] weight, int[] value, boolean[] take) {
        KnapsackOutput knapsackOutput = new KnapsackOutput();

        List<KnapsackItem> items = new ArrayList<KnapsackItem>();
        System.out.println("item" + "\t" + "value" + "\t" + "weight" + "\t" + "take");
        for (int i = 1; i < weight.length; i++) {
            if (take[i] == true) {
                double w = (double) weight[i] / SCALE;
                double v = (double) value[i] / SCALE;
                items.add(new KnapsackItem(w, v));
            }
            // print results to console for debug
            System.out.println(i + "\t" + value[i] + "\t" + weight[i] + "\t" + take[i]);
        }
        knapsackOutput.setItems(items);
        return knapsackOutput;
    }

    private static int[] getWeights(KnapsackInput knapsackInput) {
        List<KnapsackItem> items = knapsackInput.getItems();
        int[] weights = new int[items.size() + 1];
        for (int i = 1; i <= items.size(); i++) {
            weights[i] = (int) (items.get(i - 1).getWeight() * SCALE);
        }
        return weights;
    }

    private static int[] getValues(KnapsackInput knapsackInput) {
        List<KnapsackItem> items = knapsackInput.getItems();
        int[] values = new int[items.size() + 1];
        for (int i = 1; i <= items.size(); i++) {
            values[i] = (int) (items.get(i - 1).getValue() * SCALE);
        }
        return values;
    }

    private static KnapsackInput readFile(File fin) throws IOException {
        KnapsackInput knapsackInput = new KnapsackInput();
        FileInputStream fis = new FileInputStream(fin);
        //Construct BufferedReader from InputStreamReader
        BufferedReader br = new BufferedReader(new InputStreamReader(fis));

        int numberOfItems = Integer.valueOf(br.readLine());
        List<KnapsackItem> items = new ArrayList<KnapsackItem>();
        while (items.size() < numberOfItems) {
            String item = br.readLine();
            double weight = Double.valueOf(item.split(" ")[0]);
            double value = Double.valueOf(item.split(" ")[1]);
            items.add(new KnapsackItem(weight, value));
        }

        knapsackInput.setNumberOfItems(numberOfItems);
        knapsackInput.setItems(items);
        br.close();

        return knapsackInput;
    }

    private static void writeFile(File fo, KnapsackOutput knapsackOutput) throws IOException {
        FileOutputStream fos = new FileOutputStream(fo);
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));

        for (KnapsackItem item : knapsackOutput.getItems()) {
            bw.write(item.getWeight() + " " + item.getValue());
            bw.newLine();
        }

        bw.close();
    }

    static class KnapsackInput {
        private int numberOfItems;

        private List<KnapsackItem> items;

        public int getNumberOfItems() {
            return numberOfItems;
        }

        public void setNumberOfItems(int numberOfItems) {
            this.numberOfItems = numberOfItems;
        }

        public List<KnapsackItem> getItems() {
            return items;
        }

        public void setItems(List<KnapsackItem> items) {
            this.items = items;
        }
    }

    static class KnapsackOutput {

        private List<KnapsackItem> items;

        public List<KnapsackItem> getItems() {
            return items;
        }

        public void setItems(List<KnapsackItem> items) {
            this.items = items;
        }
    }

    static class KnapsackItem {

        private double weight;

        private double value;

        public KnapsackItem(double weight, double value) {
            this.weight = weight;
            this.value = value;
        }

        public double getWeight() {
            return weight;
        }

        public void setWeight(double weight) {
            this.weight = weight;
        }

        public double getValue() {
            return value;
        }

        public void setValue(double value) {
            this.value = value;
        }
    }
}