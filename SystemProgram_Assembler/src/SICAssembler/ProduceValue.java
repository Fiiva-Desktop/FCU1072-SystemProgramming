package SICAssembler;

import FileOperation.LoadFile;

public class ProduceValue {

	/*private String[] instruList_SIC = new String[]{	"ADD", "AND", "COMP", "DIV", "J", "JGT", "JEQ", "JLT", "JSUB",
													"LDA", "LDCH", "LDL", "LDX", "MUL", "OR", "RD", "RSUB", "STA",
													"STCH", "STL", "STSW", "STX", "SUB", "TD", "TIX", "WD"};
	*/
	public ProduceValue(LoadFile sicTable) {
		for(int i = 0 ; i < sicTable.getLines() ; i++) {
			
			if(sicTable.getInstrString(i).toLowerCase().equals("start") && i == 0) {
				int DecValue = new Integer(Integer.valueOf(sicTable.getValueString(0), 10));
				sicTable.addLocatTable(0 , DecValue);
				sicTable.addLocatTable(1 , DecValue);				
			}
			else if(sicTable.getInstrString(i).length() == 4 && sicTable.getInstrString(i).substring(0, 3).toLowerCase().equals("res")) {
				int space = Integer.parseInt(sicTable.getValueString(i));
				if(sicTable.getInstrString(i).toLowerCase().equals("resw")) {
					space *= 3;
					int temp = new Integer(sicTable.getLocationValue(i) + space);
					sicTable.addLocatTable(temp);
				}
				else {
					int temp = new Integer(sicTable.getLocationValue(i) + space);
					sicTable.addLocatTable(temp);
				}
			}
			else if(sicTable.getInstrString(i).toLowerCase().equals("byte")) {
				int length = sicTable.getValueString(i).substring(2, sicTable.getValueString(i).length()-1).length();				
				if(sicTable.getValueString(i).toLowerCase().charAt(0) == 'c') {
					length *= 2;
					sicTable.addLocatTable(sicTable.getLocationValue(i)+length);
				}
				else if(sicTable.getValueString(0).toLowerCase().charAt(0) == 'x') {
					sicTable.addLocatTable(sicTable.getLocationValue(i)+length);
				}
			}
			else {				
				int temp = new Integer(sicTable.getLocationValue(i)+3);
				sicTable.addLocatTable(temp);
			}			
		}

//--------------------------------------------------------------------------------------------		
		
		for(int i = 0 ; i < sicTable.getLines() ; i++) {
			if(sicTable.getInstrString(i).toLowerCase().equals("start")) {
				if(i != 0) {
					System.out.println("Error: START in line " + (i+1));
					System.exit(i);
				}
				sicTable.addObjecTable(" ");
			}
			else if(sicTable.getInstrString(i).toLowerCase().equals("end")) {
				if(i != sicTable.getLines()-1) {
					System.out.println("Error: END in line " + (i+1));
					System.exit(i);
				}
				sicTable.addObjecTable(" ");				
			}
			else if(sicTable.getInstrString(i).length() == 4 && sicTable.getInstrString(i).substring(0, 3).toLowerCase().equals("res")){
				sicTable.addObjecTable("");
			}
			else if(sicTable.getInstrString(i).toLowerCase().equals("word")) {
				String temp = String.format("%6s", Integer.toHexString(Integer.parseInt(sicTable.getValueString(i)))).replace(" ", "0").toUpperCase();
				sicTable.addObjecTable(temp);
			}
			else if(sicTable.getInstrString(i).toLowerCase().equals("byte")) {
				if(sicTable.getValueString(i).toLowerCase().charAt(0) == 'c') {
					String subs = sicTable.getValueString(i).toUpperCase().substring(2, sicTable.getValueString(i).length()-1);
					String ascii = new String();					
					for(int j = 0 ; j < subs.length() ; j++) {
						ascii = ascii + String.valueOf(Integer.toHexString((int)subs.charAt(j)));
					}
					sicTable.addObjecTable(ascii);
				}
				else if(sicTable.getValueString(i).toLowerCase().charAt(0) == 'x') {
					String subs = sicTable.getValueString(i).toUpperCase().substring(2, sicTable.getValueString(i).length()-1);
					sicTable.addObjecTable(subs);
				}
			}
			else {
				/*
				 * 4 Bytes = lacationTable.get(i -1) + 4
				 * 3 Bytes = lacationTable.get(i -1) + 3
				 * 2 Bytes = lacationTable.get(i -1) + 2
				 */	
				
				String objcode = new String();
				switch(sicTable.getInstrString(i).toLowerCase()){
					case "add":
						objcode = objcode + "18";
						break;
					case "and":
						objcode = objcode + "40";
						break;
					case "comp":
						objcode = objcode + "28";
						break;
					case "div":
						objcode = objcode + "24";
						break;
					case "j":
						objcode = objcode + "3C";
						break;
					case "jgt":
						objcode = objcode + "34";
						break;
					case "jeq":
						objcode = objcode + "30";
						break;
					case "jlt":
						objcode = objcode + "38";
						break;
					case "jsub":
						objcode = objcode + "48";
						break;
					case "lda":
						objcode = objcode + "00";
						break;
					case "ldch":
						objcode = objcode + "50";
						break;
					case "ldl":
						objcode = objcode + "08";
						break;
					case "ldx":
						objcode = objcode + "04";
						break;
					case "mul":
						objcode = objcode + "20";
						break;
					case "or":
						objcode = objcode + "44";
						break;
					case "rd":
						objcode = objcode + "D8";
						break;
					case "rsub":
						objcode = objcode + "4C";
						break;
					case "sta":
						objcode = objcode + "0C";
						break;
					case "stch":
						objcode = objcode + "54";
						break;
					case "stl":
						objcode = objcode + "14";
						break;
					case "stsw":
						objcode = objcode + "E8";
						break;
					case "stx":
						objcode = objcode + "10";
						break;
					case "sub":
						objcode = objcode + "1C";
						break;
					case "td":
						objcode = objcode + "E0";
						break;
					case "tix":
						objcode = objcode + "2C";
						break;
					case "wd":
						objcode = objcode + "DC";
						break;					
					//-----------------------------------	
					case "resw":
						objcode = objcode + "00";
						break;
					case "resb":
						objcode = objcode + "00";
						break;
					case "word":
						String temp01 = Integer.toHexString(Integer.parseInt(sicTable.getValueString(i)));
						String wordValue = String.format("%06s", temp01);
						sicTable.addObjecTable(wordValue);
						System.out.println("WORD : " + wordValue);
						break;
					case "byte":
						// 處理大於255的數值 
						String byteValue = Integer.toHexString(Integer.parseInt(sicTable.getValueString(i)));
						sicTable.addObjecTable(byteValue);
						System.out.println("BYTE : " + byteValue);
						break;				
					//----------------------------------	
					default:
						System.out.println("No matching instructions in line\n\"" + 
								String.format("%-8s", sicTable.getLabelString(i)) + 
								String.format("%-8s", sicTable.getInstrString(i)) + 
								String.format("%-8s", sicTable.getValueString(i)) +
								"\"");
						System.exit(i);
						break;
				}
				for(int j = 0 ; j < sicTable.getLines() ; j++) {
					int vallen = new Integer(sicTable.getValueString(i).length());
					int valx = new Integer(0);
					String val = new String(sicTable.getValueString(i).toLowerCase());
					if(sicTable.getValueString(i).length() > 2 && sicTable.getValueString(i).toLowerCase().substring(vallen-2, vallen).equals(",x")) {
						val = sicTable.getValueString(i).toLowerCase().substring(0, vallen-2);
						valx = valx + 32768;
					}					
					String lab = sicTable.getLabelString(j).toLowerCase();
					if(val.equals(lab)) {
						String fmt = String.format("%4s", String.valueOf(Integer.toHexString(sicTable.getLocationValue(j)+valx))).replace(" ", "0");
						objcode = objcode + fmt;
					}					
				}
				sicTable.addObjecTable(objcode);
			}
		}
	}
}

				
				