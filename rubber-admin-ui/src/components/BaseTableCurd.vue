<script>
  import request from '@/utils/request';
  import global from '@/utils/Global';
  import sysUrl from '@/api/sys/SysUrl';
  import Treeselect from "@riophae/vue-treeselect";
  import "@riophae/vue-treeselect/dist/vue-treeselect.css";

  export default {
    components: { Treeselect },
    /**
     * ***************************************数据体***************************************************************
     * 分页查询方法
     */
    data() {
      return {
        //分页总数
        pageTotal: 0,
        //查询的字段信息
        query:{
          page:1,
          size:10,
          selectModels:[]
        },
        //表格的基本信息
        tableData: [],

        //编辑和新增是否显示
        addEditVisible:false,
        //默认是编辑类型
        addEditType:'',
        //操作标题
        addEditTitle:'',

        //表单提交的数据信息
        form:{},

        //执行的url
        url:{
          pageList:"",
          add:"",
          del:"",
          edit:"",
        },
        //当前行数
        rowIndex: -1,
        //多选数组值
        multipleSelection: [],
        //当选数据值
        radioSelection:{},
        //选中的删除的行id
        delList: [],
        // 表单校验
        rules:{
          email: [
            {
              type: "email",
              message: "'请输入正确的邮箱地址",
              trigger: ["blur", "change"]
            }
          ],
          phone: [
            {
              pattern: /^1[3|4|5|6|7|8|9][0-9]\d{8}$/,
              message: "请输入正确的手机号码",
              trigger: "blur"
            }
          ]
        },
        statusOptions:[
          {
            dictValue:0,
            dictLabel:'正常'
          },
          {
            dictValue:-1,
            dictLabel:'停用'
          }
        ]

      };
    },

    //记载页面的时候 会调用的方法
    created() {
      this.firstInit();
    },

    // 方法结合
    methods: {

      /**
       * ***************************************首次初始化***************************************************************
       * 分页查询方法
       */
      firstInit(){
        this.getPageList();
      },

      /**
       * ***************************************列表查询***************************************************************
       * 分页查询方法
       */
      getPageList() {
        request({
          url: global.rubberBasePath + this.url.pageList,
          method: 'get',
          params: {
            'json':encodeURI(JSON.stringify(this.query))
          }
        }).then(result => {
          console.info(result);
          if (global.SUCCESS === result.code){
            this.handleAfterPageList(result);
          }else {
            this.handelRequestError(result);
          }
        })
      },

      handelRequestError(result) {
        this.$message.error(result.msg);
      },
      /**
       * 处理消息返回体
       */
      handleAfterPageList(result){
        const list =  result.data;
        this.tableData = list.records;
        this.pageTotal = list.total;
        this.treeList = this.tableData;
      },

      /**
       * 搜索按钮点击事件
       */
      handleSearch() {
        this.$set(this.query, 'page', 1);
        this.getPageList();
      },

      /**
       * 分页导航 查询事件
       * @param val
       */
      handlePageSearch(val) {
        this.$set(this.query, 'page', val);
        this.getPageList();
      },


      /**
       * 新增编辑操作
       */
      handleAddEdit(id) {
        if(this.addEditType === 'add'){
          this.handleAdd();
        }else if(this.addEditType === 'edit'){
          this.handleEdit(id);
        }
      },

      /**
       * 关闭编辑操作框
       */
      closeAddEdit() {
        this.addEditVisible = false;
        this.addEditType = '';
        this.addEditTitle = '';
        this.form = {};
        this.rowIndex = -1;
      },

      /**
       *  * ***************************************编辑操作***************************************************************
       * 打开编辑操作框
       * @param index 当前行数id
       * @param row 当前行数的数据
       */
      openEdit(index, row) {
        this.rowIndex = index;
        this.form = row;
        this.radioSelection = row;

        this.addEditVisible = true;
        this.addEditType = 'edit';
        this.addEditTitle = '编辑';
        this.afterOpenAddEdit();
      },

      /**
       * 编辑选中的数据信息
       */
      openEditByRadio() {
        this.openEdit(0,this.radioSelection);
      },


      /**
       * 保存编辑的数据方法
       */
      handleEdit(id) {
        const editUrl = this.url.edit.replace("%s",id);
        request({
          url: global.rubberBasePath + editUrl,
          method: 'post',
          data: this.preHandleSave(this.form)
        }).then(result => {
          if(result.code === global.SUCCESS){
            this.$message.success(`修改成功`);
            this.closeAddEdit();
            this.getPageList();
          }else {
            this.handelRequestError(result);
          }
        })
      },



      /**
       * ***************************************新增操作***************************************************************
       * 打开新增框
       */
      openAdd() {
        this.addEditVisible = true;
        this.addEditType = 'add';
        this.addEditTitle = '新增';
        this.afterOpenAddEdit();
      },

      /**
       * 保存添加的方法
       */
      handleAdd() {
        request({
          url: global.rubberBasePath + this.url.add,
          method: 'post',
          data: this.preHandleSave(this.form)
        }).then(result => {
          if(result.code === global.SUCCESS){
            this.$message.success(`新增数据成功`);
            this.closeAddEdit();
            this.getPageList();
          }else {
            this.handelRequestError(result);
          }
        })
      },
      /**
       * 添加之前的操作
       */
      preHandleSave(form){
        return form;
      },


      /**
       * 打开编辑/新增框之后的统一操作
       */
      afterOpenAddEdit(){

      },


      /**
       * ***************************************删除操作***************************************************************
       * 删除操作
       */
      handleDelete(id, row) {
        // 二次确认删除
        this.$confirm('确定要删除吗？', '提示', {
          type: 'warning'
        }).then(() => {
          const delUrl = this.url.del.replace("%s", id);
          request({
            url: global.rubberBasePath + delUrl,
            method: 'post',
          }).then(result =>{
            if(result.code === global.SUCCESS){
              this.$message.success(`删除成功`);
              this.closeAddEdit();
              this.getPageList();
            }else {
              this.handelRequestError(result);
            }
          })
        }).catch(() => {});
      },

      /**
       * ***************************************选择操作***************************************************************
       * 选择操作
       */
      handleSelectionChange(val) {
        this.multipleSelection = val;
        this.radioSelection = val[0];
      },



      select(selection, row) {
        // 清除 所有勾选项
        this.$refs.multipleTable.clearSelection()
        // 当表格数据都没有被勾选的时候 就返回
        // 主要用于将当前勾选的表格状态清除
        if(selection.length == 0) return
        this.$refs.multipleTable.toggleRowSelection(row, true);
      },

      // 表格的选中 可以获得当前选中的数据
      radioSelectionChange(val) {
        // 将选中的数据存储起来
        this.radioSelection = val[0];
      },
      // 表格某一行的单击事件
      rowClick(row, column) {
        const selectData = this.radioSelection
        this.$refs.multipleTable.clearSelection()
        if(selectData!==undefined && selectData !== null && selectData.length === 1 ) {
          selectData.forEach(item => {
            // 判断 如果当前的一行被勾选, 再次点击的时候就会取消选中
            if (item == row) {
              this.$refs.multipleTable.toggleRowSelection(row, false);
            }
            // 不然就让当前的一行勾选
            else {
              this.$refs.multipleTable.toggleRowSelection(row, true);
            }
          })
        }
        else {
          this.$refs.multipleTable.toggleRowSelection(row, true);
        }
      },


      normalShowStatus(status){
        if (status === 0){
          return '正常';
        }else if(status < 0){
          return "停用";
        }else {
          return "-"
        }
      }
    },

    render(){

    }

  };
</script>
