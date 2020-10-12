import java.util.Random;

public class CountingBloomFilter {
  int numBits; // 10000
  int numElements; // 1000
  int numHashes; // 7
  int[] s;
  int[] elements;
  int numFound;
  int numRemoved; // 500
  int numAdded; // 500
  int[] counter;
  int[] removed;
  int[] original;
  Random rand = new Random();

  public CountingBloomFilter(int numElements, int numBits, int numHashes, int numRemoved,
      int numAdded) {
    this.numElements = numElements;
    this.numBits = numBits;
    this.numHashes = numHashes;
    this.numRemoved = numRemoved;
    this.numAdded = numAdded;
    s = new int[numHashes];
    counter = new int[numBits];
    elements = new int[numElements];
    removed = new int[numRemoved];
    // original = new int[numElements - numRemoved];
    generateKHashFunctions();
  }

  public void generateKHashFunctions() {
    for (int i = 0; i < s.length; i++) {
      s[i] = Math.abs(rand.nextInt());
    }
  }

  public void generateElements() {
    for (int i = 0; i < numElements; i++)
      elements[i] = Math.abs(rand.nextInt());
  }

  public void encodeElements() {
    for (int i = 0; i < elements.length; i++)
      if (elements[i] != -1)
        encode(elements[i]);
  }

  public boolean encode(int element) {
    int[] hashes = new int[numHashes];
    boolean flag = false;
    for (int i = 0; i < numHashes; i++) {
      hashes[i] = (s[i] ^ element) % numBits;
    }
    // increment the k counters
    for (int i = 0; i < hashes.length; i++) {
      int hash = hashes[i];
      counter[hash]++;
    }
    return flag;
  }

  public void removeElements() {
    for (int i = 0; i < numRemoved; i++) {
      int index = Math.abs(rand.nextInt(elements.length));
      removed[i] = elements[index];
      if (elements[index] != -1) {
        remove(elements[index]);
        elements[index] = -1;
      }
    }
  }

  private void remove(int element) {
    for (int i = 0; i < numHashes; i++) {
      int hash = (s[i] ^ element) % numBits;
      counter[hash]--;
    }
  }

  public void addElements() {
    for (int i = 0; i < numAdded; i++) {
      encode(Math.abs(rand.nextInt()));
    }
  }

  /*
   * private void add(int element) { for (int i = 0; i < numHashes; i++) { int hash = (s[i] ^
   * element) % numBits; counter[hash]++; } }
   */

  public void lookupElements() {
    for (int i = 0; i < elements.length; i++) {
      if (elements[i] != -1)
        lookup(elements[i]);
    }
  }

  private void lookup(int element) {
    int[] hashes = new int[numHashes];
    for (int i = 0; i < numHashes; i++) {
      hashes[i] = (s[i] ^ element) % numBits;
    }

    for (int i = 0; i < numHashes; i++) {
      if (counter[hashes[i]] <= 0) {
        return;
      }
    }
    numFound++;
  }

}
