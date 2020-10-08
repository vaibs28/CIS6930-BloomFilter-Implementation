
public class ApplicationRunner {

  public static void main(String[] args) {
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
  }

}
