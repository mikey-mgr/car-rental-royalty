<template>
    <div class="container">
        <div class="row">
            <div class="col-md-12 justify-content-center text-start d-flex pt-3">
                <div id="signin-div" class="flex-item p-5">
                    <h2 class="">Sign In</h2>
                    <form @submit="signin" class="form-group">
                        <div class="form-group">
                            <label for="email">Email</label>
                            <input v-model="email" id="email" type="email" class="form-control" required>
                        </div>
                        <div class="form-group pt-4">
                            <label for="password">Password</label>
                            <input v-model="password" id="password" type="password" class="form-control" required>
                        </div>
                        <button type="submit" class="btn btn-primary mt-4">Login</button>
                        <hr class="mb-5">
                        <label class="col-12 pl-0 font-italic">Don't have an Account?</label>
                        <router-link :to="{name: 'SignupView'}"> 
                                <button class="btn btn-primary create-account">Create Account</button>
                        </router-link>    
                    </form>
                </div>
            </div>
        </div>
        
    </div>
</template>

<script>
import axios from 'axios';
import swal from 'sweetalert';

export default {
    name: 'SigninView',
    props:['baseURL'],
    data(){
        return{
            email: null,
            password: null,
        };
    },
    methods: {
        async signin (e){
            e.preventDefault();
            const body = {
                email: this.email,
                password: this.password,
            }
            
            try {
                // Step 1: Login via Spring Security form login
                await axios({
                    method: "post",
                    url: `${this.baseURL}/login`,
                    data: new URLSearchParams(body),
                    headers: {
                        "Content-Type": "application/x-www-form-urlencoded",
                    },
                    withCredentials: true,
                    maxRedirects: 0, // Don't follow redirects automatically
                    validateStatus: (status) => status >= 200 && status < 400, // Accept 3xx redirects
                });
                
                // Step 2: Get user info after successful login
                const signinResponse = await axios.get(`${this.baseURL}/user/signin`, {
                    withCredentials: true
                });
                
                const loginInfo = signinResponse.data;
                if(loginInfo.status === "Login Success"){
                    this.$emit("usersInfo");
                    swal({
                        text: "Login successful, redirecting",
                        icon: "success"
                    });
                    localStorage.setItem("token", loginInfo.token);
                    localStorage.setItem("role", loginInfo.role);
                    window.location.replace("/home");
                } else {
                    swal({
                        text: "Invalid details",
                        icon: "warning"
                    });
                }
            } catch (err) {
                console.log('err', err);
                if(err.response && err.response.status === 401){
                    swal({
                        text: "Invalid email or password",
                        icon: "error"
                    });
                } else if(err.code === "ERR_NETWORK"){
                    swal({
                        text: "Network error, please check your connection",
                        icon: "error"
                    });
                } else {
                    swal({
                        text: "Login failed. Please try again.",
                        icon: "error"
                    });
                }
            }
        },
    },
    mounted(){
        
    }
    
}
</script>


<style scoped>
.btn-primary {
    background-color: #f0c14b;
    color: black;
    border-color: #f0c14b;
}

/* @media screen {
    #signin-div{
    }
} */
.create-account{
    background-color: lightgray;
    border-color: lightgray;
}

</style>