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
   * 
   * 
   * @param element
   * @return
   */
  public void encode(int element) {
    // hash element to k entries in the bitmap
    int[] hashes = new int[numHashes];
    for (int i = 0; i < numHashes; i++) {
      hashes[i] = (s[i] ^ element) % numBits;
    }

    // check if all are 1
    for (int i = 0; i < hashes.length;) {
      if (lookup(element)) // already present
        return;

      // set all the hashes to 1
      setToOne(hashes);
      break;
    }
  }

  public void setToOne(int[] hashes) {
    for (int i = 0; i < hashes.length; i++) {
      if (bitmap[hashes[i]] == 0)
        bitmap[hashes[i]] = 1;
    }
  }

  public void lookupElements() {
    for (int i = 0; i < elements.length; i++) {
      if (lookup(elements[i]))
        numFound++;
    }
  }

  /**
   * returns true if lookup was successful
   * 
   * @param element
   * @return
   */
  public boolean lookup(int element) {
    int[] hashes = new int[numHashes];
    for (int i = 0; i < numHashes; i++) {
      hashes[i] = (s[i] ^ element) % numBits;
    }

    for (int i = 0; i < numHashes; i++) {
      if (bitmap[hashes[i]] == 1) {
        continue;
      } else {
        return false;
      }
    }
    return true;
  }
}
