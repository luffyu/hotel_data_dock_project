package com.rubber.project.lt.enmus;

/**
 * @author luffyu
 * Created on 2021/4/10
 */
public enum  LtBreakfastEnums {

    /**
     * 早餐的类型
     */
    NO_BF("011001","不含早餐",0),
    ONE_BF("011002","含单早",1),
    TWO_BF("011003","含双早",2),
    ONE_TWO_BF("011004","含单双早",2),
    BED_BF("011007","含床位早",1),
    three_BF("011008","含三早",3),
    four_BF("011011","含四早",4),
    five_BF("011014","含五早",5),
    six_BF("011010","含六早",6),
    eight_BF("011012","含八早",7),
    TWO_BF_DI("011015","双早+午餐/晚餐",8),
    ;
    /**
     * 字段code值
     */
    private String code;

    /**
     * label备注
     */
    private String label;

    /**
     * 早餐的数量
     */
    private int num;

    LtBreakfastEnums(String code, String label, int num) {
        this.code = code;
        this.label = label;
        this.num = num;
    }


    public String getCode() {
        return code;
    }

    public String getLabel() {
        return label;
    }

    public int getNum() {
        return num;
    }


    public static LtBreakfastEnums getByCode(String code){
        for (LtBreakfastEnums breakfastEnums:LtBreakfastEnums.values()){
            if (breakfastEnums.getCode().equals(code)){
                return breakfastEnums;
            }
        }
        return NO_BF;
    }
}
