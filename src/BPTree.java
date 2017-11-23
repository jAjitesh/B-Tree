
import java.util.*;

public class BPTree {
	
	public static Node root = null;
	public static int degree;
	
	public BPTree(int degree){
		this.degree = degree;
	}
	
	// ******************************************INSERT***************************************
	
	protected static Node insertNode(Node root, DataNode dataNode){
		//CHECK IF ROOT IS A LEAF OR NOT
		if(!root.isLeaf){ // IF NO
			root.isSplit = false;
			ArrayList<Double> roots = new ArrayList<Double>();
			roots = ((IntNode)root).keys; // ARRAYLIST OF ROOT KEYS
			int k = 0;
			while(k < roots.size()){
				//TO INSERT THE NEW NODE 
				if(roots.get(k) > dataNode.key || k == roots.size()-1) {
					int pos = roots.get(k) > dataNode.key ? k : k + 1;  //CHECKING FOR THE RIGHT POSITION IN THE NODE
					
					Node node = new Node();
					node = insertNode(((IntNode)root).intPtrs.get(pos), dataNode); //RECURSIVELY CHECKS TILL THE FINAL LEVEL USING POINTERS(NEXT, PREV) AND INSERTS AT THE RIGHT PLACE
					
					if(node.isSplit){ // AFTER INSERTION IF THE DATANODE SIZE == DEGREE THEN IT SPLITS
						
						((IntNode)root).insert(((IntNode)node).keys.get(0), ((IntNode)node).intPtrs.get(0),
								((IntNode)node).intPtrs.get(1)); // INSERTING BETWEEN THE POINTERS(IN THE LEAF NODES)
						if(((IntNode)root).keys.size() == degree){ //CHECKING THE DEGREE CONDITION, WHICH IS TRUE
							int splitPosition = ((int) Math.ceil(((double)degree)/2)) - 1;
							IntNode intNode = new IntNode();
							IntNode childNode = new IntNode();
							intNode.insert(((IntNode)root).keys.get(splitPosition), root, childNode); //ADDING THE KEY AT SPLIT POSITION TO THE PARENT FOR POINTERS
							((IntNode)root).keys.remove(splitPosition);//REMOVING IT IN THE INTERNAL NODE
							
							for(int i = splitPosition, j = splitPosition; i < degree-1; i++){// SPLITTING THE NODE AT SPLIT POSITION(BY REMOVING FROM CURRENT NODE TO ADDING TO A NEW NODE)
								childNode.keys.add(((IntNode)root).keys.remove(j));
							}
							int j = splitPosition + 1;
							for(int i = splitPosition+1; i <= degree; i++){ //UPDATING POINTERD ACCORDINGLY
								childNode.intPtrs.add(((IntNode)root).intPtrs.remove(j));
							}
							intNode.isSplit = true;
							return intNode;
						}
						return root;
					}
					else // IF THERE'S NO SPLIT
						return root;
					//break;
				}
				k++;
			}
		}
		else{ //IF ROOT IS A LEAF NODE
			((LeafNode)root).insertNode(dataNode); //INSERT NODE 

			if(((LeafNode)root).dataNodes.size() == degree){ // IF DEGREE CONDITION IS MET THEN SPLIT
				int position = degree/2; //CENTER SPLIT
				LeafNode leaf = new LeafNode();
				for(int i = position, j = position; i < degree; i++){ //SPLITTING THE NODE AT SPLIT POSITION(BY REMOVING FROM CURRENT NODE TO ADDING TO A NEW NODE)
					leaf.insertNode(((LeafNode)root).dataNodes.remove(j)); 
				}
				
				//UPDATING THE NEXT AND THE PREV POINTER FOR OUR NEW ROOT
				leaf.next = ((LeafNode)root).next;
				((LeafNode)root).next = leaf;
				leaf.prev = ((LeafNode)root);
				if(leaf.next != null)
					leaf.next.prev = leaf;
				
				//UPDATING SPLIT KEY TO NEW ROOT
				IntNode split = new IntNode();
				split.keys.add(leaf.dataNodes.get(0).key);
				split.isSplit = true;
				split.intPtrs.add((LeafNode)root);
				split.intPtrs.add(leaf);
				return split;
			}
			else
				return root;
		}
		
		return root;
	}
	
