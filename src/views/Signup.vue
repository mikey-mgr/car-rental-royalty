<template>
    <div class="container">
        <div class="row">
            <div class="col-md-12 text-center justify-content-center d-flex pt-3">
                <div id="signup-div" class="flex-item p-5">
                    <label for="email"><h2 class="">Create Account</h2></label>
                    <form @submit="signup" class="form-group">
                        <div class="form-group pt-4">
                            <label for="email">Email</label>
                            <input v-model="email" id="email" type="email" class="form-control" required>
                        </div>
                        <div class="row">
                            <div class="col-md-6 pt-4">
                                <div class="form-group">
                                    <label for="firstName">First Name</label>
                                    <input v-model="firstName" id="firstName" type="text" class="form-control" required>
                                </div>
                            </div>
                            <div class="col-md-6 pt-4">
                                <div class="form-group">
                                    <label for="lastName">Last Name</label>
                                    <input v-model="lastName" id="lastName" type="text" class="form-control" required>
                                </div>
                            </div>
                        </div>
                        <div class="form-group pt-4">
                            <label for="password">Password</label>
                            <input v-model="password" id="password" type="password" class="form-control" required>
                        </div>
                        <div class="form-group pt-4">
                            <label for="confirmPassword">Confirm Password</label>
                            <input v-model="confirmPassword" id="confirmPassword" type="password" class="form-control" required>
                        </div>
                        <button type="submit" class="btn btn-primary m-0 mt-4">Create Account</button>
                        <hr class="mb-4">
                        <label class="col-12 font-italic">Already have an Account?</label>
                        <router-link :to="{name: 'SigninView'}"> 
                                <button class="btn btn-primary login">Login</button>
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
    name: "SignupView",
    props:['baseURL'],
    data(){
        return{
            email: null,
            firstName: null,
            lastName: null,
            password: null,
            confirmPassword: null,
        }
    },
    methods: {
        async signup(e){
            e.preventDefault();
            if (this.password === this.confirmPassword){
                //call signup api
                const user = {
                    email: this.email,
                    firstName: this.firstName,
                    lastName: this.lastName,
                    password: this.password,
                };
                await axios.post(`${this.baseURL}/user/signup`, user)
                .then(() => {
                    this.$router.replace('/');
                    swal({
                        text: "Signup successful, please login now",
                        icon: "success"
                    });
                    this.$router.push({name: 'SigninView'})
                })
                .catch((err) => {
                    console.error('err', err);
                    if(err.response.data == "User with email is already present"){
                        swal({
                            text: "A user with this email already exists",
                            icon: "info"
                        });
                    }
                });

            } else {
                //show some error
                swal({
                        text: "Passwords don't match, please try again",
                        icon: "error"
                    });
            }
        }
    },
    mounted() {
        
    },
    
}
</script>

<style scoped>
.btn-primary {
    background-color: #f0c14b;
    color: black;
    border-color: #f0c14b;
}
#logo {
  width: 120px;
  margin-left: 0px;
  margin-right: 20px;
}
@media screen {
    #signup-div{

    }
}
.login {
    background-color: lightgray;
    border-color: lightgray;
}

</style>