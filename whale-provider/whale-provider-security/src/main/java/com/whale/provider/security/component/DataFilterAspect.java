package com.whale.provider.security.component;

import cn.hutool.core.util.StrUtil;
import com.whale.provider.basices.web.GrabException;
import com.whale.provider.security.annotation.DataFilter;
import com.whale.provider.security.domain.WhaleUser;
import com.whale.provider.security.utils.SecurityUtil;
import lombok.AllArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import com.whale.provider.basices.domain.BaseEntity;
import java.util.List;



/**
 * @Author: sy
 * @Date: Created by 2021/6/8 10:21
 * @description: 数据过滤，切面处理类
 */
@Aspect
@Component
@AllArgsConstructor
public class DataFilterAspect {

    private final JdbcTemplate jdbcTemplate;
    /**
     * 全部数据权限
     */
    public static final String DATA_SCOPE_ALL = "1";

    /**
     * 自定数据权限
     */
    public static final String DATA_SCOPE_CUSTOM = "2";

    /**
     * 部门数据权限
     */
    public static final String DATA_SCOPE_DEPT = "3";

    /**
     * 部门及以下数据权限
     */
    public static final String DATA_SCOPE_DEPT_AND_CHILD = "4";

    /**
     * 仅本人数据权限
     */
    public static final String DATA_SCOPE_SELF = "5";


    public static String ROLE_DATASCOPE = "select data_scope dataScope from sys_role where id = ?";


    @Pointcut("@annotation(com.whale.provider.security.annotation.DataFilter)")
    public void dataFilterCut() {

    }

    @Before("dataFilterCut()")
    public void dataFilter(JoinPoint point) throws Throwable {
        Object params = null;
        if (point.getArgs().length > 1) {
            params = point.getArgs()[1];
        } else {
            params = point.getArgs()[0];
        }

        if ( params instanceof BaseEntity) {
            BaseEntity baseEntity = (BaseEntity) params;
            baseEntity.setSqlFilter(getSQLFilter(point));
            return;
        }
        throw new GrabException("数据权限过滤错误，请联系管理员");
    }

    /**
     * 获取数据过滤的SQL
     */
    private String getSQLFilter(JoinPoint point) {
        MethodSignature signature = (MethodSignature) point.getSignature();
        DataFilter dataFilter = signature.getMethod().getAnnotation(DataFilter.class);
        //获取表的别名
        String tableAlias = dataFilter.tableAlias();
        if (StrUtil.isNotBlank(tableAlias)) {
            tableAlias += ".";
        }
        StringBuilder sqlFilter = new StringBuilder();
        WhaleUser user = SecurityUtil.getUser();
        List<Integer> roles = SecurityUtil.getRoleIdList();
        if (roles != null && roles.size() > 0) {
            sqlFilter.append(" (");
            for (int i = 0; i < roles.size(); i++) {
                String dataScope = jdbcTemplate.queryForObject(ROLE_DATASCOPE, String.class, roles.get(i));
                if (i != 0) {
                    sqlFilter.append(" OR");
                }
                if (DATA_SCOPE_ALL.equals(dataScope)) {
                    sqlFilter = new StringBuilder();
                    break;
                } else if (DATA_SCOPE_CUSTOM.equals(dataScope)) {
                    sqlFilter.append(StrUtil.format(" {}dept_id IN ( SELECT dept_id FROM sys_role_dept WHERE role_id = {} ) ", tableAlias, roles.get(i)));
                } else if (DATA_SCOPE_DEPT.equals(dataScope)) {
                    sqlFilter.append(StrUtil.format(" {}dept_id = {} ", tableAlias, user.getDeptId()));
                } else if (DATA_SCOPE_DEPT_AND_CHILD.equals(dataScope)) {
                    sqlFilter.append(StrUtil.format(
                            " {}dept_id IN ( SELECT dept_id FROM sys_dept WHERE dept_id = {} or find_in_set( {} , ancestors ) )", tableAlias, user.getDeptId(), user.getDeptId()));
                } else if (DATA_SCOPE_SELF.equals(dataScope)) {
                    sqlFilter.append(StrUtil.format(" create_by = '{}' ", user.getUsername()));
                }
            }
            if (StrUtil.isNotBlank(sqlFilter)) {
                sqlFilter.append(")");
            }
        }
        return sqlFilter.toString();
    }


}
