

import java.util.*;

public class IntNode extends Node {
	
	protected ArrayList<Double> keys = new ArrayList<Double>();
	protected ArrayList<Node> intPtrs = new ArrayList<Node>();
	
	// INSERTING, IN A SORTED WAY, INSIDE A NODE AND UPDATING THE POINTERS
	protected void insert(double key, Node ptr1, Node ptr2){
		if(this.keys.isEmpty()){
			this.keys.add(key);
			this.intPtrs.add(ptr1);
			this.intPtrs.add(ptr2);
		}
		else{

			int i = 0;
			while(i < this.keys.size()){
				if(this.keys.get(i) > key) {

					this.keys.add(i, key);
					this.intPtrs.add(i + 1, ptr2);
					
					return;
				}
				i++;
			}
			this.keys.add(key);
			this.intPtrs.add(ptr2);
		}
		return;
	}
	
}