				//INCALL FUNCTION(RECURSIVE) TO INSERT A NEW NODE
	protected static void insertNode(double key, String value){
		DataNode dataNode = new DataNode(key, value);
		if(root == null){
			root = new LeafNode();
			((LeafNode)root).insertNode(new DataNode(key, value));
		}
		else{
			root = insertNode(root, dataNode);
			
		}
		
	}
	
	//***************************************SEARCH*************************************
	
	protected static String search(double key){
		Node node = new Node();
		node = root;
		//IF NODE IS NOT A LEAF
		while(!node.isLeaf){
			int i = 0;
			while(i < ((IntNode)node).keys.size() && ((IntNode)node).keys.get(i) < key){ // TRAVERSES UNTIL THE LEAST VALUE BEFORE THE KEY IN OUR KEYS ARRAYLIST
				i++;
			}
			node = ((IntNode)node).intPtrs.get(i); // GET TO THE POSITION WHERE IT MEETS THE KEY (NULL OTHERWISE)
		}
		//TO HANDLE DUPLICATES AND TO PRINT ALL THE DUPLICATES IN A SORTED MANNER OF THEIR INSERTS
		StringBuilder str = new StringBuilder();
		
		boolean flag = true;
		while((LeafNode)node != null && flag){
			for(int i=0; i < ((LeafNode)node).dataNodes.size(); i++){
				double keyVal = ((LeafNode)node).dataNodes.get(i).key; 
				if(keyVal == key){
					str.append(((LeafNode)node).dataNodes.get(i).val).append(", "); //APPENDING ALL THE VALUES WITH SAME KEYS TO OUR STRINGBUILDER
				}
				else if( keyVal > key){ //BREAK CONDITION IF NOT EQUAL
					flag = false;
					break;
				}
			}
			node = ((LeafNode)node).next;
		}
		if(str.toString().equals("")){ // IF NONE FOUND
			return "Null";	
		}
			
		else{
			return str.toString();
		}
	}

	//**************************RANGE SEARCH***********************************
	
	//INNER FUNCTION CALL FOR RANGE SEARCH
	protected static StringBuilder strHelp(StringBuilder str, Node node, Double key1, Double key2){
		
		ArrayList<DataNode> leafList = new ArrayList<DataNode>();
		leafList = ((LeafNode)node).dataNodes;
		for(int i = 0; i < leafList.size(); i++){
			if(leafList.get(i).key >= key1 && leafList.get(i).key <= key2 ){
				str.append("(").append(leafList.get(i).key).append(",").append(leafList.get(i).val).append("), "); //APPENDING ALL OUR RANGE KEY,VALUES
			}
			if(leafList.get(i).key > key2){
				return str;
			}
		}
		
		return str;
	}
	
	
	protected static String search(double key1, double key2){
		Node node = new Node();
		node = root;
		while(!node.isLeaf){ // TRAVERSE DOWN THE TREE UNTIL OUR NODE IS A LEAF
			int index = 0;
			while(index < ((IntNode)node).keys.size() && ((IntNode)node).keys.get(index) < key1){ // TRAVERSING DOWN ON THE RIGHT PATH AND GETTING THE INITIAL POSITION
				index++;
			}
			node = ((IntNode)node).intPtrs.get(index); // USING POINTERS ARRAYLIST, GETTING THE POINTER TO THE FIRST KEY NODE
		}
	
		StringBuilder str = new StringBuilder();  //TO APPEND KEY,VALUES FROM KEY1 TO KEY2
		str = strHelp(str, node, key1, key2); //APPENDS ALL THE KEY,VALUES FROM KEY1 TO KEY2 IN THE 1ST ENCOUNTERED NODE (WITH KEY1)
		while(((LeafNode)node).next != null){
			
			// CHECKING THE CONDITION FOR 1ST KEY NEXT NODE
			if(((LeafNode)node).next.dataNodes.get(0).key > key2){  
				break;
			}
			else{ // IF NEXT NODE HAS KEYS IN OUR RANGE
				str = strHelp(str, ((LeafNode)node).next, key1, key2); //APPEND TO OUR STRINGBUILDER str
			}
			node = ((LeafNode)node).next; // UPDATING OUR NODE
		}
		
		if(str.toString().equals("")){
			return "Null";	
		}	
		else{
			return str.toString();
		}
			
	}
	
}
