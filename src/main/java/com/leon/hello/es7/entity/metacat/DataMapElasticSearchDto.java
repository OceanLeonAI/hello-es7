package com.leon.hello.es7.entity.metacat;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.io.Serializable;

/**
 * Audit information.
 * 数据地图相关自定义实体
 */
@SuppressWarnings("unused")
@Data
@EqualsAndHashCode(callSuper = false)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DataMapElasticSearchDto implements Serializable {

    private static final long serialVersionUID = 239522595961773257L;

    /**
     * 主键
     */
    @ApiModelProperty("主键id")
    private String id;

    /**
     * 元数据表名称
     */
    @ApiModelProperty(value = "元数据表名称")
    private String tableName;
    /**
     * 元数据表中文名称
     */
    @ApiModelProperty(value = "元数据表中文名称")
    private String tableCnName;
//
//    /**
//     * 主题域主键
//     */
//    @ApiModelProperty(value = "主题域主键")
//    private Long subjectFieldId;
//    /**
//     * 主题域名称
//     */
//    @ApiModelProperty(value = "主题域名称")
//    private String subjectFieldName;
//    /**
//     * 数据分层主键
//     */
//    @ApiModelProperty(value = "数据分层主键")
//    private Long dataHierarchyId;
//    /**
//     * 数据分层名称
//     */
//    @ApiModelProperty(value = "数据分层名称")
//    private String dataHierarchyName;
//
//    @ApiModelProperty(value = "数据源ID")
//    private Long dataSourceId;
//
//    @ApiModelProperty(value = "数据源名称")
//    private String dataSourceName;
//
//    @ApiModelProperty(value = "责任人")
//    private String maintainer;
//
//    /**
//     * 描述
//     */
//    @ApiModelProperty(value = "描述")
//    private String description;
//
//    /**
//     * 数据类型，0：原生表；1：数仓表；2：元数据表
//     */
//    @ApiModelProperty(value = "数据类型，0：原生表；1：数仓表；2：元数据表")
//    private Integer isDataVisible = 2;
//
//    /**
//     * 创建人id
//     */
//    @ApiModelProperty(value = "创建人id")
//    private long createUser;
//
//    /**
//     * 创建人姓名
//     */
//    @ApiModelProperty(value = "创建人姓名")
//    private String realName;
//
//    /**
//     * 创建时间
//     */
//    // @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
//    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
//    @ApiModelProperty(value = "创建时间")
//    private Date createTime;

    /**
     * 创建时间日期字符串
     */
    @ApiModelProperty(value = "创建时间字符串类型")
    private String createTimeString;
}
