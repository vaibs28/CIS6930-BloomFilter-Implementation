
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
    System.out.println("Found in set B = " + bf.numFound);

    System.out.println("********COUNTING BLOOM FILTER IMPLEMENTATION***********");
    CountingBloomFilter cbf = new CountingBloomFilter(1000, 10000, 7, 500, 500);
    cbf.generateElements();
    cbf.encodeElements();
    cbf.removeElements();
    cbf.addElements();
    cbf.lookupElements();
    System.out.println("Found in Set A= " + cbf.numFound);
  }

}
