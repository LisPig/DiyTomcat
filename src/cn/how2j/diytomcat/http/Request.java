package cn.how2j.diytomcat.http;

import cn.how2j.diytomcat.Bootstrap;
import cn.how2j.diytomcat.catalina.Context;
import cn.how2j.diytomcat.catalina.Engine;
import cn.how2j.diytomcat.catalina.Host;
import cn.how2j.diytomcat.util.MiniBrowser;
import cn.hutool.core.util.StrUtil;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

public class Request {
    private String requestString;
    private String uri;
    private Socket socket;
    private Context context;
    private Host host;
    private Engine engine;
    public Request(Socket socket,Engine engine) throws IOException{
        this.socket = socket;
        //this.host = host;
        this.engine = engine;
        parseHttpRequest();
        if(StrUtil.isEmpty(requestString))
            return;
        parseUri();
        parseContext();
        if(!"/".equals(context.getPath()))
            uri = StrUtil.removePrefix(uri,context.getPath());
    }

    private void parseContext(){
        String path = StrUtil.subBetween(uri,"/","/");
        if(null == path)
            path = "/";
        else
            path = "/" + path;

        context = engine.getDefaultHost().getContext(path);
        if(null == context)
            context = engine.getDefaultHost().getContext("/");
    }

    private void parseHttpRequest() throws IOException{
        InputStream is = this.socket.getInputStream();
        byte[] bytes = MiniBrowser.readBytes(is);
        requestString = new String(bytes,"utf-8");
    }

    private void parseUri(){
        String temp;
        temp = StrUtil.subBetween(requestString," ","");
        if(!StrUtil.contains(temp,'?')){
            uri = temp;
            return;
        }
        temp = StrUtil.subBefore(temp,'?',false);
        uri = temp;
    }

    public String getUri(){
        return uri;
    }

    public String getRequestString(){
        return requestString;
    }

    public Context getContext() {
        return context;
    }
}
