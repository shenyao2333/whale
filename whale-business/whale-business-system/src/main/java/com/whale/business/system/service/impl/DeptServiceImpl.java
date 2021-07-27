package com.whale.business.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.whale.business.system.domain.Area;
import com.whale.business.system.domain.Dept;
import com.whale.business.system.mapper.DeptMapper;
import com.whale.business.system.service.DeptService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


@Service
public class DeptServiceImpl extends ServiceImpl<DeptMapper, Dept> implements DeptService {

    /**
     * 构建树
     *
     * @param list     分类表
     * @param parentId 传入的父节点ID
     * @return String
     */
    @Override
    public List<Dept> buildTree(List<Dept> list, int parentId){
        List<Dept> deptList = new ArrayList<Dept>();
        for (Iterator<Dept> iterator = list.iterator(); iterator.hasNext(); ) {
            Dept t = (Dept) iterator.next();
            if (t.getParentId().intValue() == parentId) {
                recursion(list, t);
                deptList.add(t);
            }
        }
        return deptList;
    }

    /**
     * 递归列表
     *
     * @param list
     * @param t
     */
    private void recursion(List<Dept> list, Dept t) {
        // 得到子节点列表
        List<Dept> childList = getChildList(list, t);
        t.setChildren(childList);
        for (Dept tChild : childList) {
            if (hasChild(list, tChild)) {
                // 判断是否有子节点
                Iterator<Dept> it = childList.iterator();
                while (it.hasNext()) {
                    Dept n = (Dept) it.next();
                    recursion(list, n);
                }
            }
        }
    }

    /**
     * 得到子节点列表
     */
    private List<Dept> getChildList(List<Dept> list, Dept t) {
        List<Dept> tlist = new ArrayList<Dept>();
        Iterator<Dept> it = list.iterator();
        while (it.hasNext()) {
            Dept n = (Dept) it.next();
            if (n.getParentId().intValue() == t.getDeptId().intValue()) {
                tlist.add(n);
            }
        }
        return tlist;
    }

    /**
     * 判断是否有子节点
     */
    private boolean hasChild(List<Dept> list, Dept t) {
        return getChildList(list, t).size() > 0 ? true : false;
    }

    /**
     * 根据部门ID查询信息
     *
     * @param deptId 部门ID
     * @return 部门信息
     */
    @Override
    public Dept selectDeptById(Integer deptId) {
        return baseMapper.selectDeptById(deptId);
    }


    /**
     * 根据区域ID查询机构
     *
     * @param area 区域对象
     * @return 机构列表
     */
    @Override
    public List<Map<String, Object>> areaDeptTreeData(Area area) {
        Integer areaId = area.getId();
        List<Map<String, Object>> trees = new ArrayList<Map<String, Object>>();
        List<Dept> deptList = baseMapper.selectList(new QueryWrapper<Dept>().orderByAsc("sort"));
        /*if (areaId != null) {
            List<String> roleDeptList = baseMapper.selectAreaDeptTree(areaId);
            trees = getTrees(deptList, true, roleDeptList);
        } else {
            trees = getTrees(deptList, false, null);
        }*/
        return trees;
    }
}
