package org.my.excel;

import lombok.Data;

import java.util.List;

/**
 * @author 李杰  210242
 * @description:
 * @date 2021/8/16 13:50
 */
@Data
public class Asset {
    private String routing;
    private String area;
    private String assetCode;
    private String assetName;
    private String checkItem;
    private List<String> checkItemList;

}
