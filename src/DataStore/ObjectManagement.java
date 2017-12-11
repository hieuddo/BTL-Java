package DataStore;

import java.util.Vector;

public class ObjectManagement {
    private Vector< Class_info > myList;
    private int[][] relationship = new int[1000][1000];

    public ObjectManagement() {
        myList = new Vector();
    }
    public void addObject(Class_info cl) {
        myList.add(cl);
        cl.setPos(myList.size()-1);
    }
    public Vector< Class_info > getMyList() {
        return this.myList;
    }
    public int getNumberOfClass() {
        return this.myList.size();
    }
    public void initRela() {
        // 1 : extends
        // 2 : implements
        // 3 : association
        
        Vector< Class_info > newList = new Vector();
        for (Class_info cl : myList) {
            for (Class_info cl2 : myList) {
                if (cl2.getName().trim().equals(cl.extendsTo)) {
                    relationship[cl2.getPos()][cl.getPos()] = 1;
                }
            }
            for (String parent : cl.implementsList) {
                for (Class_info cl2 : myList) {
                    if (cl2.getName().trim().equals(parent.trim())) {
                        relationship[cl2.getPos()][cl.getPos()] = 2;
                    }
                }
            }
            
            for (Class_info cl2 : myList) {
                for (Member_info atribute : cl.atributesList) {
                    if (atribute.getName().indexOf(cl2.getName().trim()) >= 0) {
                        relationship[cl2.getPos()][cl.getPos()] = 3;
                    }
                }
            }
        }

    }
    public String output() {
        String str = new String();
        for (Class_info cl : myList) {
            str += cl.getInfo() + "\n-------------------------------------------------------\n";
        }
        System.out.println(str);
        return str;
    }
}
