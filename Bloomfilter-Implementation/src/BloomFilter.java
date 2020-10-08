import java.util.Random;

public class BloomFilter {

  int[] bitmap;
  int numBits; // 10000
  int numElements; // 1000
  int numHashes; // 7
  int[] s;
  int[] elements;
  int numFound;
  int alreadyEncoded;
  int notEncoded;
  Random rand = new Random();

  public BloomFilter(int numBits, int numHashes, int numElements) {
    this.numElements = numElements;
    this.numBits = numBits;
    this.numHashes = numHashes;
    bitmap = new int[numBits];
    s = new int[numHashes];
    elements = new int[numElements];
    generateKHashFunctions();
  }


  public void generateKHashFunctions() {
    for (int i = 0; i < s.length; i++) {
      s[i] = Math.abs(rand.nextInt());
    }
  }

  public void generateElements() {
    for (int i = 0; i < numElements; i++) {
      elements[i] = Math.abs(rand.nextInt());
    }
  }

  public void encodeElements() {
    for (int i = 0; i < elements.length; i++)
      encode(elements[i]);
  }

  /**
   * return false if element is already encoded
   * 
   * @param element
   * @return
   */
  public boolean encode(int element) {
    // hash element to k entries in the bitmap
    int[] hashes = new int[numHashes];
    boolean flag = false;
    for (int i = 0; i < numHashes; i++) {
      hashes[i] = (s[i] ^ element) % numBits;
    }
    // check if all are 1
    for (int i = 0; i < hashes.length; i++) {
      if (bitmap[hashes[i]] == 1)
        continue;
      else {
        // set all the hashes to 1
        flag = true;
        notEncoded++;
        setToOne(hashes);
        break;
      }
    }
    if (!flag) {
      alreadyEncoded++;
    }
    return flag;
  }

  public void setToOne(int[] hashes) {
    for (int i = 0; i < hashes.length; i++) {
      if (bitmap[hashes[i]] == 0)
        bitmap[hashes[i]] = 1;
    }
  }

  public void lookupElements() {
    for (int i = 0; i < elements.length; i++) {
      lookup(elements[i]);
    }
  }

  /**
   * Increments the counter by 1 if lookup was successful
   * 
   * @param element
   * @return
   */
  public void lookup(int element) {
    int[] hashes = new int[numHashes];
    for (int i = 0; i < numHashes; i++) {
      hashes[i] = (s[i] ^ element) % numBits;
    }

    for (int i = 0; i < numHashes; i++) {
      if (bitmap[hashes[i]] == 0) {
        numFound++;
        // System.out.println("found");
        setToOne(hashes);
      }
    }
  }
}
