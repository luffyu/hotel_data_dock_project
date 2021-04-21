<template>
    <div>
        <div class="container">
            <el-form :inline="true" style="padding: 10px" :model="selectModels" class="demo-form-inline">
                <el-form-item label="携程酒店ID">
                    <el-input v-model="selectModels.xcHotelId" placeholder="携程酒店ID"></el-input>
                </el-form-item>
                <el-form-item label="携程酒店名称">
                    <el-input v-model="selectModels.xcHotelName" placeholder="携程酒店名称"></el-input>
                </el-form-item>
                <el-form-item label="龙腾酒店ID">
                    <el-input v-model="selectModels.ltHotelId" placeholder="龙腾酒店ID"></el-input>
                </el-form-item>
                <el-form-item label="龙腾酒店名称">
                    <el-input v-model="selectModels.ltHotelName" placeholder="龙腾酒店名称"></el-input>
                </el-form-item>
                <el-form-item>
                    <el-button type="primary" icon="el-icon-search" @click="handlerSearch">查询</el-button>
                </el-form-item>
            </el-form>
        </div>

        <div class="container" style="margin-top: 10px;">

            <el-row style="padding: 10px;text-align: right">
                <el-button class="titleH" icon="el-icon-plus"  type="primary" @click="handelEdit()" >新增酒店</el-button>
            </el-row>


            <el-table
                    :data="tableData"
                    style="width: 100%"
                    row-key="hotelContrastId"
                    border>

                <el-table-column
                        prop="hotelContrastId"
                        label="同步ID"
                        width="60">
                </el-table-column>
                <el-table-column
                        prop="xcHotelId"
                        label="携程的酒店名称"
                        min-width="200">
                    <template slot-scope="scope">
                        {{ scope.row.xcHotelName}}（{{ scope.row.xcHotelId }}）
                    </template>
                </el-table-column>

                <el-table-column
                        prop="xcHotelId"
                        label="龙腾的酒店名称"
                        min-width="200">
                    <template slot-scope="scope">
                        {{ scope.row.ltHotelName}}（{{ scope.row.ltHotelId }}）
                    </template>
                </el-table-column>

                <el-table-column
                        prop="execType"
                        label="执行类型"
                        width="150">
                    <template slot-scope="scope">
                        <span>{{showExecType(scope.row.execType)}}</span>
                    </template>
                </el-table-column>

                <el-table-column
                        prop="lastSyncTime"
                        label="最近一次同步时间"
                        width="170">
                </el-table-column>

                <el-table-column
                        prop="lastSyncStatus"
                        label="最近一次同步状态"
                        width="170">
                    <template slot-scope="scope">
                        <el-tag :type="scope.row.lastSyncStatus === 1 ? 'success' : scope.row.lastSyncStatus === 0 ? '' :'danger'  ">
                            {{showSyncStatue(scope.row.lastSyncStatus)}}
                        </el-tag>
                    </template>
                </el-table-column>

                <el-table-column
                        prop="remark"
                        label="备注">
                </el-table-column>

                <el-table-column
                        prop=""
                        label="操作"
                        fixed="right"
                        width="300">
                    <template slot-scope="scope">
                        <el-button
                                type="text"
                                icon="el-icon-video-play"
                                @click="startForManual(scope.row.hotelContrastId)"
                        >手动执行一次</el-button>

                        <el-button
                                type="text"
                                icon="el-icon-edit"
                                @click="handelEdit(scope.row.hotelContrastId)"
                        >编辑</el-button>

                        <el-button
                                type="text"
                                icon="el-icon-school"
                                @click="toHotelInfo(scope.row.hotelContrastId)"
                        >查看房型信息</el-button>

                    </template>
                </el-table-column>


            </el-table>
        </div>

        <div class="pagination">
            <el-pagination
                    background
                    layout="sizes,total, prev, pager, next"
                    :current-page.sync="query.page"
                    :page-size.sync="query.size"
                    :total="query.pageTotal"
                    :page-sizes="[10, 20, 30, 100]"
                    @current-change="handlerSearch"
                    @size-change="handlerSearch"
            />
        </div>


        <el-dialog title="正在同步中" :visible.sync="dialogTableVisible">
            <div style="width: 80%;margin-left: 10%">
                <el-progress :text-inside="true" :stroke-width="26" :percentage="percentage"></el-progress>
            </div>
        </el-dialog>

    </div>

