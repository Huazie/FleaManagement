/**
 * 	登录js模块
 * 
 *  @author huazie
 *  @date 2017/03/01
 */
define(function(require, exports, module) {
	
	//跳主登录
	ReqUrlMap.put("fleaerLogin", "fleaerLogin!login.flea");
	
	/**
	 * 登录界面初始化
	 */
	exports.init = function(){
		
		$("#tabs").tabs();//jquery tabs
		
		$("#login").on("click",function(){//登录按钮
			var name = $("#name").val();
			var password = $("#password").val();
			
			if(name.trim() == ""){
				Huazie.dialog.tips("warning", "亲，账号不能为空哟", 1);
				return;
			}
			
			if(password.trim() == ""){
				Huazie.dialog.tips("warning", "亲，密码不能为空哟", 1);
				return;
			}
			
			var $thiz = $(this);
			
			$thiz.addClass("disabled").html(Huazie.msg.btnText("key", "正在登陆中..."));
			
			var cmd = {
				"fleaerLogin.name" : name.trim(),
				"fleaerLogin.password" : password.trim()
			}
			
			// 登录验证
			Huazie.ajax.postJson(ReqUrlMap.get("fleaerLogin"), cmd, function(data, status) {
				
				var result = data;

				if(status){
					if(result.retCode == "Y"){
						Huazie.dialog.tips("info", result.retMess, 2);
						$thiz.removeClass("disabled").html(Huazie.msg.btnText("unlock", "登录"));
						location.href = ReqUrlMap.get("fleaerHome");
					}else if(result.retCode == "N"){
						Huazie.dialog.tips("warning", result.retMess, 2);
						$("#name").val('');
						$("#password").val('');
						$thiz.removeClass("disabled").html(Huazie.msg.btnText("lock", "登录"));
					}
				}else{
					Huazie.dialog.tips("warning", result.retMess, 2);
					$thiz.removeClass("disabled").html(Huazie.msg.btnText("lock", "登录"));
				}

			});
			
		});
	}
	
});