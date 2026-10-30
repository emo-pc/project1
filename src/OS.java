import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
//our main command operator class
public class OS {
    private HashTable<House> houseDict;
    private HashTable<ArrayList<SearchRecord>>  searchDict;
    private Tree houseTree;
    private int activeCount;
    private int timeCounter;

    public OS(){
        this.houseDict=new HashTable<>(100000);
        this.searchDict=new HashTable<>(50000);
        this.houseTree=new Tree();
        this.activeCount=0;
        this.timeCounter=0;
    }


    private void addHouse(String id,int price,int size,BufferedWriter bw) throws IOException{
        //if a house with the same id exists -> error
        if (houseDict.get(id)!=null){
            bw.write("ERROR");
            bw.newLine();
            return;
        }

        timeCounter++;
        //adding house to hashTable and avl tree
        House house=new House(id,price,size,timeCounter);
        houseDict.put(id,house);
        houseTree.insert(house);
        activeCount++;
        bw.write("House "+id+" Added Successfully");
        bw.newLine();

    }

    private void removeHouse(String id,BufferedWriter bw) throws IOException{
        House house=houseDict.get(id);
        //if id is invalid or house is sold -> error
        if (house==null|| house.isActive==false){
            bw.write("ERROR");
            bw.newLine();
            return;
        }
        //removing house from tree and marking it as inactive
        houseTree.delete(house);
        house.isActive=false;
        activeCount--;
        bw.write("House "+id+" Removed Successfully");
        bw.newLine();
    }

    private void houseUpdate(String id,BufferedWriter bw,int newPrice) throws IOException{
        House house=houseDict.get(id);
        //error cases
        if (house==null||house.isActive==false||house.price==newPrice){
            bw.write("ERROR");
            bw.newLine();
            return;
        }
        //first deleting then adding the updated house to tree
        houseTree.delete(house);
        house.price=newPrice;
        house.updateCount++;
        timeCounter++;
        house.timestamp=timeCounter;
        houseTree.insert(house);
        bw.write("House "+id+" Updated Successfully");
        bw.newLine();
        return;
    }

    private void houseSearch(String id,int maxPrice,int minSize,int count,BufferedWriter bw) throws IOException{
        //error cases
        if (searchDict.get(id)!=null||activeCount==0||count<=0){
            bw.write("ERROR");
            bw.newLine();
            return;
        }
        ArrayList<House> results=new ArrayList<>();
        ArrayList<String> ranks=new ArrayList<>();//list of priority ranks of found houses
        houseTree.startP1(maxPrice,minSize,count,results);
        while (ranks.size()<results.size()){
            ranks.add("P1");
        }
        if (results.size()<count){
            houseTree.startP2(maxPrice,minSize,count,results);
            while (ranks.size()<results.size()){
                ranks.add("P2");
            }
            if (results.size()<count){
                houseTree.startP3(maxPrice,minSize,count,results);
                while (ranks.size()<results.size()){
                    ranks.add("P3");
                }
                if (results.size()<count){
                    houseTree.startP4(maxPrice,minSize,count,results);
                    while (ranks.size()<results.size()){
                        ranks.add("P4");
                    }
                }
            }
        }
        ArrayList<SearchRecord> searchResult=new ArrayList<>();
        for (int i=0;i<results.size();i++){
            House res_house=results.get(i);
            searchResult.add(new SearchRecord(res_house,res_house.updateCount));
            //printing the search result
            bw.write(res_house.id+" "+res_house.price+" "+res_house.size+" "+ ranks.get(i));
            bw.newLine();
        }
        searchDict.put(id,searchResult);//saving the search session

    }

    private void closeSearch(String searchId,BufferedWriter bw) throws IOException{
        ArrayList<SearchRecord> wantedSearch=searchDict.get(searchId);
        //in case search id is invalid
        if (wantedSearch==null){
            bw.write("ERROR");
            bw.newLine();
            return;
        }
        //removing search session from hashTable
        searchDict.remove(searchId);
        bw.write("Search "+searchId+" Closed");
        bw.newLine();
        return;
    }

    private void buyHouse(String searchId,int index,BufferedWriter bw) throws IOException{
        //in case search id is invalid
        if (searchDict.get(searchId)==null){
            bw.write("ERROR");
            bw.newLine();
            return;
        }
        ArrayList<SearchRecord> searchSession =searchDict.get(searchId);
        //in case index is out of the search's boundary
        if (index>=searchSession.size()||index<0){
            bw.write("ERROR");
            bw.newLine();
            return;
        }
        SearchRecord wantedRecord=searchSession.get(index);
        House wantedHouse=wantedRecord.house;
        //in case house is already sold or updated
        if (wantedHouse.isActive==false||wantedRecord.updateCount!=wantedHouse.updateCount){
            bw.write("ERROR");
            bw.newLine();
            return;
        }
        //selling the house
        houseTree.delete(wantedHouse);
        bw.write("House "+wantedHouse.id+" Sold");
        bw.newLine();
        wantedHouse.isActive=false;
        activeCount--;
        return;
    }

    //makes process according to input line
    public void commandExecuter(String line, BufferedWriter bw) throws IOException {
        String[] tokens=line.trim().split(" ");
        String command=tokens[0];

        if (command.equals("Add")){
            String id=tokens[1];
            int price=Integer.parseInt(tokens[2]);
            int size=Integer.parseInt(tokens[3]);
            addHouse(id,price,size,bw);
        }
        else if (command.equals("Remove")){
            String id=tokens[1];
            removeHouse(id,bw);
        }
        else if (command.equals("Update")){
            String id=tokens[1];
            int newPrice=Integer.parseInt(tokens[2]);
            houseUpdate(id,bw,newPrice);
        }
        else if (command.equals("Search")){
            String searchId=tokens[1];
            int maxPrice=Integer.parseInt(tokens[2]);
            int minSize=Integer.parseInt(tokens[3]);
            int count=Integer.parseInt(tokens[4]);
            houseSearch(searchId,maxPrice,minSize,count,bw);
        }
        else if (command.equals("Buy")){
            String searchId=tokens[1];
            int index=Integer.parseInt(tokens[2]);
            buyHouse(searchId,index,bw);
        }
        else if (command.equals("Close")){
            String searchId=tokens[1];
            closeSearch(searchId,bw);
        }
        else if (command.equals("Count")){
            bw.write("Count : "+activeCount);
            bw.newLine();
        }
        else if (command.equals("TotalPrice")){
            int maxPrice=Integer.parseInt(tokens[1]);
            int minSize=Integer.parseInt(tokens[2]);
            long totalPrice=houseTree.startGetTotalPrice(maxPrice,minSize);
            bw.write("TotalPrice is "+totalPrice);
            bw.newLine();
        }
        //in case command is not valid
        else {
            bw.write("ERROR");
            bw.newLine();
        }
    }

}
