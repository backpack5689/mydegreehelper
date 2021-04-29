package com.CENAA.mydegreehelper;

public class API {
    private static final String ROOT_URL = "http://3.138.206.104/v1/api.php?apicall=";

    public static final String URL_CREATE_BP = ROOT_URL + "createdegree";
    public static final String URL_READ_BP = ROOT_URL + "getdegree";
    public static final String URL_GETALL_BP = ROOT_URL + "showalldegree";
    public static final String URL_UPDATE_BP = ROOT_URL + "updatedegree";
    public static final String URL_DELETE_BP = ROOT_URL + "deletedegree&id=";
    public static final String URL_REGISTER = ROOT_URL + "signup";
    public static final String URL_LOGIN= ROOT_URL + "login";
    public static final String URL_GETUSER= ROOT_URL + "getuser";
    public static final String URL_UPDATE_USERP = ROOT_URL + "saveuserprogress";
    public static final String URL_UPDATE_USER = ROOT_URL + "updateUser";
}
