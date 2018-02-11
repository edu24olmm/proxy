$.ecej ={
	post: function (url, param, callback) {
        $.post(url, param, function (data) {
                if (data.resultCode ==200){
                	 callback(data.data);
                }
				if(data.resultCode != 200){
					obj.layerAlertE('错误代码： ' + data.message, '错误');
				}
				if (data.resultCode == undefined){
					layer.msg('数据异常', { icon: 2 });
				}
        }, "json");
    },
    ajax: function (url, param, callback, async) {  // async-- ture,false
        $.ajax({
            type: "POST",
            url: url,
            async: async,
            data: param,
            success: function (data) {
                if (data.resultCode ==200){
                	 callback(data.data);
                }
				if(data.resultCode != 200){
					obj.layerAlertE('错误代码： ' + data.message, '错误');
				}
				if (data.resultCode == undefined){
					layer.msg('数据异常', { icon: 2 });
				}
            },
            dataType: "json"
        });
    }
}