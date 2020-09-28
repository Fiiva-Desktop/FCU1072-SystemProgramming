package FileOperation;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class LoadFile {
	
	private ArrayList<String> labelTable = new ArrayList<String>();
	private ArrayList<String> instrTable = new ArrayList<String>();	
	private ArrayList<String> valueTable = new ArrayList<String>();
	private ArrayList<Integer> locatTable = new ArrayList<Integer>();
	private ArrayList<String> objCodeTable = new ArrayList<String>();
	private String filePath = new String();

	public LoadFile(String filePath) {
		this.filePath = filePath;
		try {
			FileReader file = new FileReader(filePath);
			BufferedReader buffer = new BufferedReader(file);
			String fileContent = null;
			while((fileContent = buffer.readLine()) != null) {
				if(fileContent.charAt(0) == '.') continue;				
				String[] temp = fileContent.split("\\s+");
				this.labelTable.add(temp[0]);
				this.instrTable.add(temp[1]);
				
				
				
				try {
					if(	temp[1].equalsIgnoreCase("RSUB")	||
						temp[1].equalsIgnoreCase("FLOAT")	||
						temp[1].equalsIgnoreCase("FIX")		||
						temp[1].equalsIgnoreCase("NORM")	||
						temp[1].equalsIgnoreCase("SIO")		||
						temp[1].equalsIgnoreCase("HIO")		||
						temp[1].equalsIgnoreCase("TIO") 	) this.valueTable.add("");
					else if(temp[1].toLowerCase().equals("end")) this.valueTable.add(temp[2]);
					else this.valueTable.add(temp[2]);
				}
				catch(Exception e) {
					this.valueTable.add(new String(this.getLabelString(0)));
				}
			}
			buffer.close();
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/* Mutator */
	public void addLocatTable(int location) {
		this.locatTable.add(location);
	}
	
	public void addLocatTable(int index , int location) {
		this.locatTable.add(index , location);
	}	
	public void addObjecTable(String objCode) {
		this.objCodeTable.add(objCode);
	}
	
	
	/* Accessor */	
	//ArrayList
	public ArrayList<String> getLabelTable() {
		return this.labelTable;
	}
	
	public ArrayList<String> getInstrTable() {
		return this.instrTable;
	}
	
	public ArrayList<String> getValueTable() {
		return this.valueTable;
	}
	
	public ArrayList<Integer> getLocatTable(){
		return this.locatTable;
	}
	
	public ArrayList<String> getObjCodeTable(){
		return this.objCodeTable;
	}
	
	//String
	public String getLabelString(int index) {
		return this.labelTable.get(index);
	}	
	
	public String getInstrString(int index) {
		return this.instrTable.get(index);
	}	
	
	public String getValueString(int index) {
		return this.valueTable.get(index);
	}	
	
	//Value
	public int getLocationValue(int index) {
		return this.locatTable.get(index);
	}
	
	public String getObjectCode(int index) {
		return this.objCodeTable.get(index);
	}
	
	public int getLines() {
		return this.instrTable.size();
	}
	
	//get file name
	public String getFileName() {
		String fileName = this.filePath.substring(this.filePath.lastIndexOf("\\")+1, this.filePath.lastIndexOf('.'));
		return fileName;
	}
	
	public String getFileIn() {
		String fileIn = this.filePath.substring(0, this.filePath.lastIndexOf("\\")+1);
		return fileIn;
	}
}
