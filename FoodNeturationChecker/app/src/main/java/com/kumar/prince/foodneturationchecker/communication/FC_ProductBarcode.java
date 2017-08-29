package com.kumar.prince.foodneturationchecker.communication;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.kumar.prince.foodneturationchecker.data.model.FC_Product;


/**
 * Created by prince on 29/8/17.
 *
 */

public class FC_ProductBarcode {

    @SerializedName("status_verbose")
    @Expose
    private String status_verbose;
    @SerializedName("FCProduct")
    @Expose
    private FC_Product FCProduct = new FC_Product();
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("code")
    @Expose
    private String code;

    /**
     * 
     * @return
     *     The status_verbose
     */
    public String getStatus_verbose() {
        return status_verbose;
    }

    /**
     *
     * @return
     *     The FCProduct
     */
    public FC_Product getFCProduct() {
        return FCProduct;
    }

    /**
     *
     * @return
     *     The products
     */
    public FC_Product getProducts() {
        return FCProduct;
    }

    /**
     * 
     * @return
     *     The status
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * 
     * @return
     *     The code
     */
    public String getCode() {
        return code;
    }

}
