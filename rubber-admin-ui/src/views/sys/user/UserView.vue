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
            @click="handleDelete(scope.row.roleId, scope.row)"
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
        <el-table-column prop="userId" label="ID" width="55" align="center"></el-table-column>
        <el-table-column prop="loginAccount" label="登陆账户" align="center"></el-table-column>
        <el-table-column prop="userName" label="用户名称" align="center"></el-table-column>
        <el-table-column prop="email" label="邮箱" align="center"></el-table-column>
        <el-table-column prop="phone" label="电话" align="center"></el-table-column>
        <el-table-column label="头像" align="center">
          <template slot-scope="scope">
            <el-image
                class="table-td-thumb"
                :src="scope.row.thumb"
                :preview-src-list="[scope.row.avatar]"
            ></el-image>
          </template>
        </el-table-column>


        <el-table-column prop="loginIp" label="登陆IP" align="center"></el-table-column>
        <el-table-column prop="loginCount" label="登陆次数" align="center"></el-table-column>
        <el-table-column prop="loginTime" label="登陆时间" align="center"></el-table-column>

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
                @click="handleDelete(scope.row.id, scope.row)"
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
    <el-dialog :title="addEditTitle" :visible.sync="addEditVisible" :before-close='closeAddEdit' width="60%">
      <el-form ref="form" :model="form" :rules="rules" label-width="70px">

        <el-row>
          <el-col :span="10">
            <el-form-item label="账户">
              <el-input v-model="form.loginAccount"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="10">
            <el-form-item label="密码">
              <el-input type="password" v-model="form.loginPwd"></el-input>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="10">
            <el-form-item label="名称">
              <el-input v-model="form.userName"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="10">
            <el-form-item label="邮箱">
              <el-input v-model="form.email"></el-input>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="10">
            <el-form-item label="电话">
              <el-input v-model="form.phone"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="10">
            <el-form-item label="性别">
              <el-radio-group v-model="form.sex">
                <el-radio
                    v-for="dict in sexList"
                    :key="dict.sexId"
                    :label="dict.sexId"
                >{{dict.sexName}}</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="10">
            <el-form-item label="部门">
              <treeselect v-model="form.deptId" :multiple="false" :options="deptTree" :normalizer="normalizerForDept" />
            </el-form-item>
          </el-col>

          <el-col :span="10">
            <el-form-item label="状态">
              <el-radio-group v-model="form.status">
                <el-radio
                    v-for="dict in statusOptions"
                    :key="dict.dictValue"
                    :label="dict.dictValue"
                >{{dict.dictLabel}}</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-form-item label="角色">
            <treeselect v-model="form.roleId" :multiple="true" :options="roleList" :normalizer="normalizerForRole" />
          </el-form-item>
        </el-row>

        <el-row>
          <el-col :span="10">
            <el-form-item label="头像">
              <el-input v-model="form.avatar"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="closeAddEdit">取 消</el-button>
        <el-button type="primary" @click="handleAddEdit(form.userId)">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  import BaseList from '@/components/BaseTableCurd.vue';
  import sysUrl from '@/api/sys/SysUrl';
  import UserApi from '@/api/sys/UserApi';
  import global from '@/utils/Global';


  export default {
    extends: BaseList,
    data() {
      const data = BaseList.data();
      data.url = sysUrl.allUrl.sysUser;
      data.deptTree=[];
      data.roleList=[];
      data.sexList=[{
        sexId:1,
        sexName:'男'
      },{
        sexId:0,
        sexName:'女'
      }];
      return data
    },


    created() {
      this.getPageList();
      this.getDeptAndRole();
    },

    /**
     * 保存添加的数据信息
     */
    methods: {

      //打开编辑框
      openEdit(index, row){
        this.rowIndex = index;
        //重新获取数据信息
        UserApi.getUserInfo(row.userId).then(result=>{
          if (global.SUCCESS === result.code){
            const userInfos = result.data;
            this.form = userInfos.sysUser;

            if(userInfos.sysRoles != undefined && userInfos.sysRoles.length > 0){
                const sysRoles = userInfos.sysRoles;
                let sysRoleArray = [];
                for(let i in sysRoles){
                  sysRoleArray.push(sysRoles[i].roleId);
                }
                this.form.roleId = sysRoleArray;
            }
            this.radioSelection = userInfos.sysUser;

            this.addEditVisible = true;
            this.addEditType = 'edit';
            this.addEditTitle = '编辑';
          }else {
            this.$message.error(result.msg);
          }
        });

      },

      /**
       * 重写编辑之前的操作
       * @param form
       * @returns {{sysUser: *}}
       */
      preHandleSave(form){
        let sysRole = [];
        for (let i in form.roleId){
          sysRole.push({roleId:form.roleId[i]})
        }
        return {
          sysUser: form,
          sysRoles: sysRole
        };
      },

      getDeptAndRole(){
        let roleListParam =
            {
              page:1,
              size:1000,
              selectModels:[{field:'status',type:'eq',data:0}]
            }
        UserApi.getRoleList(roleListParam).then(result =>{
          if (global.SUCCESS === result.code){
              this.roleList = result.data.records;
          }
        });

        UserApi.getDeptTree().then(result =>{
          if (global.SUCCESS === result.code){
            this.deptTree = result.data.children;
          }
        })
      },

      //selecttree 自定义属性名称
      normalizerForDept(node) {
        return {
          id: node.deptId,
          label: node.deptName,
          children: node.children,
        }
      },
      normalizerForRole(node) {
        return {
          id: node.roleId,
          label: node.roleName,
          children: node.children,
        }
      },
    }
  };
</script>

<style scoped>

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
