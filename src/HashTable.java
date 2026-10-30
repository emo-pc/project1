import java.util.ArrayList;

//generic dict class for houses and searches
public class HashTable<T> {

    //cell in a dict
    private class HashEntry {
        String Key;
        T Value;
        //open hashing for collision
        HashEntry next;

        HashEntry(String key, T value){
            this.Key = key;
            this.Value = value;
            this.next = null;
        }
    }

    //the main list where data will be stored in
    private ArrayList<HashEntry> table;
    private int size;
    private int capacity;

    public HashTable(int initialCapacity) {
        this.capacity = initialCapacity;
        this.size = 0;
        this.table = new ArrayList<>(capacity);
        for (int i = 0; i < capacity; i++) {
            table.add(null);
        }
    }

    //custom hash function
    private int getHashCode(String key){
        //horner's method
        int hash=0;
        for (int i=0;i<key.length();i++){
            hash=29*hash+key.charAt(i);
        }
        //for edge cases
        if (hash<0){
            hash=-hash;
            if (hash<0){
                hash=0;
            }
        }
        return hash;
    }

    //returns the index of a key's cell using custom hash function
    private int getIndex(String key){
        return getHashCode(key)%capacity;
    }

    //updates arrayList to prevent overflow-rehashing
    private void rehash(){
        //adjustments
        int oldCapacity=capacity;
        capacity=2*capacity+1;
        ArrayList<HashEntry> old=table;
        table=new ArrayList<>(capacity);
        for(int i=0;i<capacity;i++){
            table.add(null);
        }
        //copying every element to new table
        for (int i=0;i<oldCapacity;i++){
            HashEntry entry=old.get(i);
            while(entry!=null){
                HashEntry next=entry.next;
                int index=getIndex(entry.Key);
                entry.next=table.get(index);
                table.set(index,entry);
                entry=next;
            }
        }
    }
    /*
    insertion of an entry to linked list of a cell
    or changing value if entry already exists
    */
    public void put(String key, T value){
        int index=getIndex(key);
        HashEntry current=table.get(index);
        while(current!=null){
            if(current.Key.equals(key)){
                current.Value=value;
                return;
            }
            current=current.next;
        }
        HashEntry entry=new HashEntry(key, value);
        entry.next=table.get(index);
        table.set(index,entry);
        size++;
        if (size > capacity*1){
            rehash();
        }
    }
    //gets the value corresponding to a key in a linked list
    public T get(String key){
        int index=getIndex(key);
        HashEntry current=table.get(index);
        while (current!=null){
            if (current.Key.equals(key)){
                return current.Value;
            }
            current=current.next;
        }
        return null;
    }



    //pops the value of a key and removes the entry from list
    public T remove(String key){
        int index=getIndex(key);
        HashEntry current=table.get(index);
        HashEntry previous=null;
        while (current!=null){
            if (current.Key.equals(key)){
                if (previous==null){
                    table.set(index,current.next);
                }
                else{
                    previous.next=current.next;
                }
                size--;
                return current.Value;
            }
            previous=current;
            current=current.next;
        }
        return null;
    }
}
