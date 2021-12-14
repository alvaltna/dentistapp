<template>
  <nav class="p-2">
    <div class="text-right">
      <div style="float:left">
        <Search/>
      </div>
      <router-link class="btn btn-dark mx-1" id="home" tag="button" to="/"
                  exact>
        Home
      </router-link>
      <router-link class="btn btn-dark mx-1" id="makeAppointments" tag="button" to="/makeAppointment" @click="checkToken"
                   v-if="loggedIn" exact>
        Make an appointment
      </router-link>

      <router-link class="btn btn-dark mx-1" id="register" tag="button" to="/registration" @click="checkToken"
                   v-if="!loggedIn" exact>
        Register
      </router-link>

      <router-link class="btn btn-dark mx-1" id="myAppointments" tag="button" to="/userAppointments" v-if="loggedIn"
                   exact>
        My Appointments
      </router-link>

      <button class="btn btn-dark mx-1" id="logout" v-if="loggedIn" @click="logout">Logout
      </button>

      <div class="dropdown" v-if="!loggedIn">
        <button type="button" id="dropdownMenuButton" data-toggle="dropdown" aria-haspopup="true"
                aria-expanded="false" class="btn btn-dark dropdown-toggle">Login
        </button>
        <div class="dropdown-menu dropdown-menu-right py-0">
          <div class="px-3 pt-3 login-dropdown">

            <form @submit.prevent="logIn">
              <div class="form-group">
                <input id="usernameInput" placeholder="Username" name="loginUsername"
                       class="form-control form-control-sm custom-input" type="text"
                       v-model="loginUsername"
                       autocomplete="username">
              </div>

              <div class="form-group">
                <input id="passwordInput" placeholder="Password" name="loginPassword"
                       class="form-control form-control-sm" type="password"
                       v-model="loginPassword"
                       autocomplete="new-password">
              </div>
              <div class="form-group">
                <button type="submit" class="btn btn-primary btn-block" name="login">Login</button>
              </div>
            </form>

            <div class="form-group text-center">
              <small>
                <router-link id="forgotPassword" tag="a" to="/forgotPassword" exact>
                  Forgot password?
                </router-link>
              </small>
            </div>
          </div>
        </div>
      </div>
    </div>
  </nav>
</template>

<script>
    import errorHandling from './../../javascript/errorHandling.js';
    import apiRequests from './../../javascript/apiRequests.js';
    import VueJwtDecode from 'vue-jwt-decode';
    import Search from './Search';


    export default {
        name: "Login",
      components: {Search},
      data() {
            return {
                loginUsername: '',
                loginPassword: '',
                loggedIn: false,
            };
        },
        methods: {
            logIn() {
                apiRequests
                    .postRequestToApi('/api/login', {
                        username: this.loginUsername,
                        password: this.loginPassword
                    })
                    .then((response) => {
                        localStorage.setItem("Authorization", response.headers.authorization);
                        this.resetFields();
                        this.loggedIn = true;
                        errorHandling.successMsg("You are logged in!", 1000);
                        this.$router.push({name: 'viewAppointments'})
                    })
                    .catch(() => {
                        errorHandling.errorMsg("Wrong username or password, try again!", 1200);
                    });
            },
            resetFields() {
                this.loginUsername = '';
                this.loginPassword = '';
            },
            logout() {
                this.loggedIn = false;
                localStorage.removeItem("Authorization");
                this.$router.push("/");
            },
            checkToken() {
                let token = localStorage.getItem('Authorization');
                if (token === null || token === undefined) {
                    this.loggedIn = false;
                    this.$router.push("/");
                }
            }
        },
        beforeMount() {
            let token = localStorage.getItem('Authorization');
            if (token !== null && token !== undefined) {
                let tokenString = token.split("Bearer ")[1];
                let exp = (VueJwtDecode.decode(tokenString))["exp"];
                if (Date.now() / 1000 > exp - 86400) {
                    this.loggedIn = false;
                    localStorage.removeItem("Authorization");
                    this.$router.push("/");
                }
            }
        },
        mounted() {
            if (localStorage.getItem("Authorization") !== null) {
                apiRequests.getRequestToApiWithAuthorization('/api/check')
                    .then(response => {
                        if (response.status === 200) {
                            this.loggedIn = true;
                        }
                    })
                    .catch(() => {
                        this.loggedIn = false;
                        this.logout();
                    });
            }
        }
    }


</script>

<style scoped>
  .dropdown {
    display: inline-block !important;
  }

  nav {
    background-color: #D6BDF3;
  }

  .login-dropdown {
    background-color: white;
    text-align: center;
  }

  .custom-input {
    width: 200px;
  }

</style>
