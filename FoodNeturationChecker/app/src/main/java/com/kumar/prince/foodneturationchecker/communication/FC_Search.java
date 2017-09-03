package com.kumar.prince.foodneturationchecker.communication;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.kumar.prince.foodneturationchecker.data.model.FC_Product;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by prince on 30/8/17.
 *
 */
public class FC_Search {

    @SerializedName("page_size")
    @Expose
    private String page_size;
    @SerializedName("FCProducts")
    @Expose
    private List<FC_Product> FCProducts = new ArrayList<FC_Product>();
    @SerializedName("page")
    @Expose
    private String page;
    @SerializedName("count")
    @Expose
    private Integer count;
    @SerializedName("skip")
    @Expose
    private Integer skip;

    /**
     * 
     * @return
     *     The page_size
     */
    public String getPage_size() {
        return page_size;
    }

    /**
     * 
     * @return
     *     The FCProducts
     */
    public List<FC_Product> getFCProducts() {
        return FCProducts;
    }

    /**
     * 
     * @return
     *     The page
     */
    public String getPage() {
        return page;
    }

    /**
     * 
     * @return
     *     The count
     */
    public Integer getCount() {
        return count;
    }

    /**
     * 
     * @return
     *     The skip
     */
    public Integer getSkip() {
        return skip;
    }

    @Override
    public String toString() {
        return "FC_Search{" +
                "page_size='" + page_size + '\'' +
                ", FCProducts=" + FCProducts +
                ", page='" + page + '\'' +
                ", count=" + count +
                ", skip=" + skip +
                '}';
    }
}
