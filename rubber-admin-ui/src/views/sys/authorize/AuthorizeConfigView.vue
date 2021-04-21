<template>
  <div class="container">

    <template>
      <el-tabs v-model="groupTypeInfo.defaultType" type="card" @tab-click="handleClick">
          <el-tab-pane  v-for="type in groupTypeInfo.typeInfo"
                        :name="type.key"
                        :label="type.label" ></el-tab-pane>
      </el-tabs>
    </template>

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
      <el-table-column prop="groupId" label="ID" width="55" align="center"></el-table-column>
      <el-table-column prop="groupKey" label="KEY值"></el-table-column>
      <el-table-column prop="groupName" label="名称"></el-table-column>
      <el-table-column prop="groupType" label="类型"></el-table-column>
      <el-table-column prop="groupMember" label="关联值" ></el-table-column>
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


    <!-- 编辑弹出框 -->
    <el-dialog :title="addEditTitle"  :visible.sync="addEditVisible" :before-close='closeAddEdit' width="40%">
      <el-form ref="form" :model="form" label-width="70px">
        <el-form-item label="KEY值">
          <el-input v-model="form.groupKey"></el-input>
        </el-form-item>
        <el-form-item label="名称">
          <el-input v-model="form.groupName"></el-input>
        </el-form-item>
        <el-form-item label="类型">
          <el-radio-group v-model="form.groupType">
            <el-radio
                v-for="dict in groupTypeOption"
                :key="dict.type"
                :label="dict.type"
            >{{dict.label}}</el-radio>
          </el-radio-group>
        </el-form-item>

        <el-form-item label="关联值">
          <el-input v-model="form.groupMember"></el-input>
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
      </el-form>
      <span slot="footer" class="dialog-footer">
          <el-button @click="closeAddEdit">取 消</el-button>
          <el-button type="primary" @click="handleAddEdit(form.groupId)">确 定</el-button>
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
      data.url = sysUrl.allUrl.sysAuthorizeConfig;
      data.groupTypeInfo = {
          defaultType:"",
          typeInfo:[]
      };
      data.groupTypeOption = [
        {
          type:'option',
          label:'基础操作'
        },
        {
          type:'apply',
          label:'业务类型'
        }
      ]
      return data
    },

    methods:{
      firstInit() {
        request({
          url: global.rubberBasePath + this.url.viewInfo,
          method: 'get',
        }).then(result => {
          if (global.SUCCESS === result.code){
            const typeMap = result.data.type;
            const typeInfo = [];
            let defaultType = "";
            for (let i in typeMap){
              if (defaultType === ""){
                defaultType = i;
              }
              const type = {};
              type["key"] = i;
              type["label"] = typeMap[i];
              typeInfo.push(type)
            }
            this.groupTypeInfo.typeInfo = typeInfo;
            this.groupTypeInfo.defaultType = defaultType;
            this.getSelectModels(defaultType);
            this.getPageList();
          }else {
            this.handelRequestError(result);
          }
        })
      },

      handleClick(tab, event) {
        this.getSelectModels(tab.name);
        this.getPageList();
      },

      getSelectModels(groupType){
        this.query.selectModels = [{
          "field":"groupType",
          "type":"EQ",
          "data":groupType
        }];
      }

      ,
      normalShowStatus(status){
        if (status === 0){
          return '正常';
        }if (status === 1){
          return '基础值';
        }else if(status < 0){
          return "停用";
        }else {
          return "-"
        }
      }
    }
  };

</script>


<style>
  .el-dropdown-link {
    cursor: pointer;
    color: rgba(76, 76, 76, 0.8);
    font-size: 10px;
  }
</style>

