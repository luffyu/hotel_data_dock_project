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

      </div>


      <el-table
          :data="tableData"
          border
          class="table"
          ref="multipleTable"
          header-cell-class-name="table-header"
          @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="55" align="center"></el-table-column>
        <el-table-column prop="roleId" label="ID" width="55" align="center"></el-table-column>
        <el-table-column prop="roleName" label="角色"></el-table-column>
        <el-table-column prop="seq" label="排序"></el-table-column>
        <el-table-column prop="createBy" label="创建人"></el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="150" ></el-table-column>
        <el-table-column prop="updateBy" label="更新人"  ></el-table-column>
        <el-table-column prop="updateTime" label="更新时间" width="150"></el-table-column>

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
                class="button-text-red"
                @click="handleDelete(scope.row.roleId, scope.row)"
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


    <!-- 编辑弹出框 -->
    <el-dialog :title="addEditTitle"  :visible.sync="addEditVisible" :before-close='closeAddEdit' width="40%">
      <el-form ref="form" :model="form" label-width="70px">
        <el-form-item label="角色名称">
          <el-input v-model="form.roleName"></el-input>
        </el-form-item>
        <el-form-item label="排序">
          <el-input-number v-model="form.seq" controls-position="right" :min="0" />
        </el-form-item>

        <el-form-item label="状态">
          <el-radio-group v-model="form.status">
            <el-radio
                v-for="dict in statusOptions"
                :key="dict.dictValue"
                :label="dict.dictValue"
            >{{dict.dictLabel}}</el-radio>
          </el-radio-group>
        </el-form-item>

        <el-form-item label="备注">
          <el-input v-model="form.remark"></el-input>
        </el-form-item>
        <el-form-item label="权限">
          <el-tree
              :data="menuOptionTree"
              show-checkbox
              node-key="key"
              width="100px"
              ref="menuOptionTree"
              :props="{children: 'children',  label: 'label'}">
          </el-tree>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
          <el-button @click="closeAddEdit">取 消</el-button>
          <el-button type="primary" @click="handleAddEdit(form.roleId)">确 定</el-button>
      </span>
    </el-dialog>

  </div>
</template>

<script>
  import BaseList from '@/components/BaseTableCurd.vue';
  import sysUrl from '@/api/sys/SysUrl';
  import request from '@/utils/request';
  import global from '@/utils/Global';


  export default {
    extends: BaseList,
    data() {
      const data = BaseList.data();
      data.url = sysUrl.allUrl.sysRole;
      data.statusOptions = [
        {
          dictValue:0,
          dictLabel:'正常'
        },
        {
          dictValue:-1,
          dictLabel:'异常'
        }
      ];
      data.menuOptionTree = [];
      return data
    },
    methods:{


      //保存之前的操作
      handleAddEdit(id) {
        const roleMenuOptions = this.$refs.menuOptionTree.getCheckedKeys();
        const  halfCheckedKeys = this.$refs.menuOptionTree.getHalfCheckedKeys();
        if (roleMenuOptions !== undefined && roleMenuOptions !== null){
          if (halfCheckedKeys != undefined){
              for (const data in halfCheckedKeys){
                if (data !== "0"){
                  roleMenuOptions.push(data);
                }
              }
          }
          this.form.roleMenuOptions = roleMenuOptions;
        }
        if(this.addEditType === 'add'){
          this.handleAdd();
        }else if(this.addEditType === 'edit'){
          this.handleEdit(id);
        }
      },


      openEdit(index, row) {

        const infoUrl = this.url.info.replace("%s",row.roleId);
        request({
          url: global.rubberBasePath + infoUrl,
          method: 'get',
          params: {
            'id':encodeURI(JSON.stringify(this.query))
          }
        }).then(result => {
          if (result.code === global.SUCCESS) {
            this.rowIndex = index;
            this.form = result.data;
            this.radioSelection = row;
            this.addEditVisible = true;
            this.addEditType = 'edit';
            this.addEditTitle = '编辑';
            this.afterOpenAddEdit();
          } else {
            this.handelRequestError(result);
          }
        });
      },


      afterOpenAddEdit(){
        request({
          url: global.rubberBasePath + sysUrl.allUrl.sysMenu.optionTree,
          method: 'get',
        }).then(result => {
          if(result.code === global.SUCCESS){
            this.menuOptionTree = result.data;
            this.$refs.menuOptionTree.setCheckedKeys(this.form.roleMenuOptions);
          }else {
            this.handelRequestError(result);
          }
        })
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
