<template>
  <div>
    <el-form :inline="true" class="container-head ">
          <div class="handle-box">
            <el-input v-model="query.menuId" placeholder="菜单Id" class="handle-input mr10"></el-input>
            <el-input v-model="query.menuName" placeholder="菜单名称" class="handle-input mr10"></el-input>
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
          row-key="menuId"
          ref="multipleTable"
          header-cell-class-name="table-header"
          @select="select"
          @row-click="rowClick"
          @selection-change="radioSelectionChange"
          :tree-props="{children: 'children', hasChildren: 'hasChildren'}"
      >
        <el-table-column type="selection" width="55" align="center"></el-table-column>
        <el-table-column label="菜单名称" width="150">
          <template slot-scope="scope">
            <i :class="scope.row.icon"></i>
            {{scope.row.menuName}}
          </template>
        </el-table-column>
        <el-table-column prop="seq" label="排序" width="100" align="center"></el-table-column>
        <el-table-column prop="url" label="请求url" width="400"></el-table-column>

        <el-table-column prop="menuType" label="类型" align="center">
          <template slot-scope="scope">
            <el-tag :type=" scope.row.menuType == 'M' ? 'success':'danger' ">
              <span v-if = "scope.row.menuType == 'M' " >目录</span>
              <span v-if = "scope.row.menuType == 'C' " >菜单</span>
              <span v-if = "scope.row.menuType == 'B' " >按钮</span>
            </el-tag>
          </template>
        </el-table-column>

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
                class="blue"
                @click="openEdit(scope.$index, scope.row)"
            >编辑</el-button>

            <el-button
                type="text"
                icon="el-icon-delete"
                class="red"
                @click="handleDelete(scope.row.menuId, scope.row)"
            >删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>


    <!-- 编辑弹出框 -->
    <el-dialog :title="addEditTitle"  :visible.sync="addEditVisible" :before-close='closeAddEdit' width="70%" >
      <el-form ref="form" :model="form" label-width="100px">
        <el-form-item label="父菜单" prop="parentId" >
          <treeselect v-model="form.parentId" :multiple="false" :options="selectRootMenus" :normalizer="normalizer" />
        </el-form-item>

        <el-form-item label="菜单类型">
          <el-radio-group v-model="form.menuType">
            <el-radio
                v-for="dict in menuTypes"
                :key="dict.dictValue"
                :label="dict.dictValue"
            >{{dict.dictLabel}}</el-radio>
          </el-radio-group>
        </el-form-item>

        <el-form-item label="名称">
          <el-input v-model="form.menuName"></el-input>
        </el-form-item>

        <el-form-item label="请求" v-if="form.menuType === 'C' ">
          <el-input v-model="form.url" value="#"></el-input>
        </el-form-item>


        <el-form-item label="排序">
          <el-input-number v-model="form.seq" controls-position="right" :min="0" />
        </el-form-item>

        <el-form-item label="菜单图标">
          <el-input v-model="form.icon"  />
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

        <el-form-item label="所需权限" v-if="false && form.menuType === 'C'">
          <el-tree
              :data="authOptionGroup"
              show-checkbox
              node-key="key"
              width="100px"
              ref="authOptionGroup"
              :props="{children: 'children',  label: 'label'}">
            <span class="custom-tree-node" slot-scope="{ node, data }">
              <span>{{ node.label }}</span>
              <span v-if="data.requestUrl != undefined && data.requestUrl != null && data.requestUrl.length > 0">
                <el-dropdown  trigger="click">
                  <span class="el-dropdown-link onchange-url-span" >
                    点击查看url <i class="el-icon-arrow-down el-icon--right"></i>
                  </span>
                  <el-dropdown-menu slot="dropdown">
                    <div v-for="(item,index) in data.requestUrl">
                      <el-dropdown-item> {{item}}</el-dropdown-item>
                    </div>
                  </el-dropdown-menu>
                </el-dropdown>
              </span>
            </span>
          </el-tree>

        </el-form-item>

      </el-form>

      <span slot="footer" class="dialog-footer">
        <el-button @click="closeAddEdit">取 消</el-button>
        <el-button type="primary" @click="handleAddEdit(form.menuId)">确 定</el-button>
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
      data.url = sysUrl.allUrl.sysMenu;
      data.menuTypes=[
        {
          dictValue:"M",
          dictLabel:'目录'
        },
        {
          dictValue:"C",
          dictLabel:'菜单'
        }
      ];
      data.authOptionGroup = [];
      data.selectRootMenus = [];
      return data
    },

    methods:{
      handleAfterPageList(result){
        this.tableData = result.data.children;
        const canSelectRootMenus = [];
        canSelectRootMenus.push(result.data);
        this.selectRootMenus = canSelectRootMenus;
        this.pageTotal = this.tableData === undefined ? 0 : this.tableData.length;
      },

      normalizer(node) {
        return {
          id: node.menuId,
          label: node.menuName,
          children: node.children,
        }
      },

      afterOpenAddEdit(){
        request({
          url: global.rubberBasePath + sysUrl.allUrl.sysAuthorize.optionTree,
          method: 'get',
        }).then(result => {
          if(result.code === global.SUCCESS){
            this.authOptionGroup = result.data;
            if (this.form.menuOptionGroup !== undefined && this.form.menuOptionGroup !==null){
              if (this.$refs.authOptionGroup != undefined){
                this.$refs.authOptionGroup.setCheckedKeys(this.form.menuOptionGroup);
              }
            }
          }else {
            this.handelRequestError(result);
          }
        })
      },

      //保存之前的操作
      handleAddEdit(id) {
        if (this.form.menuType === 'C'){
          if (this.$refs.authOptionGroup != undefined){
            const groupAuthMenu = this.$refs.authOptionGroup.getCheckedKeys();
            if (groupAuthMenu !== undefined && groupAuthMenu !== null){
              this.form.menuOptionGroup = groupAuthMenu;
            }
          }
        }
        if(this.addEditType === 'add'){
          this.handleAdd();
        }else if(this.addEditType === 'edit'){
          this.handleEdit(id);
        }
      },

      /**
       * 打开编辑页面
       * @param index
       * @param row
       */
      openEdit(index, row) {
        const infoUrl = this.url.info.replace("%s",row.menuId);
        request({
          url: global.rubberBasePath + infoUrl,
          method: 'get',
          params: {
            'id':encodeURI(JSON.stringify(this.query))
          }
        }).then(result => {
          if(result.code === global.SUCCESS){
            this.rowIndex = index;
            this.form = result.data;
            this.radioSelection = row;

            this.addEditVisible = true;
            this.addEditType = 'edit';
            this.addEditTitle = '编辑';
            this.afterOpenAddEdit();

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
    width: 150px;
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

  .red {
    color: #ff3f19;
  }

  .green {
    color: rgba(19, 192, 1, 0.85);
  }

  .origin {
    color: #ffb527;
  }
  .blue{
    color: #2442ff;
  }

  .onchange-url-span{
    font-size: 10px;
    color: #008000;
  }

  .custom-tree-node {
    flex: 1;
    display: flex;
    align-items: center;
    justify-content: space-between;
    font-size: 14px;
    padding-right: 8px;
  }

</style>
