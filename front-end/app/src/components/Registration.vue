<template>
  <div class="container-fluid">
    <div class="row justify-content-center">
      <div class="col-xl-5 col-lg-6 col-md-8 col-sm-11">

        <h3> {{ return_msg }} </h3>

        <form id="post-form" @submit.prevent="processForm">
          <div id="legend">
            <h3>Register</h3>
          </div>

          <div class="form-group text-left">
            <label for="username">Username:</label>
            <input id="username" class="form-control" type="text" name="username" placeholder="Username"
                   v-model="username" v-validate="{ required: true, min: 4, max: 128 }"
                   autocomplete="username">
            <div class="error" v-if="errors.has('username')">{{errors.first('username')}}</div>
          </div>

          <div class="form-group text-left">
            <label for="email">Email:</label>
            <input id="email" class="form-control" type="text" name="email" placeholder="Email"
                   v-model="email" v-validate="{ required: true, min: 4 }"
                   autocomplete="email">
            <div class="error" v-if="errors.has('email')">{{errors.first('email')}}</div>
          </div>

          <div class="form-group text-left">
            <label for="firstname">First name:</label>
            <input id="firstname" class="form-control" type="text" name="firstName" placeholder="Firstname"
                   v-model="firstName" v-validate="{ required: true, min: 2 }"
                   autocomplete="first-name">
            <div class="error" v-if="errors.has('firstName')">{{errors.first('firstName')}}</div>
          </div>

          <div class="form-group text-left">
            <label for="lastname">Last name:</label>
            <input id="lastname" class="form-control" type="text" name="lastname" placeholder="Lastname"
                   v-model="lastName" v-validate="{ required: true, min: 2 }"
                   autocomplete="last-name">
            <div class="error" v-if="errors.has('lastname')">{{errors.first('lastname')}}</div>
          </div>

          <div class="form-group text-left">
            <label for="password">Password:</label>
            <input id="password" class="form-control" type="password" name="password" placeholder="Password"
                   v-model="password" v-validate="{ required: true, min: 6 }"
                   autocomplete="new-password">
            <div class="error" v-if="errors.has('password')">{{errors.first('password')}}</div>
          </div>

          <div class="form-group text-left">
            <label for="matchingPassword">Password confirmation:</label>
            <input id="matchingPassword" class="form-control" type="password" name="matchingPassword"
                   placeholder="Password again" autocomplete="new-password"
                   v-model="matchingPassword" v-validate="{ required: true, min: 6, confirmed: password }">
            <div class="error" v-if="errors.has('matchingPassword')">{{errors.first('matchingPassword')}}</div>
          </div>

          <input class="btn btn-primary mb-3" type="submit" value="Submit">
        </form>
      </div>
    </div>
  </div>

</template>

<script>
    import apiRequests from './../javascript/apiRequests.js';
    import errorHandling from './../javascript/errorHandling.js';

    export default {
        name: 'registration',
        data() {
            return {
                username: '',
                firstName: '',
                lastName: '',
                password: '',
                email: '',
                matchingPassword: '',
                return_msg: '',
            };
        },
        methods: {
            register() {
                apiRequests
                    .postRequestToApi('/api/register', {
                        firstName: this.firstName,
                        lastName: this.lastName,
                        password: this.password,
                        username: this.username,
                        email: this.email
                    })
                    .then(() => {
                        this.resetFields();
                        errorHandling.successMsg("Registration successful!", 1000);
                    })
                    .catch((error) => {
                            errorHandling.errorMsg(error.response.data.errors[0].defaultMessage, 1200)
                        }
                    );
            },
            resetFields() {
                this.username = '';
                this.firstName = '';
                this.lastName = '';
                this.password = '';
                this.matchingPassword = '';
                this.email = '';
                this.return_msg = '';
                this.$nextTick(() => this.$validator.reset());
            },
            processForm() {
                this.$validator.validate().then(valid => {
                    if (valid) {
                        this.register();
                    }
                        else {
                        errorHandling.errorMsg("The form wasn't filled in properly!", 1000);
                    }
                });

            }
        }
    }
</script>

<style scoped>
</style>
