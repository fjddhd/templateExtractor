import Utils.MapSortUtil;
import Utils.WriteHtml;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.FileReader;
import cn.hutool.core.io.file.FileWriter;

import java.io.File;
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
//        addressFiles();//合并日志文件

        String filePath="D:\\javaDir\\templateExtractor\\src\\main\\resources\\Zookeeper_2k.log";
        String filePath1="D:\\javaDir\\templateExtractor\\src\\main\\resources\\Cron\\cron";
//        int cutTimeStamp=27;
        bulidFtTree(filePath1,16,6,"cron_16_6");
    }
    public static void bulidFtTree(String filePath,int cutTimeStamp,int MaxChildren,String htmlResultName){
        FileReader fileReader = new FileReader(filePath);
        List<String> dataListRaw=fileReader.readLines();
//        for (int i=0;i<dataList.size();++i){
//            System.out.println(dataList.get(i));
//        }
        List<String> dataList=new ArrayList<String>();
        //手动剔除日期
        for (int i=0;i<dataListRaw.size();++i){
            dataList.add(String.copyValueOf(dataListRaw.get(i).toCharArray(),cutTimeStamp,dataListRaw.get(i).length()-cutTimeStamp));
        }
        //符号=，/，：转空格
        for (int i=0;i<dataListRaw.size();++i){
            String tempS = dataList.get(i);
            tempS=tempS.replaceAll("="," ");
            tempS=tempS.replaceAll("\\["," ");
            tempS=tempS.replaceAll(":"," ");
            tempS=tempS.replaceAll("-"," ");
            tempS=tempS.replaceAll("\\]"," ");
            tempS=tempS.replaceAll("\\*"," ");
//            tempS=tempS.replaceAll("-"," ");
            dataList.set(i,tempS);
        }


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
            String[] ss=dataList.get(i).split(" +");//根据1次或多次空格分词
            List<String> addressedLine=new ArrayList<String>();
            HashMap<String,Integer> tempHm=new HashMap<String, Integer>();
            for (int j=0;j<ss.length;++j){
                tempHm.put(ss[j],hm.get(ss[j]));
            }
            List<String> sortedList=MapSortUtil.sortByValue(tempHm);//按频率从低到高排序
            for (int j=sortedList.size()-1;j>=0;--j){//相反顺序插入到addressLine中
                addressedLine.add(sortedList.get(j));
            }
            L.add(addressedLine);
        }
        //-TODO 可以让这个函数输出L然后与其他fttree操作联合用
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
                ArrayList<FtTree> childrens = cur.childrens;
                ArrayList<String> asist=new ArrayList<String>();
                for (int k=0;k<childrens.size();++k){
                    asist.add(childrens.get(k).value);
                }
                if (!asist.contains(lOfLine.get(j))){
                    cur=new FtTree(lOfLine.get(j));
                    childrens.add(cur);
                }else {
                    cur=childrens.get(asist.indexOf(lOfLine.get(j)));
                }
            }

        }
        /**
         * 按层输出层序遍历，但无用，因为不能表示层间连接方式
         * */
