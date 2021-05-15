<template>
    <div>
        <el-table
                :data="tableData"
                v-loading="loading"
                style="width: 100%"
                max-height="550">

            <el-table-column
                    prop="hotelIdInfo"
                    label="同步的酒店信息"
                    width="200">
            </el-table-column>

            <el-table-column
                    prop="roomIdInfo"
                    label="同步的房间信息"
                    width="200">
            </el-table-column>


            <el-table-column
                    prop="syncTime"
                    label="执行同步的时间"
                    width="150">
            </el-table-column>

            <el-table-column
                    prop="pushDate"
                    label="设置价格的日期"
                    width="150">
            </el-table-column>

            <el-table-column
                    prop="syncRequestInfo"
                    label="同步详情"
                    min-width="200px"
                    >
                <template slot-scope="scope" >
                    <span>{{showSyncInfo(scope.row.syncRequestInfo)}}</span>
                    <el-popover
                            placement="bottom"
                            title="同步详情"
                            width="200"
                            trigger="click"
                            :content="scope.row.syncRequestInfo">
                        <el-link slot="reference" icon="el-icon-paperclip"></el-link>
                    </el-popover>
                </template>
            </el-table-column>


            <el-table-column
                    prop="syncStatus"
                    label="价格详情"
                    width="150">
                <template slot-scope="scope">
                    <div>原始价格:{{scope.row.originPrice > 0 ? scope.row.originPrice : '--'}}元 </div>
                    <div>浮动价格：+{{scope.row.floatPrice != undefined ? scope.row.floatPrice : 0}}元 </div>
                    <div>设置价格：{{scope.row.pushPrice}} 元</div>
                </template>
            </el-table-column>
            <el-table-column
                    prop="syncStatus"
                    label="同步状态"
                    width="150">
                <template slot-scope="scope">
                    <el-tag :type="scope.row.syncStatus === 1 ? 'success' : scope.row.lastSyncStatus === 0 ? '' :'danger'  ">
                        {{showSyncStatue(scope.row.syncStatus)}}
                    </el-tag>
                </template>
            </el-table-column>

            <el-table-column
                    prop="syncResponseInfo"
                    label="返回详情"
                    width="150">
                <template slot-scope="scope">
                   {{showResponseInfo(scope.row.syncResponseInfo)}}
                    <el-popover
                            placement="bottom"
                            title="返回详情"
                            width="200"
                            trigger="click"
                            :content="scope.row.syncResponseInfo">
                        <el-link slot="reference" icon="el-icon-paperclip"></el-link>
                    </el-popover>
                </template>
            </el-table-column>

            <el-table-column
                    prop="remark"
                    label="备注"
                    width="150">
            </el-table-column>

            <el-table-column
                    prop="execType"
                    label="执行的类型"
                    width="150">
                <template slot-scope="scope">
                    {{showExecType(scope.row.execType)}}
                </template>
            </el-table-column>

        </el-table>

        <div class="pagination">
            <el-pagination
                    background
                    layout="sizes,total, prev, pager, next"
                    :current-page.sync="queryInfo.page"
                    :page-size.sync="queryInfo.size"
                    :total="queryInfo.pageTotal"
                    :page-sizes="[10, 20, 30, 100]"
                    @current-change="handlerSearch"
                    @size-change="handlerSearch"
            />
        </div>

    </div>

</template>

<script>
    import request from '@/utils/request';
    import global from '@/utils/Global';

    export default {
        name: "",
        props:{
            openWaterRoomInfo:{
                required:true
            }
        },
        data() {
            return {
                queryInfo:{
                    page:1,
                    size:10,
                    selectModels:[]
                },
                tableData: [],
                loading: true,
            }
        },
        created() {
            this.queryList()
        },
        watch:{
            openWaterRoomInfo:function () {
                this.queryList()
            }
        },
        methods: {
            handlerSearch(){
                this.queryList();
            },
            queryList(){
                const modelArray = [];

                const models = {};
                models.field = "roomContrastId";
                models.type= "eq";
                models.data = this.openWaterRoomInfo.roomContrastId;
                modelArray.push(models);
                this.queryInfo.selectModels = modelArray;


                request({
                    url: global.rubberBasePath + "/room-sync-water/list",
                    method: 'get',
                    params: {
                        'json':encodeURI(JSON.stringify(this.queryInfo))
                    }
                }).then(result => {
                    if (global.SUCCESS === result.code){
                        const data = result.data;
                        this.queryInfo.page = data.current;
                        this.queryInfo.pageTotal = data.total;
                        this.tableData = data.records;
                    }else {
                        this.$message.error(result.msg);
                    }
                    this.loading = false;
                })
            },
            showSyncStatue(syncStatue){
                let typeInfo = {
                    "0": "初始化",
                    "1": "成功",
                    "2": "失败",
                    "3": "异常",
                    "4": "携程接口同步异常",
                    "5": "龙腾接口拉取异常",
                    "6":"龙腾房间不存在",
                    "7":"龙腾房间为价格为空"
                };
                let syncStatueStr = syncStatue.toString();
                return typeInfo[syncStatueStr];
            },

            showResponseInfo(response){
                if (response == undefined || response == ""){
                    return '-';
                }
                let jsonObject = JSON.parse(response);
                return jsonObject.logKey;
            },
            showExecType(type){
                let typeInfo = {
                    "0": "自动执行",
                    "1": "手动执行",
                    "2": "停止自动执行",
                };
                let typeStr = type.toString();
                return typeInfo[typeStr];
            },
            showSyncInfo(data){
                let msg = "";
                let jsonInfo = JSON.parse(data);
                if (jsonInfo.roomPriceModel != undefined){
                    msg += " 价格:" + jsonInfo.roomPriceModel.roomPrice+"元，";
                    msg += " 早餐数:" + jsonInfo.roomPriceModel.breakfast+"份，";
                }
                if (jsonInfo.roomStatusModel != undefined){
                    let code = jsonInfo.roomStatusModel.saleStatus;
                    let name = "";
                    if (code == 0){
                        name = "满房";
                    }else if (code == 1){
                        name = "销售";
                    }else if (code == 2){
                        name = "限量";
                    }
                    msg += " 房态:" + name+"("+code+")";
                }
                return msg;
            }
        },
    }
</script>

<style scoped>

</style>