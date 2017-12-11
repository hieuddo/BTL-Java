package DataStore;
import java.util.Vector;

public class Class_info {
    private int position;
    private String name;
    private String accessLevel;
    private String type;
    protected Vector<String> implementsList;
    protected Vector<String> extendsList;
    protected String extendsTo;
    protected Vector<Member_info> atributesList;
    private Vector<Member_info> methodsList;

    public Class_info() {
            name = new String();
            accessLevel = new String();
            type = new String();
            implementsList = new Vector();
            extendsList = new Vector();
            atributesList = new Vector();
            methodsList = new Vector();
            extendsTo = new String();
    }
    public void setPos(int pos) {
        this.position = pos;
    }
    public int getPos() {
        return this.position;
    }
    public void setName(String name) {
            this.name = name;
    }
    public void setAccessLevel(String accessLevel) {
            this.accessLevel = accessLevel;
    }
    public void setType(String type) {
            this.type = type;
    }
    public void setExtendsList(Vector<String> parents) {
        this.extendsList = parents;
    }
    public void setImplementsList(Vector<String> parents) {
            this.implementsList = parents;
    }
    public void setExtendsTo(String parent) {
        this.extendsTo = parent;
    }
    public void setAtributesList(Vector<Member_info> atb) {
            this.atributesList = atb;
    }
    public Vector<String> getAttributesList() {
        Vector<String> atb = new Vector<String>();
        for (Member_info mi : atributesList) atb.add(mi.getName());
        return atb;
    }
    public Vector<String> getMethodsList() {
        Vector<String> m = new Vector<String>();
        for (Member_info mi : methodsList) m.add(mi.getName());
        return m;
    }
    public void setMethodsList(Vector<Member_info> met) {
            this.methodsList = met;
    }
    public String getInfo() {
            String s = new String();
            s += accessLevel + "\n";
            s += type + "\n";
            s += name + "\n";
            s += "extends: ";	s += extendsTo; //for (String st : extendsList) s += st + " ";
            s += "\nimplements: ";	for (String st : implementsList) s += st + "+";
            s += "\natributes:\n";	for (Member_info mb : atributesList) s += "\t" + mb.getInfo() + "\n";
            s += "\nmethods:\n";	for (Member_info mb : methodsList) s += "\t" + mb.getInfo() + "\n";
            return s;
    }
    public String getName() {
        return this.name.trim();
    }
}