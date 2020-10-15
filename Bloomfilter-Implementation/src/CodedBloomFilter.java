import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class CodedBloomFilter {

  int numBits;
  int numElements;
  int numHashes;
  int numSets;
  int numFilters;
  int[][] set;
  int[][] filter;
  int[] s;
  int numFound;
  Random rand = new Random();
  Map<Integer, String> setMapping;
  int codeLength;

  public CodedBloomFilter(int numSets, int numElements, int numFilters, int numBits,
      int numHashes) {
    this.numFilters = numFilters;
    this.numBits = numBits;
    this.numElements = numElements;
    this.numSets = numSets;
    this.numHashes = numHashes;
    set = new int[numSets][];
    s = new int[numHashes];
    setMapping = new HashMap();
    filter = new int[numFilters][numBits];
    initFilters();
    this.codeLength = (int) Math.ceil((Math.log(numSets + 1) / Math.log(2)));
    initMapping(setMapping, numSets);
    generateKHashFunctions();
  }

  public void initFilters() {
    for (int i = 0; i < filter.length; i++) {
      filter[i] = new int[numBits];
    }
  }

  public void generateKHashFunctions() {
    for (int i = 0; i < s.length; i++) {
      s[i] = Math.abs(rand.nextInt());
    }
  }

  public void initMapping(Map<Integer, String> map, int numSets) {
    for (int i = 0; i < numSets; i++) {
      StringBuilder sb = new StringBuilder();
      String binary = Integer.toBinaryString(i);
      sb.append(binary);
      // append leading zeroes to the code
      if (codeLength > sb.length()) {
        int diff = codeLength - sb.length();
        for (int j = 0; j < diff; j++)
          sb.insert(0, 0);
      }
      map.put(i, sb.toString());
    }
  }


  /*
   * Generate 7 sets of 1000 elements each numSets = 7 numElements = 1000
   */
  public void generateSets() {
    for (int i = 0; i < numSets; i++) {
      set[i] = new int[numElements];
      for (int j = 0; j < numElements; j++)
        set[i][j] = Math.abs(rand.nextInt());
    }
  }

  public void encodeSets() {
    for (int i = 0; i < numSets; i++) {
      String code = setMapping.get(i);
      // for all sets, encode all elements
      for (int j = 0; j < code.length(); j++) {
        if (code.charAt(j) == '1') {
          encode(i, j);
        }
      }
    }
  }

  public void encode(int setNumber, int filterNumber) {
    int[] elements = set[setNumber];
    for (int i = 0; i < elements.length; i++) {
      for (int j = 0; j < numHashes; j++) {
        int hash = s[j] ^ elements[i];
        // insert into the filter with filterNum
        filter[filterNumber][hash % numBits] = 1;
      }
    }
  }

  public void lookup() {
    for (int i = 0; i < numSets; i++) {
      int[] elements = set[i];
      String code = setMapping.get(i);
      char[] arr = code.toCharArray();

      // lookup for all elements
      for (int j = 0; j < elements.length; j++) {
        // lookup in all filters
        int element = elements[j];
        for (int k = 0; k < numFilters; k++) {
          if (isFound(element, filter[k])) {
            numFound++;
            // reconstruct
            arr[k] = '1';
            code = new String(arr);
            setMapping.put(i, code);
            break;
          }
        }
      }
    }
  }

  private boolean isFound(int element, int[] filter) {
    for (int i = 0; i < filter.length; i++) {
      for (int j = 0; j < numHashes; j++) {
        int hash = s[j] ^ element;
        if (filter[hash % numBits] == 0)
          return false;
      }
    }
    return true;
  }

}
