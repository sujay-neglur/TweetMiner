package models;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class Data {


   public List<Details> listCompletableFuture;
    public HashMap<String, Integer> cfStatMap;
    public String sentiment;
    public  HashMap<String, CompletableFuture<List<Details>>> displayMap;
}
