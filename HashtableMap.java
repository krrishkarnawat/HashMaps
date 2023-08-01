// --== CS400 Project One File Header ==--
// Name: Krrish Karnawat
// CSL Username: krrish
// Email: kkarnawat@wisc.edu
// Lecture #:  003 @2:25pm
import java.util.Iterator;
import java.util.LinkedList;
import java.util.NoSuchElementException;

/**
 * This class creates a node containing a key-value pair
 *
 * @param <KeyType>
 * @param <ValueType>
 */
class HashNode<KeyType, ValueType> {
    // creating private fields.
    private KeyType K;
    private ValueType V;
    private double hashCode;

    /**
     *  Constructor for HashNode that sets the KeyType-ValueType pairs.
     *
     * @param K
     * @param V
     */
    public HashNode(KeyType K, ValueType V) {
        this.K = K; //sets the field K to the parameter
        this.V = V; //sets the field V to the parameter
    }

    /**
     * returns the Key K.
     * @return
     */
    public KeyType getKey() {
        return this.K;
    }

    /**
     * returns the Value V
     * @return ValueType
     */
    public ValueType getValue() {
        return this.V;
    }


}

/**
 * This class creates an object of HashtableMap using a LinkedList array.
 *
 * @param <KeyType>
 * @param <ValueType>
 */
public class HashtableMap<KeyType, ValueType> implements IterableMapADT<KeyType, ValueType>, IISBNValidator{
    //declaring private fields
    private int size = 0;
    private int capacity;
    //declaring protected LinkedList array
    protected LinkedList<HashNode<KeyType, ValueType>>[] hashMap;

    /**
     * HashTableMap constructor that sets the capacity of the hashMap (LinkedList array) and initializes the hashMap.
     * @param capacity
     */
    @SuppressWarnings("unchecked")
    public HashtableMap(int capacity) {
        this.capacity = capacity; //sets the capacity field to the value of the parameter.
        this.hashMap = (LinkedList<HashNode<KeyType, ValueType>>[]) new LinkedList[capacity];
        //initializes the hashMap array of size capacity.

    }
    /**
     * HashTableMap constructor that sets the capacity of the hashMap (LinkedList array) and initializes the hashMap.
     * @param
     */
    @SuppressWarnings("unchecked")
    public HashtableMap() {
        this.capacity = 15;
        this.hashMap = (LinkedList<HashNode<KeyType, ValueType>>[]) new LinkedList[capacity];
    } // with default capacity = 15

    /**
     * private helper method that calculates the index.
     * @param K
     * @return int
     */
    private int getHashCode(KeyType K) {
        int a = (Math.abs(K.hashCode()) % this.capacity); //calculate the index
        return a; //returns the index value.
    }

    /**
     * Inserts a new (key, value) pair into the map if the map does not
     * contain a value mapped to key yet.
     *
     * @param key   the key of the (key, value) pair to store
     * @param value the value that the key will map to
     * @return true if the (key, value) pair was inserted into the map,
     * false if a mapping for key already exists and the
     * new (key, value) pair could not be inserted
     */
    @Override
    public boolean put(KeyType key, ValueType value) {
        if (key == null || value == null) { //checks if the key/value is null
            return false; //returns false
        } else {
            int index = getHashCode(key);
            if (hashMap[index] == null) { //checks if the index is null
                hashMap[index] = new LinkedList<HashNode<KeyType, ValueType>>();
            }
            if (containsKey(key) == true) {
                return false; //if key exists return false.
            }
            HashNode<KeyType, ValueType> node = new HashNode<>(key, value);
            hashMap[index].add(node);
            size++;
            if(size>= (0.7*(this.capacity))){
                hashMap = this.reHash(hashMap);
            }
        }
        return true;
    }

