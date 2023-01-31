// Jewel Rahman CS211 4/27/22
// This program reads an input file of preferences and find a stable marriage
// scenario.  The algorithm gives preference to either men or women depending
// upon whether this call is made from main:
//      makeMatches(men, women);
// or whether this call is made:
//      makeMatches(women, men);

import java.io.*;
import java.util.*;

public class StableMarriage {
    public static final String LIST_END = "END";

    public static void main(String[] args) throws FileNotFoundException {
        Scanner console = new Scanner(System.in);
        System.out.print("What is the input file? ");
        String fileName = console.nextLine();
        Scanner input = new Scanner(new File(fileName));
        System.out.println();

        List<Person> men = readHalf(input);
        List<Person> women = readHalf(input);
        makeMatches(men, women);
        writeList(men, women, "Matches for men");
        writeList(women, men, "Matches for women");
    }

    public static Person readPerson(String line) {
        int index = line.indexOf(":");
        Person result = new Person(line.substring(0, index));
        Scanner data = new Scanner(line.substring(index + 1));
        while (data.hasNextInt()) {
            result.addChoice(data.nextInt());
        }
        return result;
    }

    public static List<Person> readHalf(Scanner input) {
        List<Person> result = new ArrayList<Person>();
        String line = input.nextLine();
        while (!line.equals(LIST_END)) {
            result.add(readPerson(line));
            line = input.nextLine();
        }
        return result;
    }

    public static void makeMatches(List<Person> list1, List<Person> list2) {
	//------------------------------------
        // Implement your code here
	//------------------------------------
    	
    	// set each person to be free
        for (Person m : list1) {
            m.erasePartner(); // sets men free
        }
        
        for (Person w: list2) {
            w.erasePartner(); // sets women free
        }
        
        int m_index = 0;
        
        //for every male in the index thats free and has choices
        for(m_index = 0; m_index < list1.size(); m_index++) {
        	if(!list1.get(m_index).hasPartner() && list1.get(m_index).hasChoices()) {
        		Person m = list1.get(m_index);
        		Person w = list2.get(m.getFirstChoice());
        		
        		//if a women has already has a partner, it will be erased from list1  
        		if(w.hasPartner()) {
        			list1.get(w.getPartner()).erasePartner();
        		}
        		//set m and w to be engaged to each other
        		w.setPartner(m_index);
        		m.setPartner(m.getFirstChoice());
        		
        		//determing the number of men that should be removed from w preferences
        		for(int i = w.getChoices().indexOf(m_index) +1; i < w.getChoices().size();) {
        			int m_num = w.getChoices().get(i);
        			
        			//women is deleted from pref list
        			w.getChoices().remove(i);
        		
        		//removing taken women from list	
        		list1.get(m_num).getChoices().remove(list1.get(m_num).getChoices().indexOf(m.getFirstChoice()));
        		
        		}
        		//set back to zero to loop over the list again
        		m_index = 0;
        	}
        }
    
    }
    public static void writeList(List<Person> list1,  List<Person> list2,
                                 String title) {
        System.out.println(title);
        System.out.println("Name           Choice  Partner");
        System.out.println("--------------------------------------");
        int sum = 0;
        int count = 0;
        for (Person p : list1) {
            System.out.printf("%-15s", p.getName());
            if (!p.hasPartner()) {
                System.out.println("  --    nobody");
            } else {
                int rank = p.getPartnerRank();
                sum += rank;
                count++;
                System.out.printf("%4d    %s\n", rank,
                                  list2.get(p.getPartner()).getName());
            }
        }
        System.out.println("Mean choice = " + (double) sum / count);
        System.out.println();
    }
}
