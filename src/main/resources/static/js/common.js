var common=new Vue({
    el:'',
    data:{},
    methods:{
    	toIndex:function(data){
        	if (data.status == 1000) {
        		this.$alert('登录已过期，请重新登录', '提示', {
      	          confirmButtonText: '确定',
      	          callback: action => {
                		 window.parent.location.href = "../index.html";
      	          }
      	        });
      	    }
    	}
    },
    mounted:function(){}
});
//增加登录校验
Vue.http.interceptors.push((request, next) => {
    next((response) => {
        if(response.status==1000){
        	new Vue({}).$alert('登录已过期，请重新登录', '提示', {
            	callback:function(action, instance){
                    parent.location.href="/base/";
            	}
            });
        }else if(response.status==403){
        	new Vue({}).$alert('您没有权限操作此功能', '提示');
        }
        return response;
    });
});
(function(){
	Vue.http.options.emulateJSON = true;
	/*提供公用定义,方法*/
	Vue.extendData=function(config){
		if(config==null){
			new Vue({}).alert('参数 config is null');return config;
		}
		config.data=config.data||{};
		//分页相关属性
		config.data.page=config.data.page||{currentPage:1,pageSize:10,pageTotal:0,pageSizes:[10,20,30,50]};
		//当前选中行, 已选中行
		config.data.selectRow=config.data.selectRow||null;
		config.data.selectRowList=config.data.selectRowList||[];
		//全局loading
		config.data.fullScreenLoading=config.data.fullScreenLoading||false;
		config.methods=config.methods||{};
		//更换页码回调函数
		config.methods.currentPageChange=config.methods.currentPageChange||function(val){
   			this.page.currentPage = val;
            this.queryFormSearch();
   		};
   		//更换每页显示行数
   		config.methods.pageSizeChange=config.methods.pageSizeChange||function(val){
   			this.page.pageSize = val;
            this.queryFormSearch();
   		};
   		//重新排序回调函数
   		config.methods.pageSortChange=config.methods.pageSortChange||function(column){
            this.queryForm.ecejOrderBy=(column.prop==null?"createTime:d":(column.prop+":"+(column.order==null||column.order=="ascending"?"a":"d")));
            this.queryFormSearch();
        };
        //选中行回调函数,单选回调
        config.methods.currentRowChange=config.methods.currentRowChange||function(val){
   			this.selectRow = val;
   		};
   		//多选回调
   		config.methods.multiRowSelect=config.methods.multiRowSelect||function(list){
   			this.selectRowList=list;
   		};
   		config.methods.queryFormSearch=config.methods.queryFormSearch||function(){
   			this.$alert('请实现 queryFormSearch函数','提示');
   		};
   		//拷贝有效的属性值(source:来源对象, target:目标对象, propertyName:属性名称)
   		config.methods.copyEffectiveProperty=config.methods.copyEffectiveProperty||function(source, target, propertyName){
   			if(source!=null && target!=null && propertyName!=null){
   				if(source[propertyName]!=null && source[propertyName]!=""){
   					target[propertyName]=source[propertyName];
   				}
   			}
   		};
   		return config;
	};
})();