    /**
     * private helper method used for rehashing in case load factor > 0.7 for any function.
     * @param hashMap
     * @return
     */
    private LinkedList<HashNode<KeyType, ValueType>>[] reHash(
            LinkedList<HashNode<KeyType, ValueType>>[] hashMap) {
        // Create new hastable with double capacity
        this.capacity = 2 * this.capacity; // doubling the capacity
        @SuppressWarnings("unchecked")
        // creating a new linkedlist storing all the prev and new (double capacity) values
        LinkedList<HashNode<KeyType, ValueType>>[] helper = new LinkedList[this.capacity];
        for (int i = 0; i < hashMap.length; i++) {
            if (hashMap[i] != null) {
                for (int j = 0; j < hashMap[i].size(); j++) {
                    int newIndex = Math.abs(hashMap[i].get(j).getKey().hashCode()) % this.capacity;
                    if (helper[newIndex] == null) {
                        helper[newIndex] = new LinkedList<HashNode<KeyType, ValueType>>();
                    }
                    helper[newIndex].add(hashMap[i].get(j));
                }
            }
        }
        hashMap = helper;
        return hashMap;
    }


    /**
     * Returns the value mapped to a key if the map contains such a mapping.
     *
     * @param key the key for which to look up the value
     * @return the value mapped to the key
     * @throws NoSuchElementException if the map does not contain a mapping
     *                                for the key
     */
    @Override
    public ValueType get(KeyType key) throws NoSuchElementException {
        if (!containsKey(key)) { //checks if the key exists
            throw new NoSuchElementException(); //if key doesn't exist, throws NoSuchElementException
        }
     int index =0; //initializes index to 0
        try { //to check if index is null.
            index = getHashCode(key);  //gets the index using the getHashCode method.
        }
        catch(NullPointerException e){
            throw new NoSuchElementException();
        }
        if(hashMap[index]==(null)){ //checks if the node at the index is null.
            throw new NoSuchElementException(); //throws NoSuchElementException
        }
        for (int i = 0; i < hashMap[index].size(); i++) { //for loop to iterate through the linked list at the index
            if (hashMap[index].get(i).getKey().equals(key)) { //checks if the key match is found
                return hashMap[index].get(i).getValue(); //returns the value associated to the key.
            }

        }
        throw new NoSuchElementException("no such value exists for this key.");

    }


    /**
     * Removes a key and its value from the map.
     *
     * @param key the key for the (key, value) pair to remove
     * @return the value for the (key, value) pair that was removed,
     * or null if the map did not contain a mapping for key
     */

    @Override
    public ValueType remove(KeyType key) {
        if (!containsKey(key)) { //checks if key exists
            return null; //returns null if key doesn't exist.
        }
        int index = getHashCode(key); //gets the index using the getHashCode method.
        if(hashMap[index]==(null)){ //checks if the node at index is null.
            return null;
        }
        for (int i = 0; i < hashMap[index].size(); i++) { //uses a for loop to iterate through the linked list
            if (hashMap[index].get(i).getKey().equals(key)) { //if key match found
                ValueType va = hashMap[index].get(i).getValue(); //creates a ValueType obj and saves Value there
                hashMap[index].remove(i); //clears the key-value pair
                size--; //reduces the size
                return va; //returns the value of the matched key.

            }

        }
        return null;
    }

    /**
     * Checks if a key is stored in the map.
     *
     * @param key the key to check for
     * @return true if the key is stored (mapped to a value) by the map
     * and false otherwise
     */
    @Override
    public boolean containsKey(KeyType key) {
        int index = getHashCode(key); //gets the index using the getHashCode method.
        if(hashMap[index]==(null)){ //checks if node at index is null
            return false; //returns false
        }
        for (int i = 0; i < hashMap[index].size(); i++) {
            if (hashMap[index].get(i).getKey().equals(key)) { //checks if there's a match of the key
                return true; //returns true if match found.
            }


        }
        return false; //returns false otherwise.
    }

    /**
     * Returns the number of (key, value) pairs stored in the map.
     *
     * @return the number of (key, value) pairs stored in the map
     */
    @Override
    public int size() {
        return size; //returns the size- the no.of key-value pairs.
    }

    /**
     * Removes all (key, value) pairs from the map.
     */
    @Override
    public void clear() {
        size = 0; //sets the size to 0.
    }

