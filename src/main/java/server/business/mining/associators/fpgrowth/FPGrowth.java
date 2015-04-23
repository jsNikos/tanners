package server.business.mining.associators.fpgrowth;

import java.io.FileNotFoundException;
import java.util.*;

public class FPGrowth {

    private int threshold;
    private Vector<FPtree> headerTable;
    private FPtree fptree;
    private Map<String, Integer> itemsMaptoFrequencies;
    private Map<ArrayList<String>, Integer> frequentPattern;
    private List<String> sortedItemsbyFrequencies;

    public Map<ArrayList<String>, Integer> getFrequentPattern() { return frequentPattern; }

    public FPGrowth(List<ArrayList<String>> allTransactions, int threshold) {
        this.threshold = threshold;
        fptree((ArrayList<ArrayList<String>>) allTransactions);
        buildFPGrowth(threshold, headerTable);
       // print();

    }

    private FPtree conditional_fptree_constructor(Map<String, Integer> conditionalPatternBase, Map<String, Integer> conditionalItemsMaptoFrequencies, int threshold, Vector<FPtree> conditional_headerTable) {
        //FPTree constructing
        //the null node!
        FPtree conditional_fptree = new FPtree("null");
        conditional_fptree.item = null;
        conditional_fptree.root = true;
        //remember our transactions here has oredering and non-frequent items for condition items
        for (String pattern : conditionalPatternBase.keySet()) {
            //adding to tree
            //removing non-frequents and making a vector instead of string
            Vector<String> pattern_vector = new Vector<String>();
            StringTokenizer tokenizer = new StringTokenizer(pattern);
            while (tokenizer.hasMoreTokens()) {
                String item = tokenizer.nextToken();
                if (conditionalItemsMaptoFrequencies.get(item) >= threshold)
                    pattern_vector.addElement(item);
            }
            //the insert method
            insert(pattern_vector, conditionalPatternBase.get(pattern), conditional_fptree, conditional_headerTable);
            //end of insert method
        }
        return conditional_fptree;
    }

    private void fptree(ArrayList<ArrayList<String>> allTransactions) {
        //preprocessing fields
        itemsMaptoFrequencies = new HashMap<String, Integer>();
        sortedItemsbyFrequencies = new LinkedList<String>();
        Vector<String> itemstoRemove = new Vector<String>();
        preProcessing(allTransactions, itemstoRemove);
        construct_fpTree(allTransactions, itemstoRemove);
    }

    private void preProcessing(ArrayList<ArrayList<String>> allTxs, Vector<String> itemstoRemove) {
       for (ArrayList<String> transaction : allTxs)
            for (String item : transaction) {
                if (itemsMaptoFrequencies.containsKey(item)) {
                    int count = itemsMaptoFrequencies.get(item);
                    itemsMaptoFrequencies.put(item, count + 1);
                } else
                    itemsMaptoFrequencies.put(item, 1);
            }
        //orderiiiiiiiiiiiiiiiiiiiiiiiiiiiing
        //also elimating non-frequents

        //for breakpoint for comparison
        sortedItemsbyFrequencies.add("null");
        itemsMaptoFrequencies.put("null", 0);
        for (String item : itemsMaptoFrequencies.keySet()) {
            int count = itemsMaptoFrequencies.get(item);
            int i = 0;
            for (String listItem : sortedItemsbyFrequencies) {
                if (itemsMaptoFrequencies.get(listItem) < count) {
                    sortedItemsbyFrequencies.add(i, item);
                    break;
                }
                i++;
            }
        }
        //removing non-frequents
        //this pichidegi is for concurrency problem in collection iterators
        for (String listItem : sortedItemsbyFrequencies)
            if (itemsMaptoFrequencies.get(listItem) < threshold)
                itemstoRemove.add(listItem);

        for (String itemtoRemove : itemstoRemove)
            sortedItemsbyFrequencies.remove(itemtoRemove);

    }

