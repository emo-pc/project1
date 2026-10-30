public class House {
    String id;
    int price;
    int size;
    int timestamp;
    int updateCount;
    boolean isActive;
    House(String id,int price,int size,int timestamp){
        this.id = id;
        this.price = price;
        this.size = size;
        this.timestamp = timestamp;
        this.updateCount=0;
        this.isActive =true;
    }

    //houses will be added to BSTree according to this.
    public int compareTo(House other){
        if (this.price!=other.price){
            return Integer.compare(this.price,other.price);
        }
        if (this.size!=other.size){
            return Integer.compare(other.size,this.size);
        }
        return Integer.compare(this.timestamp,other.timestamp);
    }

}