    /**
     * Checks is the given ISBN number is a valid ISBN13 number.
     *
     * @param isbn13 the ISBN number to validate
     * @return true is the number if a valid ISBN number, false otherwise
     */
    @Override
    public boolean validate(String isbn13) {
        if(isbn13 == null){ //checks if the string is null
        return false; //returns false if null.
        }
        String g = isbn13.trim().replaceAll("-", ""); //creates a string by trimming whitespaces
        //at either ends and replacing all '-' with nothing.

        if(g.length()!=13){ //tests if the length is 13
            return false; //.returns false
        }
        if(!g.matches("[0-9]+")){ //checks if all the elements of the string are within 0-9
            return false; //returns false if not
        }
        int count1 = 0; //local variable 1
        int count2 = 0; //local variable 2
        for(int i=0; i<g.length()-1; i++){ //a for-loop that iterates through all the chars of the string
            if(i%2==0){ //checking if the char is at an even position.
                count1 = count1 + Integer.parseInt(String.valueOf(g.charAt(i))); //sums up the values of the
                //string ints at all even positions.
            }
            else{
                count2 = count2 + (Integer.parseInt(String.valueOf(g.charAt(i))) *3); //sums the string ints at odd
                // positions after multiplying it with 3.
            }
        }
        int sum = count1 + count2; //adds both the summations
        if(((10-(sum%10))%10) == Integer.parseInt(String.valueOf((g.charAt(12))))){ //checks if the condition for ISBN13
            // is true for the string with respect to the conditions that isbn13 requires.
            return true; //returns true if it's equal to the last digit of the 13 digit int String.
        }
        else{
            return false; //returns false otherwise.
        }
    }

    /**
     * Returns an iterator over elements of type {@code T}.
     *
     * @return an Iterator.
     */
    @Override
    public Iterator<ValueType> iterator() {
        return new Iteratethrough<ValueType>() ;

    }

    /**
     *
     * @param <ValueType>
     */
    public class Iteratethrough<ValueType>implements Iterator<ValueType> {

        public int arrayIndex = 0; //initializes the arrayIndex used to check the arrayIndex
        public int listPointer = -1; //initializes the listPointer used to check the index of the list in an array index

        /**
         * Returns {true} if the iteration has more elements.
         * (In other words, returns {true} if {#next} would
         * return an element rather than throwing an exception.)
         *
         * @return {true} if the iteration has more elements
         */
        @Override
        public boolean hasNext() {
            if (size == 0) { //checks if the size equals 0
                return false; //returns false if size equals 0.
            }

            int checkIndex = listPointer + 1; //creates a checker index that holds the value of listpointer plus 1.

            if (checkIndex < hashMap[arrayIndex].size()) { //checks if listpointer+1 value is less than the size of the
                //array
                return true; //returns true if that's the case.
            } else {
                for (int i = arrayIndex + 1; i < hashMap.length; i++) { //otherwise iterate through the array to see
                    //if any array indexs aren't null.
                    if (hashMap[i] != null) {
                        return true; //returns true if it found one.
                    }

                }
            }
            return false; //returns false otherwise.
        }

        /**
         * Returns the next element in the iteration.
         *
         * @return the next element in the iteration
         * @throws NoSuchElementException if the iteration has no more elements
         */
        @Override
        public ValueType next() {
            if (size() == 0) { //if size is zero throws a new NoSuchElementException
                throw new NoSuchElementException();
            }

            for (int i = arrayIndex; i < hashMap.length; i++) {
                if (hashMap[i] == null) { //finds the arrayIndex that isn't null using a for loop
                    arrayIndex += 1;
                    continue;
                }
                int checkPointer = listPointer + 1; //checkPointer is listPointer +1.
                for (int k = listPointer; k < hashMap[arrayIndex].size(); k++) {
                    if (checkPointer < hashMap[arrayIndex].size()) { //checks if the size at index i is more than the
                        //checkpointer.
                        listPointer += 1; //increments listpointer if true
                        arrayIndex = i; //sets arrayIndex to i.
                        return (ValueType) hashMap[arrayIndex].get(checkPointer).getValue(); //returns the required
                        //value.
                    } else {
                        if (arrayIndex < hashMap.length) { // in case there are more arrayIndex's left to iterate
                            arrayIndex = i + 1; //arrayIndex equals incrementation of i.
                            listPointer = -1; //listpointer rests to -1.
                            break; //breaks the second for loop
                        }
                    }
                }
            }
            throw new NoSuchElementException(); //throws NoSuchElementException in case arrayIndexs run out.
        }

    }

}


