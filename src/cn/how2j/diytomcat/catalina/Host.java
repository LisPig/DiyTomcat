package cn.how2j.diytomcat.catalina;

import cn.how2j.diytomcat.util.Constant;
import cn.how2j.diytomcat.util.ServerXMLUtil;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Host {
    private String name;
    private Map<String,Context> contextMap;


    public String getName() {
        return name;
    }

    public Context getContext(String path) {
        return contextMap.get(path);
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setContextMap(Map<String, Context> contextMap) {
        this.contextMap = contextMap;
    }

    public Host(){
        this.contextMap = new HashMap<>();
        this.name = ServerXMLUtil.getHostName();
        scanContextsOnWebAppsFolder();
        scanContextsInServerXML();
    }

    private void scanContextsOnWebAppsFolder() {
        File[] folders = Constant.webappsFolder.listFiles();
        for(File folder : folders){
            if(!folder.isDirectory())
                continue;
            loadContext(folder);
        }
    }

    private void loadContext(File folder) {
        String path = folder.getName();
        if("ROOT".equals(path))
            path = "/";
        else
            path = "/"+path;

        String docBase = folder.getAbsolutePath();
        Context context = new Context(path,docBase);
        contextMap.put(context.getPath(),context);
    }

    private void scanContextsInServerXML() {
        List<Context> contexts = ServerXMLUtil.getContexts();
        for(Context context : contexts){
            contextMap.put(context.getPath(),context);
        }
    }


}