    @SuppressWarnings("unchecked")
	private void construct_fpTree( ArrayList<ArrayList<String>> allTransactions, Vector<String> itemstoRemove) {
        //HeaderTable Creation
        // first elements use just as pointers
        headerTable = new Vector<FPtree>();
        for (String itemsforTable : sortedItemsbyFrequencies)
            headerTable.add(new FPtree(itemsforTable));

        //FPTree constructing
        //the null node!
        fptree = new FPtree("null");
        fptree.item = null;
        fptree.root = true;
        //ordering frequent items transaction
        for (ArrayList<String> transaction : allTransactions) {
            Vector<String> transactionSortedbyFrequencies = new Vector<String>();
            for (String item : transaction) {
                if (itemstoRemove.contains(item))
                    continue;

                int index = 0;
                for (String vectorString : transactionSortedbyFrequencies) {
                    //some lines of condition is for alphabetically check in equals situatioans
                    if (itemsMaptoFrequencies.get(vectorString) < itemsMaptoFrequencies.get(item) || ((itemsMaptoFrequencies.get(vectorString) == itemsMaptoFrequencies.get(item)) && (vectorString.compareToIgnoreCase(item) < 0 ? true : false))) {
                        transactionSortedbyFrequencies.add(index, item);
                        break;
                    }
                    index++;
                }
                if (!transactionSortedbyFrequencies.contains(item))
                    transactionSortedbyFrequencies.add(item);
            }
            //adding to tree
            insert(transactionSortedbyFrequencies, fptree, headerTable);
            transactionSortedbyFrequencies.clear();
        }
        //headertable reverse ordering
        //first calculating item frequencies in tree
        for (FPtree item : headerTable) {
            int count = 0;
            FPtree itemtemp = item;
            while (itemtemp.next != null) {
                itemtemp = itemtemp.next;
                count += itemtemp.count;
            }
            item.count = count;
        }
        @SuppressWarnings("rawtypes")
		Comparator c = new FrequencyComparitorinHeaderTable();
        Collections.sort(headerTable, c);
    }

    void insert(Vector<String> transactionSortedbyFrequencies, FPtree fptree, Vector<FPtree> headerTable) {
        if (transactionSortedbyFrequencies.isEmpty())
            return;

        String itemtoAddtotree = transactionSortedbyFrequencies.firstElement();
        FPtree newNode = null;
        boolean ifisdone = false;
        for (FPtree child : fptree.children) {
            if (child.item.equals(itemtoAddtotree)) {
                newNode = child;
                child.count++;
                ifisdone = true;
                break;
            }
        }
        if (!ifisdone) {
            newNode = new FPtree(itemtoAddtotree);
            newNode.count = 1;
            newNode.parent = fptree;
            fptree.children.add(newNode);
            for (FPtree headerPointer : headerTable)
                if (headerPointer.item.equals(itemtoAddtotree)) {
                    while (headerPointer.next != null) {
                        headerPointer = headerPointer.next;
                    }
                    headerPointer.next = newNode;
                }
        }
        transactionSortedbyFrequencies.remove(0);
        insert(transactionSortedbyFrequencies, newNode, headerTable);
    }

    private void buildFPGrowth(int threshold, Vector<FPtree> headerTable) {
        frequentPattern = new HashMap<ArrayList<String>, Integer>();
        buildFPGrowth(null, threshold, headerTable, frequentPattern);
    }

