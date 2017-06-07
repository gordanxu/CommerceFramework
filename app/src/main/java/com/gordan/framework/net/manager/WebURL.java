package com.gordan.framework.net.manager;

/**
 * Created by Gordan on 2015/11/5.
 */
public class WebURL
{
    public static String URL_BASE = "http://10.101.111.186/";

    public static final String URL_PROVINCES_GET  = "csm_app/api/common/provinces"; //获取省份列表信息
    public static final String URL_CITES_GET = "csm_app/api/common/provinces/%s/cities"; //获取地市列表信息
    public static final String URL_AREAS_GET = "csm_app/api/common/provinces/%s/cities/%s/areas"; //获取区县列表信息
    public static final String URL_COMUNITIES_GET = "csm_app/api/common/provinces/%s/cities/%s/areas/%s/comunities"; //获取小区列表信息
    //获取 栋 单元 层 室 等信息
    public static final String URL_SUBAREAS_GET	= "csm_app/api/common/provinces/%s/cities/%s/areas/%s/comunities/%s/config";



}
