
import java.io.*;


public class adsTest {

	
	public static void main(String[] args) throws Exception {
		
		//CREATING INPUT AND OUTPUT FILES AND SUBSEQUENT BUFFERED READERS AND WRITERS
		
		//File inputFile = new File(args[0]);
		File inputFile = new File("/Users/madhavsarma/Downloads/input.txt");
		BufferedReader infile = new BufferedReader(new FileReader(inputFile));
		
		File outputFile = new File("output_file.txt");
		if(!outputFile.exists()){
			outputFile.createNewFile();
		}
		FileWriter filewriter = new FileWriter(outputFile.getAbsoluteFile());
		PrintWriter printwriter = new PrintWriter(filewriter);
		
		
		int degree = Integer.parseInt(infile.readLine().trim());
		BPTree Btree = new BPTree(degree); //CREATING A B PLUS TREE BY PARSING THE 1ST LIN, WHICH IS THE DEGREE
		
		String lineRead;
		
		while((lineRead = infile.readLine()) != null){
			if(lineRead.startsWith("Insert")){//CONDITION TO CALL INSERT FUNCTION IN OUR B+ TREE
				
				Btree.insertNode(Double.parseDouble(lineRead.split("Insert\\(")[1].split(",")[0]), lineRead.split(",")[1].split("\\)")[0]);
			
			}
			else if(lineRead.startsWith("Search")){//CONDITION TO CALL SEARCH FUNCTION IN OUR B+ TREE
				if(lineRead.contains(",")){ //CONDITION TO CALL RANGE SEARCH
					String range = Btree.search(Double.parseDouble(lineRead.split("Search\\(")[1].split(",")[0]),Double.parseDouble(lineRead.split("Search\\(")[1].split(",")[1].split("\\)")[0])).trim();
					if( range.charAt(range.length() - 1) == ','){
						range = range.substring(0, range.length() - 1);
					}
					printwriter.println(range);
				}
				else{ // CONDITION TO CALL NODE SEARCH
					String search = Btree.search(Double.parseDouble(lineRead.split("Search\\(")[1].split("\\)")[0])).trim();
					if( search.charAt(search.length() - 1) == ','){
						search = search.substring(0, search.length() - 1);
					}
					printwriter.println(search);
				}
			}
		}		
		filewriter.close();
		infile.close();
	}

}