</template>

<script>
    import request from '@/utils/request';
    import global from '@/utils/Global';

    export default {
        name: "",
        created() {
            this.queryList();
        },

        data() {
            return {
                currentPage:3,// 当前页码
                pageSize:10,// 每页大小
                total:1000,
                query:{
                    page:1,
                    size:10,
                    selectModels:[]
                },
                selectModels:{

                },
                tableData: [],
                dialogTableVisible:false,
                percentage:15,
                testQuery:{

                }
            }
        },
        methods: {
            test(){
              console.info(this.currentPage) ;
            },
            startForManual(hotelContrastId ){
                this.$confirm('确认之后当前酒店所有房间信息将同步一次', '确定手动执行一次？', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    this.dialogTableVisible = true;
                    this.percentage = 80;
                    request({
                        url: global.rubberBasePath + "/room-sync/start-manual/"+hotelContrastId,
                        method: 'post',
                    }).then(result => {
                        if (global.SUCCESS === result.code){
                            this.dialogTableVisible = false;
                            this.percentage = 100;
                            this.toHotelInfo(hotelContrastId)
                        }else {
                            this.$message.error(result.msg);
                        }
                    })

                }).catch(() => {
                    this.$message({
                        type: 'info',
                        message: '已取消执行'
                    });
                });
            },
            handlerSearch(){
                console.info(this.query)
                const modelArray = [];
                for (const index in this.selectModels){
                    const models = {};
                    models.field = index;
                    models.type= "eq";
                    models.data = this.selectModels[index];
                    if (models.data != undefined && models.data != ""){
                        modelArray.push(models);
                    }
                }
                this.query.selectModels = modelArray;
                this.queryList();
            },
            queryList(){
                request({
                    url: global.rubberBasePath + "/hotel-config/list",
                    method: 'get',
                    params: {
                        'json':encodeURI(JSON.stringify(this.query))
                    }
                }).then(result => {
                    if (global.SUCCESS === result.code){
                        const data = result.data;
                        this.query.page = data.current;
                        this.query.pageTotal = data.total;
                        this.tableData = data.records;
                    }else {
                        this.$message.error(result.msg);
                    }
                })
            },
            toHotelInfo(hotelContrastId){
                this.$router.push({path: "/hotel/data-sync/hotel-info", query: {hotelContrastId: hotelContrastId}})
            },
            handelEdit(hotelContrastId){
                let queryInfo = {};
                if (hotelContrastId != undefined){
                    queryInfo.hotelContrastId = hotelContrastId;
                }
                this.$router.push({path: "/hotel/data-sync/edit", query: queryInfo})

            },
            showExecType(type){
                let typeInfo = {
                    "0": "自动执行",
                    "1": "仅手动执行",
                    "2": "停止自动执行",
                };
                let typeStr = type.toString();
                return typeInfo[typeStr];
            },
            showSyncStatue(syncStatue){
                let typeInfo = {
                    "0": "初始化",
                    "1": "成功",
                    "2": "失败",
                    "3": "异常",
                    "4": "携程接口同步异常",
                    "5": "龙腾接口拉取异常",
                };
                let syncStatueStr = syncStatue.toString();
                return typeInfo[syncStatueStr];
            },
        }
    }
</script>

<style scoped>
    .handle-input {
        width: 150px;
        margin-left: 20px;
        display: inline-block;
    }
</style>