package com.shidiqarifs.edokter.Helper;
/**
 * Created by user on 19/11/2017.
 */

public class get_url_link {
    public static String url_link = "http://api.e-dokter.xyz/";
    public String getUrl_link(String type){
        return url_link+type+".php";
    }
}
