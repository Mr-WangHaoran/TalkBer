window.onload = function(){

    //敏感词数组
    var arr = ["操","妈的","儿子","老子","劳资","fuck","Fuck","日你","爷","sb","SB"];

    function checkNickname(rule,value,callback){
        if(value===''){
            callback(new Error("昵称不能为空"))
        }else if(arr.indexOf(value)!=-1){
            callback(new Error("昵称包含敏感词汇，请重新输入"))
        }
        callback();
    }
    function checkPhoneNumber(rule,value,callback){
        if(value===''){
            callback(new Error("手机号不能为空"))
        }else if(value.length!=11 || !/^1[3|4|5|8][0-9]\d{4,8}$/.test(value)){
            callback(new Error("无效的手机号"))
        }
        callback();
    }
    function checkEmail(rule,value,callback){
        if(value===''){
            callback(new Error("邮箱不能为空"))
        }else if(!/^([a-zA-Z0-9]+[_|_|\-|.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|_|.]?)*[a-zA-Z0-9]+\.[a-zA-Z]{2,6}$/i.test(value)){
            callback(new Error("无效的邮箱地址"))
        }
        callback();
    }
    function checkPassword(rule,value,callback){
        if (value === '') {
            callback(new Error('请输入密码'));
          }else if(value.length<6 || value.length>16){
            callback(new Error("密码长度限制在6到16位之间"))
        } 
            callback();
    }
    function QPassword(rule,value,callback){
        if (value === '') {
            callback(new Error('请再次输入密码'));
          } else if (value !== vm1.form.password) {
            callback(new Error('两次输入密码不一致!'));
          } else {
            callback();
          }
    }
    let vm1 = new Vue({
        el:'#app',
        data:{
            form:{
                nickname:'',
                phoneNumber:'',
                email:'',
                sex:0,
                password:'',
                q_password:''
            },
            rules:{
                nickname:[{validator:checkNickname,triger:'blur'}],
                phoneNumber:[{validator:checkPhoneNumber,triger:'blur'}],
                email:[{validator:checkEmail,triger:'blur'}],
                password:[{validator:checkPassword,triger:'blur'}],
                q_password:[{validator:QPassword,triger:'blur'}],
            }
        },
        methods:{
            submitForm(formName){
                this.$refs[formName].validate((valid)=>{
                    if(valid){
                        // 注册逻辑
                        var that = this;
                        var canReg = true;
                        $.ajax({
                            url:'/searchPhone?phoneNumber='+that.form.phoneNumber,
                            type:'get',
                            async:false,
                            contentType:'application/json',
                            success:function(res){
                                res = JSON.parse(res);
                                if(res.status==='error'){
                                    that.$message.error("该手机号已被注册");
                                    canReg =false;
                                    return;
                                }
                            }
                        })
                       /* axios.get("/searchEmail?email="+this.form.email).then(res=>{
                            console.log(res);
                            if(res.data.status==='error'){
                                that.$message.error(res.data.message);
                                canReg = false;
                                return;
                            }
                        });*/
                        $.ajax({
                            url:'/searchEmail?email='+that.form.email,
                            type:'get',
                            async:false,
                            contentType:'application/json',
                            success:function(res){
                                res = JSON.parse(res);
                                if(res.status==='error'){
                                    that.$message.error("该邮箱已被注册");
                                    canReg =false;
                                    return;
                                }
                            }
                        })
                        if(canReg){
                           /* axios.post("/user-registry",this.form).then(res=>{
                                console.log(res);
                            });*/
                            $.ajax({
                                url:'/user-registry',
                                type:'post',
                                data:that.form,
                                async:false,
                                success:function(res){
                                    res = JSON.parse(res);
                                    if (res.status=='success'){
                                        that.$message({
                                            type:'success',
                                            message:'注册成功！',
                                            showClose: true,
                                            onClose:function(){
                                                window.location.href="/login";
                                            }
                                        })
                                    }else{
                                        that.$message({
                                            type:'error',
                                            message:'注册失败！请联系管理员！',
                                            showClose: true,
                                            onClose:function(){
                                                window.location.href="/registry";
                                            }
                                        })
                                    }
                                }
                            })
                        }
                    }else{
                        this.$message.error({message:'请按照错误提示修改填入信息！',showClose:true});
                        return false;
                    }
                });
            },
            resetForm(formName) {
                this.$refs[formName].resetFields();
            },
        }
    }) 
}