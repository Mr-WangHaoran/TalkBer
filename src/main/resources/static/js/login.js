window.onload = function(){
    function checkPhoneOrEmail(rule,value,callback){
        if(!value){
            callback(new Error("手机号/邮箱不能为空"))
        }
        // 说明输入的是邮箱
        if(value.indexOf("@")!=-1){
           let reg = /^([a-zA-Z0-9]+[_|_|\-|.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|_|.]?)*[a-zA-Z0-9]+\.[a-zA-Z]{2,6}$/i
           if(!reg.test(value)){
                callback(new Error("邮箱格式违法"))
           }
        }else if(value.length!=11 || !/^1[3|4|5|8][0-9]\d{4,8}$/.test(value)){
            callback(new Error("无效的手机号"))
        }
        callback();
    }
    function checkCode(rule,value,callback){
        if(value==''){
            callback(new Error("请输入验证码"))
        }
        callback();
    }
    let vm = new Vue({
        el:"#app",
        data:{
            carousals:["./image/carousel_1.jpg","./image/carousel_2.jpg","./image/carousel_3.jpg","./image/carousel_4.jpg"],//轮播图
            form:{
                password:'',
                phoneOrEmail:'',
                rememberMe:false,
                code:'',  
            },
            rules:{
                phoneOrEmail:[{validator:checkPhoneOrEmail,triger:'blur'}],
                code:[{validator:checkCode,triger:'blur'}]
            },
            codeImg:'#'
        },
        created:function(){
            axios.get("/last-user-login").then(res=>{
                if(res.data=="index"){
                    window.location.href="/index.html";
                }
            });
        },
        mounted:function(){
            var that = this;
            axios.get("/verifyCode",{responseType:'blob'}).then(res=>{
                //将后台传来的二进制流转化为图片
                let blob = new Blob([res.data]);   // 返回的文件流数据
                let url = window.URL.createObjectURL(blob);  // 将他转化为路径
                that.codeImg = url  // 将转换出来的路径赋值给变量，其实和上一步结合就可以
            })
        },
        methods:{
            submitForm(formName){
                var that = this;
                this.$refs[formName].validate((valid)=>{
                    if(valid){
                        // 登录逻辑
                        $.ajax({
                            method: 'post',
                            url:'/user-login',
                            data: {
                                phoneOrEmail: that.form.phoneOrEmail,
                                password: that.form.password,
                                rememberMe: that.form.rememberMe,
                                code: that.form.code
                            },
                            success:function(res){
                                var parse = JSON.parse(res);
                                if(parse.status==='error'){
                                    that.$message.error(parse.message);
                                }else{
                                    window.location.href="/index";
                                }

                            }
                        })
                    }else{
                        this.$message.error({message:'请按照错误提示修改填入信息！',showClose:true});
                        return false;
                    }
                });
            },
            resetForm(formName) {
                this.$refs[formName].resetFields();
            },
            switchVerifyCode(){
                var that = this;
                axios.get("/verifyCode",{responseType:'blob'}).then(res=>{
                    //将后台传来的二进制流转化为图片
                    let blob = new Blob([res.data]);   // 返回的文件流数据
                    let url = window.URL.createObjectURL(blob);  // 将他转化为路径
                    that.codeImg = url  // 将转换出来的路径赋值给变量，其实和上一步结合就可以
                })
            }
        }
    });


}