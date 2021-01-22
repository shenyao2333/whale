package com.whale.generator.netty.common.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "friend")
public class Friend {
    private static final long serialVersionUID = 1L;
    @TableId(value = "id", type = IdType.INPUT)
    private Integer id;

    /**
     * 用户id
     */
    @TableField(value = "user_id")
    private Integer userId;

    /**
     * 好友id
     */
    @TableField(value = "friend_user_id")
    private Integer friendUserId;

    /**
     * 0-正常，1-删除
     */
    @TableField(value = "del")
    private String del;

    /**
     * 分组id
     */
    @TableField(value = "group_id")
    private Integer groupId;

    /**
     * 更新时间
     */
    @TableField(value = "updated")
    private Date updated;

    /**
     * 创建时间
     */
    @TableField(value = "created")
    private Date created;

    /**
     * 备注
     */
    @TableField(value = "remark")
    private String remark;

    public static final String COL_ID = "id";

    public static final String COL_USER_ID = "user_id";

    public static final String COL_FRIEND_USER_ID = "friend_user_id";

    public static final String COL_DEL = "del";

    public static final String COL_GROUP_ID = "group_id";

    public static final String COL_UPDATED = "updated";

    public static final String COL_CREATED = "created";

    public static final String COL_REMARK = "remark";
}