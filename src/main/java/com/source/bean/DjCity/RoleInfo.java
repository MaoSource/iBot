package com.source.bean.DjCity;

import lombok.Data;
import lombok.ToString;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Source
 * @date 2022/11/14/16:54
 */
@Data
@ToString
public class RoleInfo {

    public RoleInfo() {
        this.setIActionId(3);
        this.setIGoodsId(3655);
        this.setSBizCode("cf");
    }

    /**
     * actId 默认：3
     */
    private int iActionId;

    /**
     *code/propId cf改名卡的是：3655
     */
    private int iGoodsId;

    /**
     * bizCode/busId cf默认为：cf
     */
    private String sBizCode;

    /**
     * 角色信息
     * 手游：serviceID != 0 就是 serviceID
     * 端游：areaID != 0 就是 areaID
     *
     */
    private String partition;

    /**
     * serviceID != "" 就是 serviceID
     * 是空就是areaID
     */
    private String iZoneId;

    /**
     * systemID <0 那就是0 否则就是：systemID
     */
    private int platid;

    /**
     * serviceName 游戏大区
     */
    private String sZoneDesc;

    /**
     * 游戏账号
     * bizCode == 'lol' && accountId != ""
     * roleCode
     */
    private String sRoleId;

    /**
     * 游戏名称
     * roleName
     */
    private String sRoleName;

    /**
     * 许愿描述
     */
    private String sGetterDream;
}
