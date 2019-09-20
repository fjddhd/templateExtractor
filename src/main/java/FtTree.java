import java.util.ArrayList;

public class FtTree {
    private static int maxChildren=2;
    private String value;
    private ArrayList<FtTree> childrens;

    public FtTree(String value) {
        this.value=value;
        this.childrens=new ArrayList<FtTree>();
    }
    public void cutOff(){
        if (childrens.size()>=maxChildren){
            childrens=new ArrayList<FtTree>();
        }
    }


    public static int getMaxChildren() {
        return maxChildren;
    }

    public static void setMaxChildren(int maxChildren) {
        FtTree.maxChildren = maxChildren;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public ArrayList<FtTree> getChildrens() {
        return childrens;
    }

    public void setChildrens(ArrayList<FtTree> childrens) {
        this.childrens = childrens;
    }
}
