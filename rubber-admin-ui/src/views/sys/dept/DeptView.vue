<template>
  <div>
    <el-form :inline="true" class="container-head ">
      <div class="handle-box">
        <el-select v-model="query.address" placeholder="地址" class="handle-select mr10">
          <el-option key="1" label="广东省" value="广东省"></el-option>
          <el-option key="2" label="湖南省" value="湖南省"></el-option>
        </el-select>
        <el-input v-model="query.name" placeholder="用户名" class="handle-input mr10"></el-input>
        <el-button type="primary" icon="el-icon-search" @click="handleSearch">搜索</el-button>
      </div>
    </el-form>


    <div class="container">
      <div class="container-head ">
        <el-button
            type="button"
            icon="el-icon-lx-add"
            class="el-button-add"
            @click="openAdd"
        >新增</el-button>

        <el-button
            type="button"
            icon="el-icon-lx-edit"
            class="el-button-edit"
            @click="openEditByRadio"
        >修改</el-button>

        <el-button
            type="button"
            icon="el-icon-lx-delete"
            class="el-button-delete"
            @click="openAdd"
        >删除</el-button>

        <el-button
            type="button"
            icon="el-icon-lx-exchange"
            class="el-button-fold"
            @click="collapseAll()"
        >展开/折叠</el-button>
      </div>

      <el-table
          :data="tableData"
          border
          class="table"
          row-key="deptId"
          ref="multipleTable"
          highlight-current-row
          header-cell-class-name="table-header"
          @select="select"
          @row-click="rowClick"
          @selection-change="radioSelectionChange"
          :tree-props="{children: 'children', hasChildren: 'hasChildren'}"
      >
        <el-table-column type="selection" ref="selectKey" width="55" align="center"></el-table-column>
        <el-table-column prop="deptName" label="部门名称"></el-table-column>
        <el-table-column prop="leader" label="负责人"></el-table-column>
        <el-table-column prop="phone" label="电话"></el-table-column>
        <el-table-column prop="email" label="邮件"></el-table-column>
        <el-table-column label="状态" align="center">
          <template slot-scope="scope">
            <el-tag :type=" scope.row.status == '0' ? 'success':'danger' ">
              <span >{{ normalShowStatus(scope.row.status) }}</span>
            </el-tag>
          </template>
        </el-table-column>

        <el-table-column label="操作" width="180" align="center">
          <template slot-scope="scope">
            <el-button
                type="text"
                icon="el-icon-edit"
                @click="openEdit(scope.$index, scope.row)"
            >编辑</el-button>
            <el-button
                type="text"
                icon="el-icon-delete"
                class="red"
                @click="handleDelete(scope.row.deptId, scope.row)"
            >删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <div class="pagination">
        <el-pagination
            background
            layout="total, prev, pager, next"
            :current-page="query.page"
            :page-size="query.size"
            :total="pageTotal"
            @current-change="handlePageSearch"
        ></el-pagination>
      </div>
    </div>


    <!-- 添加或修改部门对话框 -->
    <el-dialog :title="addEditTitle" :visible.sync="addEditVisible" :before-close='closeAddEdit' width="40%">
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">

          <el-form-item label="上级部门" prop="parentId">
            <treeselect v-model="form.parentId" :multiple="false" :options="selectDeptTree" :normalizer="normalizer" />
          </el-form-item>

          <el-form-item label="部门名称" prop="deptName">
            <el-input v-model="form.deptName" placeholder="请输入部门名称" />
          </el-form-item>

          <el-form-item label="显示排序" prop="seq">
            <el-input-number v-model="form.seq" controls-position="right" :min="0" />
          </el-form-item>

          <el-form-item label="负责人" prop="leader">
            <el-input v-model="form.leader" placeholder="请输入负责人" maxlength="20" />
          </el-form-item>

          <el-form-item label="联系电话" prop="phone">
            <el-input v-model="form.phone" placeholder="请输入联系电话" maxlength="11" />
          </el-form-item>

          <el-form-item label="邮箱" prop="email">
            <el-input v-model="form.email" placeholder="请输入邮箱" maxlength="50" />
          </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="closeAddEdit">取 消</el-button>
        <el-button type="primary" @click="handleAddEdit(form.deptId)">确 定</el-button>
      </div>
    </el-dialog>

  </div>
</template>

<script>
  import BaseList from '@/components/BaseTableCurd.vue';
  import sysUrl from '@/api/sys/SysUrl';

  export default {
    extends: BaseList,
    data() {
      const data = BaseList.data();
      data.url = sysUrl.allUrl.sysDept;
      data.selectDeptTree = [];
      return data
    },
    methods: {
      handleAfterPageList(result) {
        this.tableData = result.data.children;
        const originSelectDeptTree = [];
        originSelectDeptTree.push(result.data);
        this.selectDeptTree = originSelectDeptTree;
      },

      //selecttree 自定义属性名称
      normalizer(node) {
        return {
          id: node.deptId,
          label: node.deptName,
          children: node.children,
        }
      },
    }
  };
</script>

<style scoped>
  .handle-box {
  }

  .handle-select {
    width: 120px;
  }

  .handle-input {
    width: 300px;
    display: inline-block;
  }
  .table {
    width: 100%;
    font-size: 14px;
  }
  .red {
    color: #ff0000;
  }
  .mr10 {
    margin-right: 10px;
  }
  .table-td-thumb {
    display: block;
    margin: auto;
    width: 40px;
    height: 40px;
  }
</style>
