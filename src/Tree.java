import java.util.ArrayList;

//avl tree class
public class Tree {
    private class Node {
        House house;
        Node left;
        Node right;
        int height;

        Node(House house) {
            this.house = house;
            this.height = 1;
        }
    }

    private Node root;

    private int getHeight(Node node){
        if(node==null)return 0;
        return node.height;
    }

    //to update height when rotation or insertion is made
    private void update(Node node){
        if( node==null) return;
        node.height=1+Math.max(getHeight(node.left),getHeight(node.right));
    }

    //balance factor
    private int balance(Node node){
        if( node==null) return 0;
        return getHeight(node.left)-getHeight(node.right);
    }


    private Node rightRotation(Node node){
        Node left=node.left;
        Node rightOfLeft=left.right;
        left.right=node;
        node.left=rightOfLeft;

        update(node);
        update(left);
        return left;
    }

    private Node leftRotation(Node node){
        Node right=node.right;
        Node leftOfRight=right.left;
        right.left=node;
        node.right=leftOfRight;
        update(node);
        update(right);
        return right;
    }


    private Node insertNode(Node node, House house){
        //base case
        if(node==null) return new Node(house);
        //to left or right of the node according to house comparison
        int diff=house.compareTo(node.house);
        if(diff>0){
            node.right= insertNode(node.right,house);
        }
        else if(diff<0){
            node.left= insertNode(node.left,house);
        }
        else  {
            return node;
        }
        update(node);
        //making rotations if required to preserve avl property
        int balance=balance(node);
        if(balance>1){
            if (house.compareTo(node.left.house)<0) {
                return rightRotation(node);
            }
            else if(house.compareTo(node.left.house)>0){
                node.left=leftRotation(node.left);
                return rightRotation(node);
            }
        }
        if (balance<-1) {
            if (house.compareTo(node.right.house)>0) {
                return leftRotation(node);
            }
            else if(house.compareTo(node.right.house)<0){
                node.right=rightRotation(node.right);
                return leftRotation(node);
            }
        }
        return node;
    }

    public void insert(House house){root=insertNode(root,house);}

    //going to most left node for min
    private Node findMin(Node node){
        while (node.left!=null){
            node=node.left;
        }
        return node;
    }

    private Node deleteNode(Node node, House house){
        if (node==null){
            return null;

        }
        //to left or right of the node according to house comparison
        int diff=house.compareTo(node.house);
        if (diff<0){
            node.left=deleteNode(node.left,house);
        }
        else if (diff>0){
            node.right=deleteNode(node.right,house);
        }
        //case of found
        else {
            //if 0 or 1 child, child takes the place
            if (node.left==null||node.right==null){
                if (node.left!=null){
                    node=node.left;
                }
                else node =node.right;
            }
            //assigning min house of right subtree to node's house
            else{
                Node tmp=findMin(node.right);
                node.house=tmp.house;
                node.right=deleteNode(node.right,tmp.house);
            }
        }
        if (node==null){
            return null;
        }
        update(node);
        //rotations to keep avl property
        int balance=balance(node);
        if (balance>1){
            if (balance(node.left)>=0) {
                return rightRotation(node);
            }
            else if(balance(node.left)<0){
                node.left=leftRotation(node.left);
                return rightRotation(node);
            }
        }
        else if(balance<-1){
            if (balance(node.right)<=0) {
                return leftRotation(node);
            }
            else if(balance(node.right)>0){
                node.right=rightRotation(node.right);
                return leftRotation(node);
            }
        }
        return node;
    }
    public void delete(House house){
        root=deleteNode(root,house);
    }
    private void searchP1(Node node, int inpMaxPrice, int inpMinSize, int count, ArrayList<House> dict){
        if (node==null||dict.size()>=count){
            return;
        }
        //inorder traversal
        searchP1(node.left,inpMaxPrice,inpMinSize,count,dict);
        if (dict.size()>=count){
            return;
        }
        if (node.house.price<=inpMaxPrice){
            if (node.house.size>=inpMinSize){
                dict.add(node.house);
            }
            if (dict.size()<count){
                searchP1(node.right,inpMaxPrice,inpMinSize,count,dict);
            }
        }
    }
    public void startP1(int inpMaxPrice, int inpMinSize, int count, ArrayList<House> dict){
        searchP1(root,inpMaxPrice,inpMinSize,count,dict);
    }

    private void searchP2(Node node,int inpMaxPrice, int inpMinSize, int count, ArrayList<House> dict){
        if (node==null||dict.size()>=count){return;}
        //inorder traversal
        searchP2(node.left,inpMaxPrice,inpMinSize,count,dict);
        if (dict.size()>=count){return;}
        if (node.house.price<=inpMaxPrice){
            if (node.house.size<inpMinSize){
                dict.add(node.house);
            }
            if (dict.size()<count){
                searchP2(node.right,inpMaxPrice,inpMinSize,count,dict);
            }
        }
    }
    public void startP2(int inpMaxPrice, int inpMinSize, int count, ArrayList<House> dict){
        searchP2(root,inpMaxPrice,inpMinSize,count,dict);
    }

    private void searchP3(Node node,int inpMaxPrice, int inpMinSize, int count, ArrayList<House> dict){
        if (node==null||dict.size()>=count){return;}
        //inorder traversal
        if (node.house.price>inpMaxPrice){
            searchP3(node.left,inpMaxPrice,inpMinSize,count,dict);
            if (dict.size()>=count){return;}
            if (node.house.size>=inpMinSize){
                dict.add(node.house);
            }
        }
        if (dict.size()<count){
            searchP3(node.right,inpMaxPrice,inpMinSize,count,dict);
        }
    }
    public void startP3(int inpMaxPrice, int inpMinSize, int count, ArrayList<House> dict){
        searchP3(root,inpMaxPrice,inpMinSize,count,dict);
    }

    private void searchP4(Node node,int inpMaxPrice, int inpMinSize, int count, ArrayList<House> dict){
        if (node==null||dict.size()>=count){return;}
        //inorder traversal
        if (node.house.price>inpMaxPrice){
            searchP4(node.left,inpMaxPrice,inpMinSize,count,dict);
            if (dict.size()>=count){return;}
            if (node.house.size<inpMinSize){
                dict.add(node.house);
            }
        }
        if (dict.size()<count){
            searchP4(node.right,inpMaxPrice,inpMinSize,count,dict);
        }
    }
    public void startP4(int inpMaxPrice, int inpMinSize, int count, ArrayList<House> dict){
        searchP4(root,inpMaxPrice,inpMinSize,count,dict);
    }

    private long getTotalPrice(Node node,int inpMaxPrice,int  inpMinSize){
        if (node==null)return 0;
        //inorder traversal
        long sum=getTotalPrice(node.left,inpMaxPrice,inpMinSize);
        if (node.house.price<=inpMaxPrice){
            if (node.house.size>=inpMinSize){
                sum+=node.house.price;
            }
            sum+=getTotalPrice(node.right,inpMaxPrice,inpMinSize);
        }
        return sum;
    }
    public long startGetTotalPrice(int inpMaxPrice,int inpMinSize){
        return getTotalPrice(root,inpMaxPrice,inpMinSize);
    }

}
