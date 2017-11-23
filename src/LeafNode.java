
import java.util.*;

public class LeafNode extends Node {
	
	//EACH LEAFNODE HAVING ARRAYLIST OF DATANODES AND POINTERS TO NEXT NODE AND PREVIOUS NODE
	
	protected ArrayList<DataNode> dataNodes = new ArrayList<DataNode>();
	protected LeafNode prev = null;
	protected LeafNode next = null;
	
	
	
	protected LeafNode() {
		this.isLeaf = true;
	}
	
	//INSERT A DATANODE INTO A LEAFNODE
	protected void insertNode(DataNode dataNode){
		if(dataNodes.isEmpty()){
			dataNodes.add(dataNode);
		}
		else{ //INSERTING THE DATANODE IN ITS RIGHT POSITION (SORTED WAY)
			for(int i = 0; i < dataNodes.size(); i++){
				if(dataNodes.get(i).key > dataNode.key){
					dataNodes.add(i, dataNode);
					return;
				}
			}
			dataNodes.add(dataNode);
		}
	}
}
