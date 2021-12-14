<template>
  <div class="container-fluid">
    <div class="row justify-content-center">
      <div class="col-xl-5 col-lg-6 col-md-8 col-sm-11">

        <h5>Password reset form for user {{username}}</h5>
        <form id="post-form" @submit.prevent="processForm">

          <div class="form-group text-left">
            <label for="password">New password:</label>
            <input id="password" class="form-control" type="password" name="newPassword" placeholder="Password"
                   v-model="newPassword" v-validate="{ required: true, min: 6 }"
                   autocomplete="new-password">
            <div class="error" v-if="errors.has('newPassword')">{{errors.first('newPassword')}}</div>
          </div>

          <div class="form-group text-left">
            <label for="matchingPassword">Enter same password again:</label>
            <input id="matchingPassword" class="form-control" type="password" name="matchingPassword"
                   placeholder="Password again" autocomplete="new-password"
                   v-model="matchingPassword" v-validate="{ required: true, min: 6, confirmed: newPassword }">
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
        name: "PasswordReset",
        data() {
            return {
                username: '',
                token: '',
                newPassword: '',
                matchingPassword: '',
            };
        },
        methods: {
            register() {
                apiRequests
                    .postRequestToApi('/api/resetPassword', {
                        username: this.username,
                        token: this.token,
                        newPassword: this.newPassword,
                    })
                    .then(() => {
                        this.resetFields();
                        errorHandling.successMsg("Password changed!", 1000);
                    })
                    .catch((error) => {
                            errorHandling.errorMsg(error.response.data.message, 1200)
                        }
                    );
            },
            resetFields() {
                this.username = '';
                this.newPassword = '';
                this.matchingPassword = '';
                this.token = '';
                this.$nextTick(() => this.$validator.reset());
            },
            processForm() {
                this.$validator.validate().then(valid => {
                    if (valid) {
                        this.register();
                    } else {
                        errorHandling.errorMsg("The form wasn't filled in properly!", 1000);
                    }
                });
            }
        },
        mounted() {
            this.username = this.$route.query.username;
            this.token = this.$route.query.token;
        }
    }
</script>

<style scoped>

</style>