    private void buildFPGrowth(ArrayList<String> base, int threshold, Vector<FPtree> headerTable, Map<ArrayList<String>, Integer> frequentPatterns) {
        for (FPtree iteminTree : headerTable) {
            ArrayList<String> currentPattern = new ArrayList<String>();
            if (base != null)
                for (String item : base)
                    currentPattern.add(item);
            currentPattern.add(iteminTree.item);

            int supportofCurrentPattern = 0;
            Map<String, Integer> conditionalPatternBase = new HashMap<String, Integer>();
            while (iteminTree.next != null) {
                iteminTree = iteminTree.next;
                supportofCurrentPattern += iteminTree.count;
                String conditionalPattern = null;
                FPtree conditionalItem = iteminTree.parent;

                while (!conditionalItem.isRoot()) {
                    conditionalPattern = conditionalItem.item + " " + (conditionalPattern != null ? conditionalPattern : "");
                    conditionalItem = conditionalItem.parent;
                }
                if (conditionalPattern != null)
                    conditionalPatternBase.put(conditionalPattern, iteminTree.count);

            }
            frequentPatterns.put(currentPattern, supportofCurrentPattern);
            //counting frequencies of single items in conditional pattern-base
            Map<String, Integer> conditionalItemsMaptoFrequencies = new HashMap<String, Integer>();
            for (String conditionalPattern : conditionalPatternBase.keySet()) {
                StringTokenizer tokenizer = new StringTokenizer(conditionalPattern);
                while (tokenizer.hasMoreTokens()) {
                    String item = tokenizer.nextToken();
                    if (conditionalItemsMaptoFrequencies.containsKey(item)) {
                        int count = conditionalItemsMaptoFrequencies.get(item);
                        count += conditionalPatternBase.get(conditionalPattern);
                        conditionalItemsMaptoFrequencies.put(item, count);
                    } else {
                        conditionalItemsMaptoFrequencies.put(item, conditionalPatternBase.get(conditionalPattern));
                    }
                }
            }
            //conditional fptree
            //HeaderTable Creation
            // first elements are being used just as pointers
            // non conditional frequents also will be removed
            Vector<FPtree> conditional_headerTable = new Vector<FPtree>();
            for (String itemsforTable : conditionalItemsMaptoFrequencies.keySet()) {
                int count = conditionalItemsMaptoFrequencies.get(itemsforTable);
                if (count < threshold)
                    continue;

                FPtree f = new FPtree(itemsforTable);
                f.count = count;
                conditional_headerTable.add(f);
            }
            FPtree conditional_fptree = conditional_fptree_constructor(conditionalPatternBase, conditionalItemsMaptoFrequencies, threshold, conditional_headerTable);
            //headertable reverse ordering
            Collections.sort(conditional_headerTable, new FrequencyComparitorinHeaderTable());

            if (!conditional_fptree.children.isEmpty())
                buildFPGrowth(currentPattern, threshold, conditional_headerTable, frequentPatterns);
        }
    }

    private void insert(Vector<String> pattern_vector, int count_of_pattern, FPtree conditional_fptree, Vector<FPtree> conditional_headerTable) {
        if (pattern_vector.isEmpty()) {
            return;
        }
        String itemtoAddtotree = pattern_vector.firstElement();
        FPtree newNode = null;
        boolean ifisdone = false;
        for (FPtree child : conditional_fptree.children) {
            if (child.item.equals(itemtoAddtotree)) {
                newNode = child;
                child.count += count_of_pattern;
                ifisdone = true;
                break;
            }
        }
        if (!ifisdone) {
            for (FPtree headerPointer : conditional_headerTable) {
                //this if also gurantees removing og non frequets
                if (headerPointer.item.equals(itemtoAddtotree)) {
                    newNode = new FPtree(itemtoAddtotree);
                    newNode.count = count_of_pattern;
                    newNode.parent = conditional_fptree;
                    conditional_fptree.children.add(newNode);
                    while (headerPointer.next != null) {
                        headerPointer = headerPointer.next;
                    }
                    headerPointer.next = newNode;
                }
            }
        }
        pattern_vector.remove(0);
        insert(pattern_vector, count_of_pattern, newNode, conditional_headerTable);
    }

    @SuppressWarnings("unused")
	private void print() throws FileNotFoundException {

        Vector<String> sortedItems = new Vector<String>();
        sortedItems.add("null");
        ArrayList<String> emptyList = new ArrayList<String>();

        frequentPattern.put(emptyList, 0);
        if (frequentPattern.keySet() != null)
            for (ArrayList<String> items : frequentPattern.keySet()) {
                int count = frequentPattern.get(items);

                int i = 0;
                for (String listItem : sortedItems) {
                    if (frequentPattern.get(listItem) < count) {
                        for (String item : items)
                        sortedItems.add(i, item);
                        break;
                    }
                    i++;
                }
            }

        @SuppressWarnings("resource")
		Formatter output = new Formatter("a.out");
        for (ArrayList<String> frequentPattern : this.frequentPattern.keySet())
            output.format("%s\t%d\n", frequentPattern, this.frequentPattern.get(frequentPattern));
    }
}
