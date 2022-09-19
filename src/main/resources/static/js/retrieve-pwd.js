window.onload = function(){
    function checkEmail(rule,value,callback){
        if(value.length==0){
            callback(new Error("邮箱不能为空"))
        }else if(!/^([a-zA-Z0-9]+[_|_|\-|.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|_|.]?)*[a-zA-Z0-9]+\.[a-zA-Z]{2,6}$/i.test(value)){
            callback(new Error("邮箱格式违法"))
        }else{
            // ajax请求查询邮箱是否存在
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
          } else if (value !== vm2.form.password) {
            callback(new Error('两次输入密码不一致!'));
          } else {
            callback();
          }
    }
    let vm2 = new Vue({
        el:'#app',
        data:{
            active:0,
            code:'',
            form:{
                email:'',
                password:'',
                q_password:''
            },
            disabled:true,
            rules:{
                email:[{validator:checkEmail,trigger:'blur'}],
                password:[{validator:checkPassword,trigger:'blur'}],
                q_password:[{validator:QPassword,trigger:'blur'}]
            }
        },
        methods:{
            next(){
                //监测邮箱是否存在
                var that = this;
                if(this.active===0){
                    axios.get("/searchEmail?email="+this.form.email).then(res=>{
                        if(res.data.status!='error'){
                            that.$message.error("邮箱不存在！请重新输入");
                            that.active=0;
                            return;
                        }else{
                            // 发送验证码到指定邮箱
                            axios.get("/sendCodeMail?email="+this.form.email).then(res=>{console.log(res)})
                        }
                    })
                }

                if(this.active===1){
                    // 验证码要进行对比
                    var that = this;
                    axios.get("/mailCodeVerify?code="+this.code).then(res=> {
                        //验证码输入正确
                        if (res.data.status == 'success') {
                            that.$message({
                                type: 'success',
                                message: '验证成功，现在您可以修改密码',
                                showClose: true,
                                onClose: function () {
                                    that.active = 2;
                                    return;
                                }
                            })
                        } else {
                            that.$message({
                                type: 'error',
                                message: res.data.message,
                                showClose: true,
                            })
                            that.active=1;
                        }
                    })
                }else if(this.active===2){
                    // 修改密码
                    axios.post("/update-pwd",{
                            password:that.form.password,
                            email:that.form.email
                        }).then(res=>{
                        //修改成功,直接跳转
                        if(res.data.status=='success'){
                            that.$message({
                                type:'success',
                                message:res.data.message,
                                showClose:true,
                                onClose:function () {
                                    window.location.href="/login.html";
                                }
                            })
                        }else{
                            that.$message({
                                type:'error',
                                message:res.data.message,
                                showClose:true,
                                onClose:function () {
                                    window.location.href="/retrieve-pwd.html";
                                }
                            })
                        }
                    })
                }
                this.active++;       
            },
            blur(formName){
                this.$refs[formName].validate((valid)=>{
                    if(valid){
                        // 打开下一步按钮
                        this.disabled = false;
                    }else{
                        this.disabled = true;
                    }
                });
            }
        }
    }) 
}