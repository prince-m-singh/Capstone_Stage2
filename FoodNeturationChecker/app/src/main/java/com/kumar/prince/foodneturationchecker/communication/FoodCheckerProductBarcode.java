package com.kumar.prince.foodneturationchecker.communication;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.kumar.prince.foodneturationchecker.data.model.FoodCheckerProduct;


/**
 * Created by prince on 29/8/17.
 *
 */

public class FC_ProductBarcode {

    @SerializedName("status_verbose")
    @Expose
    private String status_verbose;
    @SerializedName("product")
    @Expose
    private FoodCheckerProduct FCProduct = new FoodCheckerProduct();
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
    public FoodCheckerProduct getFCProduct() {
        return FCProduct;
    }

    /**
     *
     * @return
     *     The products
     */
    public FoodCheckerProduct getProducts() {
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

    @Override
    public String toString() {
        return "FC_ProductBarcode{" +
                "status_verbose='" + status_verbose + '\'' +
                ", FCProduct=" + FCProduct +
                ", status=" + status +
                ", code='" + code + '\'' +
                '}';
    }
}
