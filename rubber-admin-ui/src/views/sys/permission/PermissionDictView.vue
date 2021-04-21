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
        <el-table-column prop="id" label="ID" width="55" align="center"></el-table-column>
        <el-table-column prop="dictKey" label="字典Key"></el-table-column>
        <el-table-column prop="dictName" label="字典名称"></el-table-column>
        <el-table-column prop="dictValue" label="字典值" width="200"></el-table-column>
        <el-table-column prop="dictType" label="字典类型"></el-table-column>
        <el-table-column prop="updateBy" label="修改人"></el-table-column>
        <el-table-column prop="updateTime" label="修改时间"></el-table-column>

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
        <el-form-item label="字典Key" >
          <el-input v-model="form.dictKey"></el-input>
        </el-form-item>
        <el-form-item label="字典名称">
          <el-input v-model="form.dictName"></el-input>
        </el-form-item>
        <el-form-item label="字典值" >
          <el-input v-model="form.dictValue" ></el-input>
        </el-form-item>

        <el-form-item label="字典类型">
          <el-input v-model="form.dictType" ></el-input>
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
          <el-button type="primary" @click="handleAddEdit(form.id)">确 定</el-button>
      </span>
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
      data.url = sysUrl.allUrl.sysPermissionDict;
      return data
    },
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
