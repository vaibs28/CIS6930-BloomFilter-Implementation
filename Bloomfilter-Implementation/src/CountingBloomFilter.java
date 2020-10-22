import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;

public class CountingBloomFilter {
  int numBits; // 10000
  int numElements; // 1000
  int numHashes; // 7
  int[] s;
  Set<Integer> elements;
  Set<Integer> original;
  Set<Integer> removed;
  int numFound;
  int numRemoved; // 500
  int numAdded; // 500
  int[] counter;
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
    elements = new HashSet(numElements);
    removed = new HashSet();
    generateKHashFunctions();
  }

  public void generateKHashFunctions() {
    for (int i = 0; i < s.length; i++) {
      s[i] = Math.abs(rand.nextInt());
    }
  }

  public void generateElements() {
    for (int i = 0; i < numElements; i++) {
      int val = Math.abs(rand.nextInt());
      if (!elements.contains(val)) {
        elements.add(val);
      } else {
        i--;
      }
    }
  }

  public void encodeElements() {
    Iterator<Integer> iter = elements.iterator();
    while (iter.hasNext()) {
      encode(iter.next());
    }
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
      int val = (int) elements.toArray()[Math.abs(rand.nextInt(elements.size()))];
      if (!removed.contains(val)) {
        remove(val);
        removed.add(val);
      } else {
        i--;
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
      int val = Math.abs(rand.nextInt());
      // int val = (int) elements.toArray()[index];
      if (!removed.contains(val) && !elements.contains(val)) {
        encode(val);
      } else {
        i--;
      }
    }
  }

  /*
   * private void add(int element) { for (int i = 0; i < numHashes; i++) { int hash = (s[i] ^
   * element) % numBits; counter[hash]++; } }
   */

  public void lookupElements() {
    Iterator<Integer> iter = elements.iterator();
    while (iter.hasNext()) {
      lookup(iter.next());
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
