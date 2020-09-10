package cn.how2j.diytomcat.util;

import cn.how2j.diytomcat.catalina.Context;
import cn.hutool.core.io.FileUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


import javax.swing.text.Document;
import java.io.File;

import static cn.how2j.diytomcat.util.Constant.webXmlFile;

public class WebXMLUtil {
    public static String getWelComeFile(Context context){
        String xml = FileUtil.readUtf8String(webXmlFile);
        org.jsoup.nodes.Document d = Jsoup.parse(xml);
        Elements es = d.select("Welcome-file");

        for(Element e : es){
            String welcomeFileName= e.text();
            File f = new File(context.getDocBase(),welcomeFileName);
            if(f.exists())
                return f.getName();
        }
        return "index.html";
    }
}
