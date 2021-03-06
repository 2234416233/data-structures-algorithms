package cumt.tj.learn.structures.hash;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by sky on 17-6-3.
 * 数据结构与算法分析java版，第5章散列
 * 通过分离链表法解决冲突，这个实现是利用key计算hash的
 */
public class SeparateChainingHashTable<K,V> {

    //先要有一个存储链表的数组
    LinkedList<Node>[] hashTable;
    //默认链表长度，暂时设为11吧，表的大小最好为素数
    private static final int DEFAULT_TABLE_SIZE=11;
    //当前哈希表中的键值对的个数
    private int currentSize;


    public SeparateChainingHashTable() {
        this(DEFAULT_TABLE_SIZE);
    }

    public SeparateChainingHashTable(int size) {
        //分配空间
        allocateArray(size);
    }

    /**
     * 添加键值对
     * @param key 没错，我就是键
     * @param value 没错，我就是值，娃哈哈……
     */
    public void put(K key,V value){
        Node node=new Node(key,value);

        //首先，要算出x的哈希值
        int hash=myHash(node);
        //然后，找到这个索引的链表
        List<Node> theList= hashTable[hash];

        //然后，看看这个链表里面是不是已经有这个key存在了
        if(theList.contains(node)){
            //有就删除
            theList.remove(node);

        }
        //将已经存在的东东删掉就不会出现一个key对应对个value的情况了
        theList.add(node);

        if(++currentSize>hashTable.length){
            //再添加就要超过了哈希表的大小了
                /*
                这个地方涉及到一个叫充填因子的东东，这里的currentSize记录的是插入的<key,value>的个数，由于可能的冲突，
                一个链表可能含有多个元素，所以它不是已经有元素的链表的个数。
                 */
            rehash();
        }
    }

    /**
     * 根据key获取value
     * @param key 没错，我就是key
     * @return 无情返回value，哇哈哈哈……
     */
    public V get(K key){

        Node node=new Node(key,null);

        //首先算出key对应的哈希值
        int hash =myHash(node);
        //找到对应的链表
        List<Node> theList=hashTable[hash];

        //查找是否key对应的链表的索引
        int index=theList.indexOf(node);
        if(index!=-1){
            //要获取的东东存在
            return theList.get(index).value;
        }

        //没有找到
        return null;
    }

    /**
     * 删除键值对
     * @param key 没错，我就是键，就是根据我删除的，娃哈哈……
     */
    public void remove(K key){
        Node node=new Node(key,null);

        //首先算出key对应的哈希值
        int hash =myHash(node);
        //找到对应的链表
        List<Node> theList=hashTable[hash];

        //查找是否key对应的链表的索引
        int index=theList.indexOf(node);
        if(index!=-1){
            //要获取的东东存在
            theList.remove(index);
        }

        //没有找到，那就不用动手删除了，娃哈哈……
    }

    private void rehash(){
        LinkedList<Node>[] oldArray=this.hashTable;

        //首先它要造一个数组，是原来的2倍大小（离2倍最近的素数）
        allocateArray(nextPrime(oldArray.length*2));

        //然后，要让原来数组里面的元素，利用重新插入到新数组里面（表的大小变化了，会影响hash值与探测，所以要重新插入）
        for (LinkedList<Node> itemList:oldArray
             ) {
            if(itemList.size()!=0){
                for (Node item:itemList
                     ) {
                    put(item.key,item.value);
                }
            }
        }

    }


    private void allocateArray(int size){
        //找一个接近的素数，作为数组大小
        this.hashTable = new LinkedList[nextPrime(size)];

        //一次性分配空间，编程珠玑和数据结构与算法分析里面都说，一次性分配空间耗时更少，虽然我没有搞清楚为什么
        for(int i=0;i<hashTable.length;i++){
            hashTable[i]=new LinkedList<Node>();
        }

        currentSize=0;
    }

    private int myHash(Node node){
        int hashCode=node.hashCode();

        hashCode %= hashTable.length;
        if(hashCode<0){
            hashCode +=hashTable.length;
        }

        return hashCode;
    }

    //链表的节点，存储key与value两个东东
    private class Node{

        K key;
        V value;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
        }

        //需要重写它的equal方法，以便使用链表的查找方法
        @Override
        public boolean equals(Object o) {
            //首先，要看看obj是不是自己
            if (o == this)
                return true;

            //然后，要看看obj是不是Node>类型的
            if (!(o instanceof SeparateChainingHashTable.Node)) {
                return false;
            }
            Node e = (Node)o;
            //key与value都想等，才认为相等
//            if(key.equals(e.key) && value.equals(e.value)) return true;
            //只要key，就认为相等，这样就不会出现一个key对应多个value的情况
            if(key.equals(e.key) ){
                return true;
            }

            return false;
        }

        //需要重写它的hashcode方法，以便调用获取hash值
        @Override
        public int hashCode() {

            //参照HashMap的做法，返回这样一种不知道原理的hashcode
//            return key.hashCode() ^ value.hashCode();
//            通过key计算hash
            return key.hashCode();
//            int result = key != null ? key.hashCode() : 0;
//            result = 31 * result + (value != null ? value.hashCode() : 0);
//            return result;
        }
    }

    /**
     * 找到刚好比n大的素数
     * @param n 我就是n，^_^，哇哈哈哈……
     * @return 刚好比n大的素数
     */
    static int nextPrime(int n){
//        private int nextPrime(int n){
        int m=n;

        while(!isPrime(m)){
            //还不是素数？继续加吧！
            m++;
        }

        return m;
    }

    /**
     * 判断是否为素数，如果在2到根号m之间的数都不能被m整除的话，m就是素数了
     * @param m 没错，我就是传说中的m
     * @return 娃哈哈……
     */
    static boolean isPrime(int m){
//        private boolean isPrime(int m){
        if(m<=3){
            //[1,3]之内的数，除了1,都是素数
            return m>1;
        }

        //判断在2到根号m之间的数能不能被m整除
        double i=Math.sqrt(m);
        for(int j=2;j<=i;j++){
            //能被整除，假的
            if (m%j==0) return false;
        }

        return true;
    }

}
