//an additional helpful class for search sessions
public class SearchRecord {
    House house;
    int updateCount;//the update count of house at the time search is made
    SearchRecord(House house, int updateCount){
        this.house = house;
        this.updateCount = updateCount;
    }
}
