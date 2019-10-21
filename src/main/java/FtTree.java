import java.util.ArrayList;

public class FtTree {
    public String value;
    public ArrayList<FtTree> childrens;

    public FtTree(String value) {
        this.value=value;
        this.childrens=new ArrayList<FtTree>();
    }
    public void cutOff(int maxChildren){
        if (childrens.size()>=maxChildren){
            childrens=new ArrayList<FtTree>();
        }
    }

}
