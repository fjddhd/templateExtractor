import Utils.MapSortUtil;
import cn.hutool.core.io.file.FileReader;

import java.util.*;

public class MainClass {
    public static void main(String[] args) {
//        HashMap<String,Integer> hm=new HashMap();
//        hm.put("asdsa",4);
//        hm.put("fgdf",2);
//        hm.put("gjhh",1);
//        hm.put("qasdsda",7);
//        hm.put("wassdsa",5);
//        hm.put("basdsa",9);
//        List<String> list=MapSortUtil.sortByValue(hm);
//        System.out.println("----------测试map排序----------");
//        for (int i=0;i<list.size();++i){
//            System.out.println(list.get(i));
//        }
        bulidFtTree();
    }
    public static void bulidFtTree(){
        FileReader fileReader = new FileReader("D:\\javaDir\\templateExtractor\\src\\main\\resources\\Apache_2k.log");
        List<String> dataList=fileReader.readLines();
//        for (int i=0;i<dataList.size();++i){
//            System.out.println(dataList.get(i));
//        }
        List<List<String>> L=new ArrayList<List<String>>();
        HashMap<String,Integer> hm=new HashMap();
        for (int i=0;i<dataList.size();++i){
            String[] ss=dataList.get(i).split(" ");
            HashSet<String> hs=new HashSet();
            for (int j=0;j<ss.length;++j){
                hs.add(ss[j]);
            }
            List<String> list=new ArrayList<String>(hs);
            for (int j=0;j<list.size();++j){
                if (hm.containsKey(list.get(j))){
                    hm.put(list.get(j),hm.get(list.get(j))+1);
                }else {
                    hm.put(list.get(j),1);
                }
            }
        }
        for (int i=0;i<dataList.size();++i){
            String[] ss=dataList.get(i).split(" ");
            List<String> addressedLine=new ArrayList<String>();
            HashMap<String,Integer> tempHm=new HashMap<String, Integer>();
            for (int j=0;j<ss.length;++j){
                tempHm.put(ss[j],hm.get(ss[j]));
            }
            List<String> sortedList=MapSortUtil.sortByValue(tempHm);
            for (int j=sortedList.size()-1;j>=0;--j){
                addressedLine.add(sortedList.get(j));
            }
            L.add(addressedLine);
        }
        //-TODO 可以让这个函数输出L然后与其他fttree操作联合用
        //-TODO 以下代码需要测试是否有错
        FtTree ftTree=new FtTree("start");
//        int mark=0;
        for (int i=0;i<L.size();++i){
            List<String> lOfLine = L.get(i);
//            HashMap<String,Integer> hmOfLine=new HashMap();
            FtTree cur= ftTree;
//            if (i!=0 && !lOfLine.get(0).equals(ftTree.getChildrens().get(0).getValue())) {
//                continue;
//            }
//            mark++;
            for (int j=0;j<lOfLine.size();++j){
                ArrayList<FtTree> childrens = cur.getChildrens();
                ArrayList<String> asist=new ArrayList<String>();
                for (int k=0;k<childrens.size();++k){
                    asist.add(childrens.get(k).getValue());
                }
                if (!asist.contains(lOfLine.get(j))){
                    cur=new FtTree(lOfLine.get(j));
                    childrens.add(cur);
                }else {
                    cur=childrens.get(asist.indexOf(lOfLine.get(j)));
                }
            }

        }
        System.out.println(" ");//用于debug断点看树结构
    }
    //-TODO 可在一次遍历中做剪枝
    public void traserval(FtTree ftTree, boolean needCutOff){
//        System.out.println(FtTree.getValue());
        ArrayDeque<FtTree> ad=new ArrayDeque<FtTree>();
        ad.push(ftTree);
//        List<FtTree> childrens=FtTree.getChildrens();
        while (!ad.isEmpty()){
            FtTree popFtTree = ad.pop();
            ArrayList<FtTree> childrens = popFtTree.getChildrens();
            System.out.println(popFtTree.getValue());
            if (needCutOff){
                ftTree.cutOff();
                continue;
            }
            for (int i=0;i<childrens.size();++i){
                ad.push(childrens.get(i));
            }
        }

    }


}
