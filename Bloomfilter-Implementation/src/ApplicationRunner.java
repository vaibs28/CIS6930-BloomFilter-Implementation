
public class ApplicationRunner {

  public static void main(String[] args) {
    System.out.println("********BLOOM FILTER IMPLEMENTATION***********");
    BloomFilter bf = new BloomFilter(10000, 7, 1000);

    // set A
    bf.generateElements();
    bf.encodeElements();
    bf.lookupElements();
    System.out.println("Found in set A = " + bf.numFound);

    // set B
    bf.generateElements();
    bf.lookupElements();
    System.out.println("Found in set B = " + bf.numFound + "\n");

    System.out.println("********COUNTING BLOOM FILTER IMPLEMENTATION***********");
    CountingBloomFilter cbf = new CountingBloomFilter(1000, 10000, 7, 500, 500);
    cbf.generateElements();
    cbf.encodeElements();
    cbf.removeElements();
    cbf.addElements();
    cbf.lookupElements();
    System.out.println("Found in Set A= " + cbf.numFound + "\n");

    System.out.println("********CODED BLOOM FILTER IMPLEMENTATION**************");
    CodedBloomFilter codf = new CodedBloomFilter(7, 1000, 3, 30000, 7);
    codf.generateSets();
    codf.encodeSets();
    codf.lookup();
    System.out.println("Number of elements=" + codf.numFound);

  }

}
