import Vue from 'vue';
import Router from 'vue-router';

Vue.use(Router);

export default new Router({
    routes: [
        {
            path: '/',
            redirect: '/welcome'
        },
        {
            path: '/',
            component: () => import(/* webpackChunkName: "home" */ '../views/home/Home.vue'),
            meta: { title: '自述文件' },
            children: [
                {
                    path: '/welcome',
                    component: () => import(/* webpackChunkName: "welcome" */ '../views/welcome.vue'),
                    meta: { title: '我的主页' }
                },
                {
                    path: '/sys/user-view',
                    component: () => import(/* webpackChunkName: "welcome" */ '../views/sys/user/UserView'),
                    meta: { title: '用户管理' }
                },
                {
                    path: '/sys/role-view',
                    component: () => import(/* webpackChunkName: "welcome" */ '../views/sys/role/RoleView'),
                    meta: { title: '角色管理' }
                },
                {
                    path: '/sys/dept-view',
                    component: () => import(/* webpackChunkName: "welcome" */ '../views/sys/dept/DeptView'),
                    meta: { title: '部门管理' }
                },
                {
                    path: '/sys/menu-view',
                    component: () => import(/* webpackChunkName: "welcome" */ '../views/sys/menu/MenuView'),
                    meta: { title: '菜单管理' }
                },
                {
                    path: '/sys/permission-dict-view',
                    component: () => import(/* webpackChunkName: "welcome" */ '../views/sys/permission/PermissionDictView'),
                    meta: { title: '权限字典' }
                },
                {
                    path: '/sys/authorize-view',
                    component: () => import(/* webpackChunkName: "welcome" */ '../views/sys/authorize/AuthorizeView'),
                    meta: { title: '权限信息-v1' }
                },
                {
                    path: '/sys/authorize-config',
                    component: () => import(/* webpackChunkName: "welcome" */ '../views/sys/authorize/AuthorizeConfigView'),
                    meta: { title: '权限配置' }
                },

                {
                    path: '/hotel/data-sync',
                    component: () => import(/* webpackChunkName: "welcome" */ '../views/hotel/dataSync/index'),
                    meta: { title: '数据同步管理' }
                },

                {
                    path: '/hotel/data-sync/hotel-info',
                    component: () => import(/* webpackChunkName: "welcome" */ '../views/hotel/dataSync/hotelInfo'),
                    meta: { title: '数据同步管理' }
                },
                {
                    path: '/hotel/data-sync/edit',
                    component: () => import(/* webpackChunkName: "welcome" */ '../views/hotel/dataSync/hotelAddEdit'),
                    meta: { title: '数据同步管理' }
                },

                {
                    path: '/dashboard',
                    component: () => import(/* webpackChunkName: "dashboard" */ '../components/page/Dashboard.vue'),
                    meta: { title: '系统首页' }
                },
                {
                    path: '/icon',
                    component: () => import(/* webpackChunkName: "icon" */ '../components/page/Icon.vue'),
                    meta: { title: '自定义图标' }
                },
                {
                    path: '/table',
                    component: () => import(/* webpackChunkName: "table" */ '../components/page/BaseTable.vue'),
                    meta: { title: '基础表格' }
                },
                {
                    path: '/tabs',
                    component: () => import(/* webpackChunkName: "tabs" */ '../components/page/Tabs.vue'),
                    meta: { title: 'tab选项卡' }
                },
                {
                    path: '/form',
                    component: () => import(/* webpackChunkName: "form" */ '../components/page/BaseForm.vue'),
                    meta: { title: '基本表单' }
                },
                {
                    // 富文本编辑器组件
                    path: '/editor',
                    component: () => import(/* webpackChunkName: "editor" */ '../components/page/VueEditor.vue'),
                    meta: { title: '富文本编辑器' }
                },
                {
                    // markdown组件
                    path: '/markdown',
                    component: () => import(/* webpackChunkName: "markdown" */ '../components/page/Markdown.vue'),
                    meta: { title: 'markdown编辑器' }
                },
                {
                    // 图片上传组件
                    path: '/upload',
                    component: () => import(/* webpackChunkName: "upload" */ '../components/page/Upload.vue'),
                    meta: { title: '文件上传' }
                },
                {
                    // vue-schart组件
                    path: '/charts',
                    component: () => import(/* webpackChunkName: "chart" */ '../components/page/BaseCharts.vue'),
                    meta: { title: 'schart图表' }
                },
                {
                    // 拖拽列表组件
                    path: '/drag',
                    component: () => import(/* webpackChunkName: "drag" */ '../components/page/DragList.vue'),
                    meta: { title: '拖拽列表' }
                },
                {
                    // 拖拽Dialog组件
                    path: '/dialog',
                    component: () => import(/* webpackChunkName: "dragdialog" */ '../components/page/DragDialog.vue'),
                    meta: { title: '拖拽弹框' }
                },
                {
                    // 国际化组件
                    path: '/i18n',
                    component: () => import(/* webpackChunkName: "i18n" */ '../components/page/I18n.vue'),
                    meta: { title: '国际化' }
                },
                {
                    // 权限页面
                    path: '/permission',
                    component: () => import(/* webpackChunkName: "permission" */ '../components/page/Permission.vue'),
                    meta: { title: '权限测试', permission: true }
                },
                {
                    path: '/404',
                    component: () => import(/* webpackChunkName: "404" */ '../components/page/404.vue'),
                    meta: { title: '404' }
                },
                {
                    path: '/403',
                    component: () => import(/* webpackChunkName: "403" */ '../components/page/403.vue'),
                    meta: { title: '403' }
                },
                {
                    path: '/donate',
                    component: () => import(/* webpackChunkName: "donate" */ '../components/page/Donate.vue'),
                    meta: { title: '支持作者' }
                }
            ]
        },
        {
            path: '/login',
            component: () => import(/* webpackChunkName: "login" */ '../views/Login.vue'),
            meta: { title: '登录' }
        },
        {
            path: '*',
            redirect: '/404'
        }
    ]
});
