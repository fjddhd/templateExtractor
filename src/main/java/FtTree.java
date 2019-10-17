import java.util.ArrayList;

public class FtTree {
    private static int maxChildren=5;
    public String value;
    public ArrayList<FtTree> childrens;

    public FtTree(String value) {
        this.value=value;
        this.childrens=new ArrayList<FtTree>();
    }
    public void cutOff(){
        if (childrens.size()>=maxChildren){
            childrens=new ArrayList<FtTree>();
        }
    }

}