//        List<List<String>> LLS=Tra(ftTree);
//        for (int i=0;i<LLS.size();++i){
//            for (int j=0;j<LLS.get(i).size();++j){
//                System.out.print(LLS.get(i).get(j));
//                System.out.print(" , ");
//            }
//            System.out.println();
//        }
        //遍历加剪枝
        CutOffByTra(ftTree,true,MaxChildren);
        //保存可视化FtTree
        saveFttreeHtml(ftTree,htmlResultName);

        System.out.println(" ");//用于debug断点看树结构
    }

    public static void CutOffByTra(FtTree ftTree, boolean needCutOff,int MaxChildren){
//        System.out.println(FtTree.getValue());
        ArrayDeque<FtTree> ad=new ArrayDeque<FtTree>();
        ad.push(ftTree);
//        List<FtTree> childrens=FtTree.getChildrens();
        while (!ad.isEmpty()){
            FtTree popFtTree = ad.pop();
            ArrayList<FtTree> childrens = popFtTree.childrens;
            popFtTree.cutOff(MaxChildren);
//            System.out.println(popFtTree.value);
            for (int i=0;i<childrens.size();++i){
                ad.push(childrens.get(i));
            }
        }

    }
    //FtTree转可视化的格式字符串
    public static String saveNode(FtTree ftTree){
        //节点编号
        ArrayList<FtTree> FtTreeList=new ArrayList<FtTree>();
        ArrayDeque<FtTree> ad=new ArrayDeque<FtTree>();
        ad.push(ftTree);
//        List<FtTree> childrens=FtTree.getChildrens();
        while (!ad.isEmpty()){
            FtTree popFtTree = ad.pop();
            ArrayList<FtTree> childrens = popFtTree.childrens;
//            System.out.println(popFtTree.value);
            FtTreeList.add(popFtTree);
            for (int i=0;i<childrens.size();++i){
                ad.push(childrens.get(i));
            }
        }
//        System.out.println(FtTree.getValue());
        StringBuilder sb1=new StringBuilder();
        StringBuilder sb2=new StringBuilder();
        StringBuilder sb3=new StringBuilder();
        ArrayDeque<FtTree> ad1=new ArrayDeque<FtTree>();
        ad1.push(ftTree);
//        List<FtTree> childrens=FtTree.getChildrens();
        while (!ad1.isEmpty()){
            FtTree popFtTree = ad1.pop();
            sb2.append(FtTreeList.indexOf(popFtTree));
            sb2.append("[label=\"");
            sb2.append(popFtTree.value);
            sb2.append("\"];");
            ArrayList<FtTree> childrens = popFtTree.childrens;
//            System.out.println(popFtTree.value);
            for (int i=0;i<childrens.size();++i){
                ad1.push(childrens.get(i));
                sb3.append(FtTreeList.indexOf(popFtTree)+"--"+FtTreeList.indexOf(childrens.get(i))+";");
            }

        }
        sb1.append(sb2);
        sb1.append(sb3);
        return sb1.toString();

    }
    public static void saveFttreeHtml(FtTree ftTree,String htmlResultName){
        StringBuilder sb=new StringBuilder();
        sb.append("graph g {");
        sb.append(saveNode(ftTree));
        sb.append("}");
        System.out.println(sb.toString());

        String html = WriteHtml.generateHTML(sb.toString());
        String file = "D:/graph/"+htmlResultName+".html";  // 自定义输出路径
        WriteHtml.writeHTML(file, html);
    }

    //按层打印FtTree
    public static List<List<String>> Tra(FtTree root){
        List<List<String>> reslist = new LinkedList<List<String>>();
        List<String> list = new LinkedList<String>();
        if(root==null){
            return reslist;
        }
        int curCount = 0, curNum = 1, nextCount = 1;
        Queue<FtTree> queue = new LinkedList<FtTree>();
        FtTree t ;
        queue.offer(root);
        while (!queue.isEmpty()){
            t= queue.poll();
            list.add(t.value);
            if(t.childrens!=null){
                for(FtTree node :t.childrens){
                    if(node!=null){
                        queue.offer(node);
                    }
                    nextCount++;
                }
            }
            if (++curCount == curNum) {
                reslist.add(list);//增加元素，每次在0位置插入元素，其他往后顺延
                list = new LinkedList<String>();
                curNum = nextCount;
            }

        }
        return reslist;
    }

    public static void addressFiles(){
        for (int i=1;i<=33;++i){
            FileReader fileReader = new FileReader("D:\\javaDir\\templateExtractor\\src\\main\\resources\\Cron\\cron."+i);
            List<String> dataListRaw=fileReader.readLines();
            FileUtil.writeLines(dataListRaw,"D:\\javaDir\\templateExtractor\\src\\main\\resources\\Cron\\cron",new String(),true);
        }

    }


}
